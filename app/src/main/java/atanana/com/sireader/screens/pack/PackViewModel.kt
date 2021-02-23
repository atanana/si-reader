package atanana.com.sireader.screens.pack

import androidx.lifecycle.viewModelScope
import atanana.com.sireader.database.PackEntity
import atanana.com.sireader.database.QuestionEntity
import atanana.com.sireader.usecases.GetPackWithQuestions
import atanana.com.sireader.usecases.UpdateLastRead
import atanana.com.sireader.viewmodels.BaseViewModel
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class PackViewModel @AssistedInject constructor(
    provider: GetPackWithQuestions,
    private val updateLastRead: UpdateLastRead,
    @Assisted packId: Int
) : BaseViewModel() {

    private val _pack = MutableStateFlow<PackViewState?>(null)
    val pack: Flow<PackViewState> = _pack.filterNotNull()

    init {
        provider.getPack(packId).onEach { (pack, questions) ->
            _pack.value = PackViewState(pack, questions)
        }.launchIn(viewModelScope)
    }

    fun onQuestionClick(questionId: Int) {
        val currentState = _pack.value!!
        viewModelScope.launch {
            updateLastRead.update(currentState.pack)
        }
        val newQuestions = showQuestion(questionId, currentState.questions)
        _pack.value = currentState.copy(questions = newQuestions)
    }

    private fun showQuestion(questionId: Int, questions: List<QuestionItem>): List<QuestionItem> =
        questions.map {
            if (it.question.id == questionId) {
                it.copy(isClosed = false)
            } else {
                it
            }
        }

    @AssistedFactory
    interface Factory {
        fun create(packId: Int): PackViewModel
    }
}

data class QuestionItem(val question: QuestionEntity, val isClosed: Boolean, val price: String)

data class PackViewState(val pack: PackEntity, val questions: List<QuestionItem>)