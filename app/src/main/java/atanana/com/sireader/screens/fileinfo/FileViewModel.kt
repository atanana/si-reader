package atanana.com.sireader.screens.fileinfo

import androidx.lifecycle.viewModelScope
import atanana.com.sireader.database.PackEntity
import atanana.com.sireader.database.QuestionFileEntity
import atanana.com.sireader.usecases.GetFileInfoWithPacks
import atanana.com.sireader.viewmodels.BaseViewModel
import atanana.com.sireader.viewmodels.OpenPackMessage
import kotlinx.coroutines.flow.*
import javax.inject.Inject

class FileViewModel(
    provider: GetFileInfoWithPacks,
    fileId: Int
) : BaseViewModel() {

    private val _file = MutableStateFlow<FileViewState?>(null)
    val file: Flow<FileViewState> = _file.filterNotNull()

    init {
        provider.getInfo(fileId)
            .onEach { (fileInfo, packs) -> _file.value = FileViewState(fileInfo, packs) }
            .launchIn(viewModelScope)
    }

    fun onPackClick(packId: Int) {
        sendAction(OpenPackMessage(packId))
    }

    class Factory @Inject constructor(private val provider: GetFileInfoWithPacks) {
        fun create(fileId: Int) = FileViewModel(provider, fileId)
    }
}

data class FileViewState(val file: QuestionFileEntity, val packs: List<PackItem>)

data class PackItem(val pack: PackEntity, val lastRead: Boolean)