package atanana.com.sireader.screens.packspager

import androidx.lifecycle.viewModelScope
import atanana.com.sireader.database.PackEntity
import atanana.com.sireader.database.PacksDao
import atanana.com.sireader.viewmodels.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PacksPagerViewModel @Inject constructor(
    private val packsDao: PacksDao
) : BaseViewModel() {

    private val _packs = MutableStateFlow<PacksViewState?>(null)
    val packs: Flow<PacksViewState> = _packs.filterNotNull()

    fun loadPacks(fileId: Int, currentPackId: Int) {
        viewModelScope.launch {
            packsDao.packForFile(fileId)
                .map { packs -> PacksViewState(packs, currentPackId) }
                .collect { _packs.value = it }
        }
    }
}

data class PacksViewState(val packs: List<PackEntity>, val currentPackId: Int)