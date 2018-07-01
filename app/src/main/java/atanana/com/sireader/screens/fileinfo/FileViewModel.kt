package atanana.com.sireader.screens.fileinfo

import android.arch.lifecycle.MutableLiveData
import atanana.com.sireader.database.PackEntity
import atanana.com.sireader.database.QuestionFileEntity
import atanana.com.sireader.usecases.GetFileInfoWithPacks
import atanana.com.sireader.viewmodels.BaseViewModel
import atanana.com.sireader.viewmodels.NonNullMediatorLiveData
import atanana.com.sireader.viewmodels.OpenPackMessage
import atanana.com.sireader.viewmodels.nonNull
import io.reactivex.android.schedulers.AndroidSchedulers
import javax.inject.Inject

class FileViewModel @Inject constructor(
        private val provider: GetFileInfoWithPacks
) : BaseViewModel() {
    private val fileData = MutableLiveData<FileViewState>()

    val file: NonNullMediatorLiveData<FileViewState> = fileData.nonNull()

    fun loadFileInfo(fileId: Int) {
        addDisposable(
                provider.getInfo(fileId)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe { (fileInfo, packs) ->
                            fileData.value = FileViewState(fileInfo, packs)
                        }
        )
    }

    fun onPackClick(packId: Int) {
        bus.value = OpenPackMessage(packId)
    }
}

data class FileViewState(val file: QuestionFileEntity, val packs: List<PackItem>)

data class PackItem(val pack: PackEntity, val lastRead: Boolean)