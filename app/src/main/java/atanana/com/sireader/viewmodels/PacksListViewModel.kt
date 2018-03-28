package atanana.com.sireader.viewmodels

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import atanana.com.sireader.database.QuestionFileEntity
import atanana.com.sireader.database.QuestionFilesDao

class PacksListViewModel(private val filesDao: QuestionFilesDao) : ViewModel() {
    private val filesData = MutableLiveData<List<QuestionFileEntity>>()

    val files: LiveData<List<QuestionFileEntity>>
        get() = filesData
}