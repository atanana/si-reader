package atanana.com.sireader.screens.fileinfo

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import atanana.com.sireader.database.PackEntity
import atanana.com.sireader.database.QuestionFileEntity
import atanana.com.sireader.usecases.GetFileInfoWithPacks
import atanana.com.sireader.viewmodels.BaseViewModel
import atanana.com.sireader.viewmodels.NonNullMediatorLiveData
import atanana.com.sireader.viewmodels.OpenPackMessage
import atanana.com.sireader.viewmodels.nonNull
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

class FileViewModel @Inject constructor(
        private val provider: GetFileInfoWithPacks
) : BaseViewModel() {
    private val fileData = MutableLiveData<FileViewState>()

    val file: NonNullMediatorLiveData<FileViewState> = fileData.nonNull()

    fun loadFileInfo(fileId: Int) {
        viewModelScope.launch {
            provider.getInfo(fileId)
                .collect { (fileInfo, packs) ->
                    fileData.value = FileViewState(fileInfo, packs)
                }
        }
    }

    fun onPackClick(packId: Int) {
        bus.value = OpenPackMessage(packId)
    }
}

data class FileViewState(val file: QuestionFileEntity, val packs: List<PackItem>)

data class PackItem(val pack: PackEntity, val lastRead: Boolean)