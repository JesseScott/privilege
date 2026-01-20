package tt.co.jesses.privilege.data

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

class UserPreferencesRepositoryTest {
    private val dataStore = mock<DataStore<Preferences>>()
    private val checklistFilled = booleanPreferencesKey("checklist_filled")
    private lateinit var repository: UserPreferencesRepository

    // We use a MutableSharedFlow to control the dataStore.data emission
    private val preferencesFlow = MutableSharedFlow<Preferences>(replay = 1)

    @BeforeEach
    fun setup() {
        whenever(dataStore.data).thenReturn(preferencesFlow)
        repository = UserPreferencesRepository(dataStore)
    }

    @Test
    fun `isPrivilegeChecklistFilled returns false by default`() = runTest {
        val preferences = mock<Preferences>()
        whenever(preferences[checklistFilled]).thenReturn(null)
        preferencesFlow.emit(preferences)

        val result = repository.isPrivilegeChecklistFilled.first()
        assertFalse(result)
    }

    @Test
    fun `isPrivilegeChecklistFilled returns true when saved`() = runTest {
        val preferences = mock<Preferences>()
        whenever(preferences[checklistFilled]).thenReturn(true)
        preferencesFlow.emit(preferences)

        val result = repository.isPrivilegeChecklistFilled.first()
        assertTrue(result)
    }
}
