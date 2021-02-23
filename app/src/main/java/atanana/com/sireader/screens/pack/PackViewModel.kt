package atanana.com.sireader.screens.pack

import androidx.lifecycle.viewModelScope
import atanana.com.sireader.database.PackEntity
import atanana.com.sireader.database.QuestionEntity
import atanana.com.sireader.usecases.GetPackWithQuestions
import atanana.com.sireader.usecases.UpdateLastRead
import atanana.com.sireader.viewmodels.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PackViewModel @Inject constructor(
    private val provider: GetPackWithQuestions,
    private val updateLastRead: UpdateLastRead
) : BaseViewModel() {

    private val _pack = MutableStateFlow<PackViewState?>(null)
    val pack: Flow<PackViewState> = _pack.filterNotNull()

    fun loadPack(packId: Int) {
        if (_pack.value?.pack?.id != packId) {
            viewModelScope.launch {
                provider.getPack(packId).collect { (pack, questions) ->
                    _pack.value = PackViewState(pack, questions)
                }
            }
        }
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
}

data class QuestionItem(val question: QuestionEntity, val isClosed: Boolean, val price: String)

data class PackViewState(val pack: PackEntity, val questions: List<QuestionItem>)