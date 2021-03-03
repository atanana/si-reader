package atanana.com.sireader.screens.fileslist

import android.Manifest
import android.app.Activity
import android.content.res.Resources
import android.net.Uri
import android.view.Menu
import android.view.MenuItem
import android.webkit.MimeTypeMap
import androidx.activity.result.ActivityResultLauncher
import androidx.appcompat.view.ActionMode
import androidx.core.app.ActivityCompat
import androidx.lifecycle.viewModelScope
import atanana.com.sireader.CannotSaveInDatabaseException
import atanana.com.sireader.ParseFileException
import atanana.com.sireader.R
import atanana.com.sireader.usecases.DeleteFiles
import atanana.com.sireader.usecases.GetFilesItems
import atanana.com.sireader.usecases.ParseFile
import atanana.com.sireader.utils.checkPermission
import atanana.com.sireader.viewmodels.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FilesListViewModel @Inject constructor(
    private val resources: Resources,
    private val parseFileUseCase: ParseFile,
    private val deleteFilesUseCase: DeleteFiles,
    private val selectionManager: FilesSelectionManager,
    getFilesItems: GetFilesItems
) : BaseViewModel() {

    companion object {
        private val FILE_TYPES = listOf("doc", "txt")
            .mapNotNull { MimeTypeMap.getSingleton().getMimeTypeFromExtension(it) }
    }

    private val _files = MutableStateFlow<FilesListViewState>(Loading)
    val state: StateFlow<FilesListViewState> = _files

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
        viewModelScope.launch {
            getFilesItems.getFiles().collect { files ->
                _files.value = if (files.isEmpty()) {
                    NoFiles
                } else {
                    Files(selectionManager.mapItems(files))
                }
            }
        }

        viewModelScope.launch {
            selectionManager.selectionMode.collect { isSelection ->
                val action = if (isSelection) {
                    StartActionModeMessage(callback)
                } else {
                    StopActionModeMessage
                }
                sendAction(action)
            }
        }
    }

    private fun updateSelectionTitle() {
        val selectedFilesCount = selectionManager.selectedFiles.size
        val title = resources.getQuantityString(R.plurals.files_selected, selectedFilesCount, selectedFilesCount)
        sendAction(StringActionModeTitleMessage(title))
    }

    fun onFileClick(fileId: Int) {
        if (selectionManager.isSelectionMode) {
            toggleFileSelection(fileId)
        } else {
            sendAction(OpenFileMessage(fileId))
        }
    }

    private fun toggleFileSelection(fileId: Int) {
        selectionManager.toggleFileSelection(fileId)
        updateFilesSelection()
        updateSelectionTitle()
    }

    private fun updateFilesSelection() {
        val files = (_files.value as? Files)?.files ?: return
        _files.value = Files(selectionManager.mapItems(files))
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
                openFileSelector()
            }
            ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.READ_EXTERNAL_STORAGE) -> {
                sendAction(ReadStoragePermissionExplanation)
            }
            else -> {
                requestReadStorage(request)
            }
        }
    }

    fun requestReadStorage(request: ActivityResultLauncher<String>) {
        request.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
    }

    fun onPermissionResult(isGranted: Boolean) {
        if (isGranted) {
            openFileSelector()
        } else {
            sendAction(ResourceToastMessage(R.string.no_permissions_to_read_files))
        }
    }

    private fun openFileSelector() {
        sendAction(OpenFilePicker(FILE_TYPES))
    }

    fun processFile(uri: Uri?) {
        uri ?: return
        val oldState = _files.value
        _files.value = Loading
        viewModelScope.launch {
            try {
                parseFileUseCase.process(uri)
            } catch (e: Exception) {
                _files.value = oldState
                val action = getParsingErrorMessage(e)
                sendAction(action)
            }
        }
    }

    private fun onDeleteClicked() {
        val fileIds = selectionManager.selectedFiles.toIntArray()
        viewModelScope.launch {
            deleteFilesUseCase.process(fileIds)
            sendAction(ResourceToastMessage(R.string.files_deleted))
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