package atanana.com.sireader.usecases

import atanana.com.sireader.database.PackEntity
import atanana.com.sireader.database.QuestionFilesDao
import atanana.com.sireader.preferences.Prefs
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class UpdateLastRead @Inject constructor(
        private val filesDao: QuestionFilesDao,
        private val prefs: Prefs
) {
    suspend fun update(pack: PackEntity) {
        withContext(Dispatchers.IO) {
            filesDao.updateLastReadPack(pack.fileId, pack.id)
            prefs.lastReadFile = pack.fileId
        }
    }
}