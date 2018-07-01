package atanana.com.sireader.usecases

import atanana.com.sireader.database.PackEntity
import atanana.com.sireader.database.PacksDao
import atanana.com.sireader.database.QuestionFileEntity
import atanana.com.sireader.database.QuestionFilesDao
import atanana.com.sireader.screens.fileinfo.PackItem
import io.reactivex.Flowable
import io.reactivex.functions.BiFunction
import javax.inject.Inject

class GetFileInfoWithPacks @Inject constructor(
        private val filesDao: QuestionFilesDao,
        private val packsDao: PacksDao
) {
    fun getInfo(fileId: Int): Flowable<Pair<QuestionFileEntity, List<PackItem>>> =
            file(fileId).zipWith(packs(fileId), zipFileWithPacks())

    private fun zipFileWithPacks(): BiFunction<QuestionFileEntity, List<PackEntity>, Pair<QuestionFileEntity, List<PackItem>>> {
        return BiFunction { file, packs ->
            Pair(file, packs.map { PackItem(it, true) })
        }
    }

    private fun packs(fileId: Int) = packsDao.packForFile(fileId)

    private fun file(fileId: Int) = filesDao.file(fileId)
}