package atanana.com.sireader.viewmodels

import android.arch.lifecycle.MutableLiveData
import atanana.com.sireader.database.QuestionFileEntity
import atanana.com.sireader.database.QuestionFilesDao
import io.reactivex.android.schedulers.AndroidSchedulers
import javax.inject.Inject

class FilesListViewModel @Inject constructor(filesDao: QuestionFilesDao) : BaseViewModel() {
    private val filesData = MutableLiveData<FilesListViewState>()

    val state: NonNullMediatorLiveData<FilesListViewState> = filesData.nonNull()

    init {
        state.value = Loading
        addDisposable(
                filesDao.all()
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe { entities ->
                            filesData.value = if (entities.isEmpty()) {
                                NoFiles
                            } else {
                                Files(entities)
                            }
                        }
        )
    }

    fun onFileClick(packId: Int) {
        bus.value = OpenFile(packId)
    }
}

sealed class FilesListViewState

object Loading : FilesListViewState()
object NoFiles : FilesListViewState()
data class Files(val files: List<QuestionFileEntity>) : FilesListViewState()