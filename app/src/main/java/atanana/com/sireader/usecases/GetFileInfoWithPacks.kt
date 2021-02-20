package atanana.com.sireader.usecases

import atanana.com.sireader.database.PackEntity
import atanana.com.sireader.database.PacksDao
import atanana.com.sireader.database.QuestionFileEntity
import atanana.com.sireader.database.QuestionFilesDao
import atanana.com.sireader.screens.fileinfo.PackItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.zip
import javax.inject.Inject

class GetFileInfoWithPacks @Inject constructor(
    private val filesDao: QuestionFilesDao,
    private val packsDao: PacksDao
) {
    fun getInfo(fileId: Int): Flow<Pair<QuestionFileEntity, List<PackItem>>> =
        file(fileId).zip(packs(fileId)) { file, packs ->
            Pair(file, mapPacks(packs, file.lastReadPackId))
        }

    private fun mapPacks(packs: List<PackEntity>, lastReadPackId: Int?) =
        packs.map {
            PackItem(it, it.id == lastReadPackId)
        }

    private fun packs(fileId: Int) = packsDao.packForFile(fileId)

    private fun file(fileId: Int) = filesDao.file(fileId)
}