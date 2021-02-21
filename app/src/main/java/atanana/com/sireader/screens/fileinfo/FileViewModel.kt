package atanana.com.sireader.screens.fileinfo

import androidx.lifecycle.viewModelScope
import atanana.com.sireader.database.PackEntity
import atanana.com.sireader.database.QuestionFileEntity
import atanana.com.sireader.usecases.GetFileInfoWithPacks
import atanana.com.sireader.viewmodels.BaseViewModel
import atanana.com.sireader.viewmodels.OpenPackMessage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

class FileViewModel @Inject constructor(
    private val provider: GetFileInfoWithPacks
) : BaseViewModel() {

    private val _file = MutableStateFlow<FileViewState?>(null)
    val file: StateFlow<FileViewState?> = _file

    fun loadFileInfo(fileId: Int) {
        if (_file.value?.file?.id != fileId) {
            provider.getInfo(fileId)
                .onEach { (fileInfo, packs) -> _file.value = FileViewState(fileInfo, packs) }
                .launchIn(viewModelScope)
        }
    }

    fun onPackClick(packId: Int) {
        bus.value = OpenPackMessage(packId)
    }
}

data class FileViewState(val file: QuestionFileEntity, val packs: List<PackItem>)

data class PackItem(val pack: PackEntity, val lastRead: Boolean)