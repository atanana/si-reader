package atanana.com.sireader.usecases

import atanana.com.sireader.database.PackEntity
import atanana.com.sireader.database.QuestionFilesDao
import atanana.com.sireader.doIo
import atanana.com.sireader.preferences.Prefs
import javax.inject.Inject

class UpdateLastRead @Inject constructor(
        private val filesDao: QuestionFilesDao,
        private val prefs: Prefs
) {
    fun update(pack: PackEntity) {
        doIo {
            filesDao.updateLastReadPack(pack.fileId, pack.id)
            prefs.lastReadFile = pack.fileId
        }
    }
}