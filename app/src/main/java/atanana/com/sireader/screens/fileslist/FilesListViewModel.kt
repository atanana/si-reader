package atanana.com.sireader.screens.fileslist

import android.Manifest
import android.app.Activity
import android.arch.lifecycle.MutableLiveData
import android.content.Intent
import android.content.res.Resources
import android.support.v7.view.ActionMode
import android.view.Menu
import android.view.MenuItem
import atanana.com.sireader.CannotSaveInDatabaseException
import atanana.com.sireader.ParseFileException
import atanana.com.sireader.R
import atanana.com.sireader.database.QuestionFilesDao
import atanana.com.sireader.files.OPEN_FILE_REQUEST_CODE
import atanana.com.sireader.files.OpenFileHandler
import atanana.com.sireader.usecases.GetFilesItems
import atanana.com.sireader.usecases.ParseFileUseCase
import atanana.com.sireader.viewmodels.*
import com.tbruyelle.rxpermissions2.RxPermissions
import io.reactivex.Completable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class FilesListViewModel @Inject constructor(
        private val resources: Resources,
        private val filesDao: QuestionFilesDao,
        private val openFileHandler: OpenFileHandler,
        private val parseFileUseCase: ParseFileUseCase,
        private val selectionManager: FilesSelectionManager,
        getFilesItems: GetFilesItems
) : BaseViewModel() {
    private val filesData = MutableLiveData<FilesListViewState>()

    val state: NonNullMediatorLiveData<FilesListViewState> = filesData.nonNull()

    private val callback = object : ActionMode.Callback {
        override fun onActionItemClicked(mode: ActionMode?, item: MenuItem?): Boolean {
            when (item?.itemId) {
                R.id.action_delete -> onDeleteClicked()
            }
            return true
        }

        override fun onCreateActionMode(mode: ActionMode?, menu: Menu?): Boolean {
            mode?.menuInflater?.inflate(R.menu.menu_files, menu)
            return true
        }

        override fun onPrepareActionMode(mode: ActionMode?, menu: Menu?): Boolean {
            return false
        }

        override fun onDestroyActionMode(mode: ActionMode?) {
            selectionManager.isSelectionMode = false
            updateFilesSelection()
        }
    }

    init {
        state.value = Loading

        addDisposable(
                getFilesItems.getFiles()
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe { files ->
                            filesData.value = if (files.isEmpty()) {
                                NoFiles
                            } else {
                                Files(selectionManager.mapItems(files))
                            }
                        }
        )

        addDisposable(
                selectionManager.selectionModeObservable
                        .subscribe { isSelection ->
                            bus.value = if (isSelection) {
                                StartActionModeMessage(callback)
                            } else {
                                StopActionModeMessage
                            }
                        }
        )
    }

    private fun updateSelectionTitle() {
        val selectedFilesCount = selectionManager.selectedFiles.size
        val title = resources.getQuantityString(R.plurals.files_selected, selectedFilesCount, selectedFilesCount)
        bus.value = StringActionModeTitleMessage(title)
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
        updateFilesSelection()
        updateSelectionTitle()
    }

    private fun updateFilesSelection() {
        (filesData.value as? Files)?.files?.let { files ->
            filesData.value = Files(selectionManager.mapItems(files))
        }
    }

    fun onLongFileClick(fileId: Int) {
        if (!selectionManager.isSelectionMode) {
            selectionManager.isSelectionMode = true
            onFileClick(fileId)
        }
    }

    fun fabClicked(rxPermissions: RxPermissions) {
        addDisposable(
                rxPermissions
                        .request(Manifest.permission.READ_EXTERNAL_STORAGE)
                        .subscribe { granted ->
                            if (granted) {
                                tryOpenFileSelector()
                            } else {
                                bus.value = ResourceToastMessage(R.string.no_permissions_to_read_files)
                            }
                        }
        )
    }

    private fun tryOpenFileSelector() {
        val intent = openFileHandler.openFileIntent()
        bus.value = if (intent != null) {
            ActivityForResultMessage(intent, OPEN_FILE_REQUEST_CODE)
        } else {
            ResourceToastMessage(R.string.no_file_managers_installed)
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

    private fun onDeleteClicked() {
        val fileIds = selectionManager.selectedFiles.toIntArray()
        addDisposable(
                Completable.fromAction { filesDao.deleteFilesByIds(fileIds) }
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe { bus.value = ResourceToastMessage(R.string.files_deleted) }
        )
        selectionManager.isSelectionMode = false
    }

    private fun getParsingErrorMessage(error: Throwable?): Action? =
            when (error) {
                is ParseFileException -> ResourceToastMessage(R.string.cannot_parse_file)
                is CannotSaveInDatabaseException -> ResourceToastMessage(R.string.cannot_save_questions)
                else -> ResourceToastMessage(R.string.unknown_error)
            }
}

sealed class FilesListViewState

object Loading : FilesListViewState()
object NoFiles : FilesListViewState()
data class Files(val files: List<FileItem>) : FilesListViewState()