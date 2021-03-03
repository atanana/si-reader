package atanana.com.sireader.usecases

import atanana.com.sireader.database.QuestionFilesDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class DeleteFiles @Inject constructor(private val filesDao: QuestionFilesDao) {

    suspend fun process(fileIds: IntArray) {
        withContext(Dispatchers.IO) {
            filesDao.deleteFilesByIds(fileIds)
        }
    }
}