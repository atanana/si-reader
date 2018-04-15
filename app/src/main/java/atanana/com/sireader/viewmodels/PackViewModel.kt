package atanana.com.sireader.viewmodels

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import atanana.com.sireader.database.PackEntity
import atanana.com.sireader.database.PacksDao
import atanana.com.sireader.database.QuestionEntity
import javax.inject.Inject

class PackViewModel @Inject constructor(
        private val packsDao: PacksDao
) : BaseViewModel() {
    private val packData = MutableLiveData<PackViewState>()

    val pack: LiveData<PackViewState>
        get() = packData

    fun loadPack(packId: Int) {

    }
}

data class PackViewState(val pack: PackEntity, val questions: List<QuestionEntity>)