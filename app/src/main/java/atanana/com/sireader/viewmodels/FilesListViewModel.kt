package atanana.com.sireader.viewmodels

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import atanana.com.sireader.database.QuestionFileEntity
import atanana.com.sireader.database.QuestionFilesDao
import io.reactivex.android.schedulers.AndroidSchedulers
import javax.inject.Inject

class FilesListViewModel @Inject constructor(filesDao: QuestionFilesDao) : BaseViewModel() {
    private val filesData = MutableLiveData<FilesListViewState>()

    val files: LiveData<FilesListViewState>
        get() = filesData

    init {
        addDisposable(
                filesDao.all()
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe { entities ->
                            filesData.value = FilesListViewState(
                                    entities.isNotEmpty(),
                                    entities.isEmpty(),
                                    entities
                            )
                        }
        )
    }

    fun onFileClick(packId: Int) {
        bus.value = OpenFile(packId)
    }
}

data class FilesListViewState(
        val noFilesLabelGone: Boolean,
        val filesListGone: Boolean,
        val files: List<QuestionFileEntity>
)