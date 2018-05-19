package atanana.com.sireader.screens.fileinfo

import android.arch.lifecycle.MutableLiveData
import atanana.com.sireader.database.PackEntity
import atanana.com.sireader.database.PacksDao
import atanana.com.sireader.database.QuestionFileEntity
import atanana.com.sireader.database.QuestionFilesDao
import atanana.com.sireader.viewmodels.BaseViewModel
import atanana.com.sireader.viewmodels.NonNullMediatorLiveData
import atanana.com.sireader.viewmodels.OpenPackMessage
import atanana.com.sireader.viewmodels.nonNull
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.BiFunction
import javax.inject.Inject

class FileViewModel @Inject constructor(
        private val filesDao: QuestionFilesDao,
        private val packsDao: PacksDao
) : BaseViewModel() {
    private val fileData = MutableLiveData<FileViewState>()

    val file: NonNullMediatorLiveData<FileViewState> = fileData.nonNull()

    fun loadFileInfo(fileId: Int) {
        addDisposable(
                filesDao.file(fileId)
                        .zipWith<List<PackEntity>, FileViewState>(
                                packsDao.packForFile(fileId),
                                BiFunction { file, packs -> FileViewState(file, packs) }
                        )
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe { fileData.value = it }
        )
    }

    fun onPackClick(packId: Int) {
        bus.value = OpenPackMessage(packId)
    }
}

data class FileViewState(val file: QuestionFileEntity, val packs: List<PackEntity>)