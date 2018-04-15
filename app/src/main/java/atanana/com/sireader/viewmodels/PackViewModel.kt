package atanana.com.sireader.viewmodels

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import atanana.com.sireader.database.PackEntity
import atanana.com.sireader.database.PacksDao
import atanana.com.sireader.database.QuestionEntity
import atanana.com.sireader.database.QuestionsDao
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.BiFunction
import javax.inject.Inject

class PackViewModel @Inject constructor(
        private val packsDao: PacksDao,
        private val questionsDao: QuestionsDao
) : BaseViewModel() {
    private val packData = MutableLiveData<PackViewState>()

    val pack: LiveData<PackViewState>
        get() = packData

    fun loadPack(packId: Int) {
        addDisposable(
                packsDao.pack(packId)
                        .zipWith<List<QuestionEntity>, PackViewState>(
                                questionsDao.questionsForPack(packId),
                                BiFunction { pack, questions -> PackViewState(pack, questions) }
                        )
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe { packData.value = it }
        )
    }
}

data class PackViewState(val pack: PackEntity, val questions: List<QuestionEntity>)