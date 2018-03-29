package atanana.com.sireader.viewmodels

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import atanana.com.sireader.database.QuestionFileEntity
import atanana.com.sireader.database.QuestionFilesDao
import io.reactivex.android.schedulers.AndroidSchedulers

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