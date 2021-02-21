package atanana.com.sireader.screens.packspager

import androidx.lifecycle.viewModelScope
import atanana.com.sireader.database.PackEntity
import atanana.com.sireader.database.PacksDao
import atanana.com.sireader.viewmodels.BaseViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

class PacksPagerViewModel @Inject constructor(
        private val packsDao: PacksDao
) : BaseViewModel() {

    private val _packs = MutableStateFlow<PacksViewState?>(null)
    val packs: StateFlow<PacksViewState?> = _packs

    fun loadPacks(fileId: Int, currentPackId: Int) {
        viewModelScope.launch {
            packsDao.packForFile(fileId)
                .map { packs -> PacksViewState(packs, currentPackId) }
                .collect { _packs.value = it }
        }
    }
}

data class PacksViewState(val packs: List<PackEntity>, val currentPackId: Int)