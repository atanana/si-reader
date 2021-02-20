package atanana.com.sireader.screens.fileslist

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.res.Resources
import android.view.Menu
import android.view.MenuItem
import androidx.activity.result.ActivityResultLauncher
import androidx.appcompat.view.ActionMode
import androidx.core.app.ActivityCompat
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import atanana.com.sireader.CannotSaveInDatabaseException
import atanana.com.sireader.ParseFileException
import atanana.com.sireader.R
import atanana.com.sireader.database.QuestionFilesDao
import atanana.com.sireader.files.OPEN_FILE_REQUEST_CODE
import atanana.com.sireader.files.OpenFileHandler
import atanana.com.sireader.usecases.GetFilesItems
import atanana.com.sireader.usecases.ParseFileUseCase
import atanana.com.sireader.utils.checkPermission
import atanana.com.sireader.viewmodels.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
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

        viewModelScope.launch {
            getFilesItems.getFiles().collect { files ->
                filesData.value = if (files.isEmpty()) {
                    NoFiles
                } else {
                    Files(selectionManager.mapItems(files))
                }
            }
        }

        viewModelScope.launch {
            selectionManager.selectionMode.collect { isSelection ->
                bus.value = if (isSelection) {
                    StartActionModeMessage(callback)
                } else {
                    StopActionModeMessage
                }
            }
        }
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

    fun fabClicked(activity: Activity, request: ActivityResultLauncher<String>) {
        when {
            activity.checkPermission(Manifest.permission.READ_EXTERNAL_STORAGE) -> {
                tryOpenFileSelector()
            }
            ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.READ_EXTERNAL_STORAGE) -> {
                bus.value = ResourceToastMessage(R.string.unknown_error)//todo
            }
            else -> {
                request.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
            }
        }
    }

    fun onPermissionResult(isGranted: Boolean) {
        if (isGranted) {
            tryOpenFileSelector()
        } else {
            bus.value = ResourceToastMessage(R.string.no_permissions_to_read_files)
        }
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
        if (requestCode == OPEN_FILE_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            val uri = data?.data ?: return
            val oldState = filesData.value
            filesData.value = Loading
            viewModelScope.launch {
                try {
                    parseFileUseCase.process(uri)
                } catch (e: Exception) {
                    filesData.value = oldState!!
                    bus.value = getParsingErrorMessage(e)
                }
            }
        }
    }

    private fun onDeleteClicked() {
        val fileIds = selectionManager.selectedFiles.toIntArray()
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                filesDao.deleteFilesByIds(fileIds)
            }
            bus.value = ResourceToastMessage(R.string.files_deleted)
        }
        selectionManager.isSelectionMode = false
    }

    private fun getParsingErrorMessage(error: Throwable?): Action =
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