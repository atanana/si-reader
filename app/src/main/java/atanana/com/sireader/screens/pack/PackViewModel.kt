package atanana.com.sireader.screens.pack

import android.arch.lifecycle.MutableLiveData
import atanana.com.sireader.database.PackEntity
import atanana.com.sireader.database.PacksDao
import atanana.com.sireader.database.QuestionEntity
import atanana.com.sireader.database.QuestionsDao
import atanana.com.sireader.viewmodels.BaseViewModel
import atanana.com.sireader.viewmodels.NonNullMediatorLiveData
import atanana.com.sireader.viewmodels.nonNull
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.BiFunction
import javax.inject.Inject

class PackViewModel @Inject constructor(
        private val packsDao: PacksDao,
        private val questionsDao: QuestionsDao
) : BaseViewModel() {
    private val packData = MutableLiveData<PackViewState>()

    val pack: NonNullMediatorLiveData<PackViewState> = packData.nonNull()

    fun loadPack(packId: Int) {
        addDisposable(
                packsDao.pack(packId)
                        .zipWith<List<QuestionEntity>, PackViewState>(
                                questionsDao.questionsForPack(packId),
                                BiFunction { pack, questions ->
                                    val questionViewModels = questions.map { QuestionViewModel(it, true) }
                                    PackViewState(pack, questionViewModels)
                                }
                        )
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe { packData.value = it }
        )
    }

    fun onQuestionClick(questionId: Int) {
        val currentState = packData.value!!
        val newQuestions = currentState.questions.map {
            if (it.question.id == questionId) {
                it.copy(isClosed = false)
            } else {
                it
            }
        }
        packData.value = currentState.copy(questions = newQuestions)
    }
}

data class QuestionViewModel(val question: QuestionEntity, val isClosed: Boolean)

data class PackViewState(val pack: PackEntity, val questions: List<QuestionViewModel>)