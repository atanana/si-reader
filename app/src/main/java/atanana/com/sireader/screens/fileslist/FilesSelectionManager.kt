package atanana.com.sireader.screens.fileslist

import atanana.com.sireader.database.QuestionFileEntity
import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject
import javax.inject.Inject

class FilesSelectionManager @Inject constructor() {
    private val files = mutableSetOf<Int>()

    val selectedFiles: Set<Int>
        get() = files

    private val selectionModeSubject = BehaviorSubject.create<Boolean>()

    val selectionModeObservable: Observable<Boolean>
        get() = selectionModeSubject

    var isSelectionMode = false
        set(value) {
            field = value
            if (!value) {
                files.clear()
            }
            selectionModeSubject.onNext(value)
        }

    fun mapFiles(entities: List<QuestionFileEntity>): List<FileItem> =
            entities.map { entity -> FileItem(entity, isFileSelected(entity)) }

    fun mapItems(entities: List<FileItem>): List<FileItem> =
            entities.map { item -> FileItem(item.entity, isFileSelected(item.entity)) }

    private fun isFileSelected(entity: QuestionFileEntity) =
            isSelectionMode && files.contains(entity.id)

    fun toggleFileSelection(fileId: Int) {
        if (files.contains(fileId)) {
            files.remove(fileId)
        } else {
            files.add(fileId)
        }
    }
}