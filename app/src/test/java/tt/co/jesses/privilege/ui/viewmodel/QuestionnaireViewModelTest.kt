package tt.co.jesses.privilege.ui.viewmodel

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.RegisterExtension
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import tt.co.jesses.privilege.data.UserPreferencesRepository

class QuestionnaireViewModelTest {

    @JvmField
    @RegisterExtension
    val mainDispatcherExtension = MainDispatcherExtension()

    private val userPreferencesRepository = mock<UserPreferencesRepository>()
    private lateinit var viewModel: QuestionnaireViewModel

    private val isPrivilegeChecklistFilledFlow = MutableStateFlow(false)

    @BeforeEach
    fun setup() {
        whenever(userPreferencesRepository.isPrivilegeChecklistFilled).thenReturn(isPrivilegeChecklistFilledFlow)
        viewModel = QuestionnaireViewModel(userPreferencesRepository)
    }

    @Test
    fun `initial state is correct`() = runTest {
        // Verify questions are loaded
        assertEquals(5, viewModel.questions.size)
        assertEquals("Do you feel safe walking alone at night?", viewModel.questions[0].text)

        // Verify answers are empty
        assertTrue(viewModel.answers.value.isEmpty())

        // Verify completion status (default false)
        // Using first() ensures we collect the flow to get the initial value
        assertFalse(viewModel.isPrivilegeChecklistFilled.first())
    }

    @Test
    fun `submitAnswer updates answers state`() = runTest {
        viewModel.submitAnswer(1, true)

        val answers = viewModel.answers.value
        assertEquals(1, answers.size)
        assertTrue(answers[1] == true)

        viewModel.submitAnswer(2, false)
        val updatedAnswers = viewModel.answers.value
        assertEquals(2, updatedAnswers.size)
        assertTrue(updatedAnswers[2] == false)

        // Update existing answer
        viewModel.submitAnswer(1, false)
        val modifiedAnswers = viewModel.answers.value
        assertTrue(modifiedAnswers[1] == false)
    }

    @Test
    fun `completeQuestionnaire calls repository`() = runTest {
        viewModel.completeQuestionnaire()
        verify(userPreferencesRepository).setPrivilegeChecklistFilled(true)
    }

    @Test
    fun `resetQuestionnaire resets answers and calls repository`() = runTest {
        viewModel.submitAnswer(1, true)
        assertFalse(viewModel.answers.value.isEmpty())

        viewModel.resetQuestionnaire()

        assertTrue(viewModel.answers.value.isEmpty())
        verify(userPreferencesRepository).setPrivilegeChecklistFilled(false)
    }

    @Test
    fun `isPrivilegeChecklistFilled reflects repository updates`() = runTest {
        // Start collecting the flow in the background to ensure it stays active
        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.isPrivilegeChecklistFilled.collect {}
        }

        // Initial state
        assertFalse(viewModel.isPrivilegeChecklistFilled.value)

        // Simulate repository update
        isPrivilegeChecklistFilledFlow.value = true

        // Verify ViewModel updates
        assertTrue(viewModel.isPrivilegeChecklistFilled.value)
    }
}
