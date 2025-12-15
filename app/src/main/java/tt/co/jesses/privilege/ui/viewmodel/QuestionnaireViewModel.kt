package tt.co.jesses.privilege.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import tt.co.jesses.privilege.data.UserPreferencesRepository
import tt.co.jesses.privilege.model.Question
import javax.inject.Inject

@HiltViewModel
class QuestionnaireViewModel @Inject constructor(
    private val userPreferencesRepository: UserPreferencesRepository
) : ViewModel() {

    val isPrivilegeChecklistFilled: StateFlow<Boolean> = userPreferencesRepository.isPrivilegeChecklistFilled
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = false
        )

    private val _questions = listOf(
        Question(1, "Do you feel safe walking alone at night?"),
        Question(2, "Have you ever been discriminated against because of your skin color?"),
        Question(3, "Do you have access to clean drinking water?"),
        Question(4, "Did your parents pay for your college education?"),
        Question(5, "Do you see people like yourself represented in media?")
    )
    val questions: List<Question> = _questions

    // Map of Question ID to Answer (True/False)
    private val _answers = MutableStateFlow<Map<Int, Boolean>>(emptyMap())
    val answers: StateFlow<Map<Int, Boolean>> = _answers.asStateFlow()

    fun submitAnswer(questionId: Int, answer: Boolean) {
        _answers.update { currentAnswers ->
            currentAnswers + (questionId to answer)
        }
    }

    fun completeQuestionnaire() {
        viewModelScope.launch {
            userPreferencesRepository.setPrivilegeChecklistFilled(true)
        }
    }

    fun resetQuestionnaire() {
         viewModelScope.launch {
            userPreferencesRepository.setPrivilegeChecklistFilled(false)
            _answers.value = emptyMap()
        }
    }
}
