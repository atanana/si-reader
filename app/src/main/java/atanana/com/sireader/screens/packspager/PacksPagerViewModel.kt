package atanana.com.sireader.screens.packspager

import androidx.lifecycle.viewModelScope
import atanana.com.sireader.database.PackEntity
import atanana.com.sireader.database.PacksDao
import atanana.com.sireader.viewmodels.BaseViewModel
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.*

class PacksPagerViewModel @AssistedInject constructor(
    packsDao: PacksDao,
    @Assisted params: Params,
) : BaseViewModel() {

    private val _packs = MutableStateFlow<PacksViewState?>(null)
    val packs: Flow<PacksViewState> = _packs.filterNotNull()

    init {
        packsDao.packForFile(params.fileId)
            .map { packs -> PacksViewState(packs, params.packId) }
            .onEach { _packs.value = it }
            .launchIn(viewModelScope)
    }

    @AssistedFactory
    interface Factory {
        fun create(params: Params): PacksPagerViewModel
    }

    data class Params(val fileId: Int, val packId: Int)
}

data class PacksViewState(val packs: List<PackEntity>, val currentPackId: Int)