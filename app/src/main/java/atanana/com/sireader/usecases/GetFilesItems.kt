package atanana.com.sireader.usecases

import atanana.com.sireader.database.QuestionFilesDao
import atanana.com.sireader.preferences.Prefs
import atanana.com.sireader.screens.fileslist.FileItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetFilesItems @Inject() constructor(
    private val filesDao: QuestionFilesDao,
    private val prefs: Prefs
) {
    fun getFiles(): Flow<List<FileItem>> =
        filesDao.all().map { entities ->
            val lastReadFile = prefs.lastReadFile
            entities.map { FileItem(it, false, it.id == lastReadFile) }
        }
}