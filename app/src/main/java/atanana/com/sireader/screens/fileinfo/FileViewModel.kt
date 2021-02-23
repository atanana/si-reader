package atanana.com.sireader.screens.fileinfo

import androidx.lifecycle.viewModelScope
import atanana.com.sireader.database.PackEntity
import atanana.com.sireader.database.QuestionFileEntity
import atanana.com.sireader.usecases.GetFileInfoWithPacks
import atanana.com.sireader.viewmodels.BaseViewModel
import atanana.com.sireader.viewmodels.OpenPackMessage
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.*

class FileViewModel @AssistedInject constructor(
    provider: GetFileInfoWithPacks,
    @Assisted fileId: Int
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

    @AssistedFactory
    interface Factory {
        fun create(fileId: Int): FileViewModel
    }
}

data class FileViewState(val file: QuestionFileEntity, val packs: List<PackItem>)

data class PackItem(val pack: PackEntity, val lastRead: Boolean)