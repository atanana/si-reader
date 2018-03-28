package atanana.com.sireader.viewmodels

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import atanana.com.sireader.database.QuestionFilesDao
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