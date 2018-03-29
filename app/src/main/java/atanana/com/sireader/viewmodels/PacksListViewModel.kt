package atanana.com.sireader.viewmodels

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import atanana.com.sireader.database.QuestionFileEntity
import atanana.com.sireader.database.QuestionFilesDao
import io.reactivex.android.schedulers.AndroidSchedulers
import javax.inject.Inject

class PacksListViewModelFactory @Inject constructor(
        private val filesDao: QuestionFilesDao
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T = when {
        modelClass.isAssignableFrom(PacksListViewModel::class.java) ->
            PacksListViewModel(filesDao) as T
        else -> throw IllegalArgumentException("Unknown ViewModel class")
    }
}

class PacksListViewModel(filesDao: QuestionFilesDao) : BaseViewModel() {
    private val filesData = MutableLiveData<List<QuestionFileEntity>>()

    val files: LiveData<List<QuestionFileEntity>>
        get() = filesData

    init {
        addDisposable(
                filesDao.all()
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe { filesData.value = it }
        )
    }
}