package atanana.com.sireader.screens.packspager

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import atanana.com.sireader.database.PackEntity
import atanana.com.sireader.database.PacksDao
import atanana.com.sireader.viewmodels.BaseViewModel
import atanana.com.sireader.viewmodels.NonNullMediatorLiveData
import atanana.com.sireader.viewmodels.nonNull
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

class PacksPagerViewModel @Inject constructor(
        private val packsDao: PacksDao
) : BaseViewModel() {
    private val packsData = MutableLiveData<PacksViewState>()

    val packs: NonNullMediatorLiveData<PacksViewState> = packsData.nonNull()

    fun loadPacks(fileId: Int, currentPackId: Int) {
        viewModelScope.launch {
            packsDao.packForFile(fileId)
                .map { packs -> PacksViewState(packs, currentPackId) }
                .collect { packsData.value = it }
        }
    }
}

data class PacksViewState(val packs: List<PackEntity>, val currentPackId: Int)