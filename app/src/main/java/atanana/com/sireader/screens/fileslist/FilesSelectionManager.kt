package atanana.com.sireader.screens.fileslist

import atanana.com.sireader.database.QuestionFileEntity
import javax.inject.Inject

class FilesSelectionManager @Inject constructor() {
    private val files = mutableSetOf<Int>()

    val selectedFiles: Set<Int>
        get() = files

    var isSelectionMode = false
        set(value) {
            field = value
            if (!value) {
                files.clear()
            }
        }

    fun mapFiles(entities: List<QuestionFileEntity>): List<FileItem> =
            entities.map { entity -> FileItem(entity, isFileSelected(entity)) }

    private fun isFileSelected(entity: QuestionFileEntity) =
            isSelectionMode && files.contains(entity.id)

    fun selectFile(fileId: Int) = files.add(fileId)
}