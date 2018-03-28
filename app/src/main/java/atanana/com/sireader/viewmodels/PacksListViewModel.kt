package atanana.com.sireader.viewmodels

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import atanana.com.sireader.database.QuestionFileEntity
import atanana.com.sireader.database.QuestionFilesDao
import io.reactivex.android.schedulers.AndroidSchedulers

class PacksListViewModel(private val filesDao: QuestionFilesDao) : ViewModel() {
    private val filesData = MutableLiveData<List<QuestionFileEntity>>()

    val files: LiveData<List<QuestionFileEntity>>
        get() = filesData

    init {
        filesDao.all()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { filesData.value = it }
    }
}