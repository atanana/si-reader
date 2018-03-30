package atanana.com.sireader.viewmodels

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import atanana.com.sireader.database.PackEntity
import atanana.com.sireader.database.PacksDao
import atanana.com.sireader.database.QuestionFileEntity
import atanana.com.sireader.database.QuestionFilesDao
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.BiFunction
import javax.inject.Inject

class FileViewModelFactory @Inject constructor(
        private val filesDao: QuestionFilesDao,
        private val packsDao: PacksDao
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T = when {
        modelClass.isAssignableFrom(FileViewModel::class.java) ->
            FileViewModel(filesDao, packsDao) as T
        else -> throw IllegalArgumentException("Unknown ViewModel class")
    }
}

class FileViewModel(
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