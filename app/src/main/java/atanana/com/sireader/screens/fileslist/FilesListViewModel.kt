package atanana.com.sireader.screens.fileslist

import android.Manifest
import android.app.Activity
import android.arch.lifecycle.MutableLiveData
import android.content.Intent
import android.content.res.Resources
import atanana.com.sireader.CannotSaveInDatabaseException
import atanana.com.sireader.ParseFileException
import atanana.com.sireader.R
import atanana.com.sireader.database.QuestionFilesDao
import atanana.com.sireader.files.OPEN_FILE_REQUEST_CODE
import atanana.com.sireader.files.OpenFileHandler
import atanana.com.sireader.usecases.ParseFileUseCase
import atanana.com.sireader.viewmodels.*
import com.tbruyelle.rxpermissions2.RxPermissions
import io.reactivex.android.schedulers.AndroidSchedulers
import javax.inject.Inject

class FilesListViewModel @Inject constructor(
        private val resources: Resources,
        filesDao: QuestionFilesDao,
        private val openFileHandler: OpenFileHandler,
        private val parseFileUseCase: ParseFileUseCase,
        private val selectionManager: FilesSelectionManager
) : BaseViewModel() {
    private val filesData = MutableLiveData<FilesListViewState>()

    val state: NonNullMediatorLiveData<FilesListViewState> = filesData.nonNull()

    init {
        state.value = Loading

        addDisposable(
                filesDao.all()
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe { entities ->
                            filesData.value = if (entities.isEmpty()) {
                                NoFiles
                            } else {
                                Files(selectionManager.mapFiles(entities))
                            }
                        }
        )

        addDisposable(
                selectionManager.selectionModeObservable
                        .subscribe { isSelection ->
                            if (isSelection) {
                                updateSelectionTitle()
                            } else {
                                bus.value = ResourceTitleMessage(R.string.app_name)
                            }
                        }
        )
    }

    private fun updateSelectionTitle() {
        val selectedFilesCount = selectionManager.selectedFiles.size
        val title = resources.getQuantityString(R.plurals.files_selected, selectedFilesCount, selectedFilesCount)
        bus.value = StringTitleMessage(title)
    }

    fun onFileClick(fileId: Int) {
        if (selectionManager.isSelectionMode) {
            toggleFileSelection(fileId)
        } else {
            bus.value = OpenFileMessage(fileId)
        }
    }

    private fun toggleFileSelection(fileId: Int) {
        selectionManager.toggleFileSelection(fileId)
        (filesData.value as? Files)?.files?.let { files ->
            filesData.value = Files(selectionManager.mapItems(files))
        }
        updateSelectionTitle()
    }

    fun onLongFileClick(fileId: Int) {
        if (!selectionManager.isSelectionMode) {
            selectionManager.isSelectionMode = true
            onFileClick(fileId)
            bus.value = SelectionModeChangeMessage(true)
        }
    }

    fun fabClicked(rxPermissions: RxPermissions) {
        rxPermissions
                .request(Manifest.permission.READ_EXTERNAL_STORAGE)
                .subscribe { granted ->
                    if (granted) {
                        tryOpenFileSelector()
                    } else {
                        bus.value = ResourceTextMessage(R.string.no_permissions_to_read_files)
                    }
                }
    }

    private fun tryOpenFileSelector() {
        val intent = openFileHandler.openFileIntent()
        bus.value = if (intent != null) {
            ActivityForResultMessage(intent, OPEN_FILE_REQUEST_CODE)
        } else {
            ResourceTextMessage(R.string.no_file_managers_installed)
        }
    }

    fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == OPEN_FILE_REQUEST_CODE && resultCode == Activity.RESULT_OK && data != null) {
            val uri = data.data
            val oldState = filesData.value
            filesData.value = Loading
            addDisposable(
                    parseFileUseCase.process(uri)
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe({}, { error ->
                                filesData.value = oldState
                                bus.value = getParsingErrorMessage(error)
                            })
            )
        }
    }

    private fun getParsingErrorMessage(error: Throwable?): Action? =
            when (error) {
                is ParseFileException -> ResourceTextMessage(R.string.cannot_parse_file)
                is CannotSaveInDatabaseException -> ResourceTextMessage(R.string.cannot_save_questions)
                else -> ResourceTextMessage(R.string.unknown_error)
            }
}

sealed class FilesListViewState

object Loading : FilesListViewState()
object NoFiles : FilesListViewState()
data class Files(val files: List<FileItem>) : FilesListViewState()