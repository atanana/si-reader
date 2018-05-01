package atanana.com.sireader.viewmodels

import android.arch.lifecycle.MutableLiveData
import atanana.com.sireader.database.PackEntity
import atanana.com.sireader.database.PacksDao
import io.reactivex.android.schedulers.AndroidSchedulers
import javax.inject.Inject

class PacksPagerViewModel @Inject constructor(
        private val packsDao: PacksDao
) : BaseViewModel() {
    private val packsData = MutableLiveData<PacksViewState>()

    val packs: NonNullMediatorLiveData<PacksViewState> = packsData.nonNull()

    fun loadPacks(fileId: Int, currentPackId: Int) {
        addDisposable(
                packsDao.packForFile(fileId)
                        .map { packs -> PacksViewState(packs, currentPackId) }
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe { packsData.value = it }
        )
    }
}

data class PacksViewState(val packs: List<PackEntity>, val currentPackId: Int)