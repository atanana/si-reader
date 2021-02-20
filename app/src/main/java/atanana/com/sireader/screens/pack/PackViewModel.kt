package atanana.com.sireader.screens.pack

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import atanana.com.sireader.database.PackEntity
import atanana.com.sireader.database.QuestionEntity
import atanana.com.sireader.usecases.GetPackWithQuestions
import atanana.com.sireader.usecases.UpdateLastRead
import atanana.com.sireader.viewmodels.BaseViewModel
import atanana.com.sireader.viewmodels.NonNullMediatorLiveData
import atanana.com.sireader.viewmodels.nonNull
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

class PackViewModel @Inject constructor(
    private val provider: GetPackWithQuestions,
    private val updateLastRead: UpdateLastRead
) : BaseViewModel() {
    private val packData = MutableLiveData<PackViewState>()

    val pack: NonNullMediatorLiveData<PackViewState> = packData.nonNull()

    fun loadPack(packId: Int) {
        if (packData.value == null) {
            viewModelScope.launch {
                provider.getPack(packId).collect { (pack, questions) ->
                    packData.value = PackViewState(pack, questions)
                }
            }
        }
    }

    fun onQuestionClick(questionId: Int) {
        val currentState = packData.value!!
        viewModelScope.launch {
            updateLastRead.update(currentState.pack)
        }
        val newQuestions = showQuestion(questionId, currentState.questions)
        packData.value = currentState.copy(questions = newQuestions)
    }

    private fun showQuestion(questionId: Int, questions: List<QuestionItem>): List<QuestionItem> =
        questions.map {
            if (it.question.id == questionId) {
                it.copy(isClosed = false)
            } else {
                it
            }
        }
}

data class QuestionItem(val question: QuestionEntity, val isClosed: Boolean, val price: String)

data class PackViewState(val pack: PackEntity, val questions: List<QuestionItem>)