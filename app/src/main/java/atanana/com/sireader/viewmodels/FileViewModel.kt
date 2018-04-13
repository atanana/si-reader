package atanana.com.sireader.viewmodels

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import atanana.com.sireader.database.PackEntity
import atanana.com.sireader.database.PacksDao
import atanana.com.sireader.database.QuestionFileEntity
import atanana.com.sireader.database.QuestionFilesDao
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.BiFunction
import javax.inject.Inject

class FileViewModel @Inject constructor(
        private val filesDao: QuestionFilesDao,
        private val packsDao: PacksDao
) : BaseViewModel() {
    private val fileData = MutableLiveData<FileViewState>()

    val file: LiveData<FileViewState>
        get() = fileData

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
}

data class FileViewState(val file: QuestionFileEntity, val packs: List<PackEntity>)