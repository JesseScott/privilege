package tt.co.jesses.privilege.data

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

class UserPreferencesRepositoryTest {

    private val dataStore = mock<DataStore<Preferences>>()
    private val CHECKLIST_FILLED = booleanPreferencesKey("checklist_filled")
    private lateinit var repository: UserPreferencesRepository

    // We use a MutableSharedFlow to control the dataStore.data emission
    private val preferencesFlow = MutableSharedFlow<Preferences>(replay = 1)

    @Before
    fun setup() {
        whenever(dataStore.data).thenReturn(preferencesFlow)
        repository = UserPreferencesRepository(dataStore)
    }

    @Test
    fun `isPrivilegeChecklistFilled returns false by default`() = runTest {
        val preferences = mock<Preferences>()
        whenever(preferences[CHECKLIST_FILLED]).thenReturn(null)
        preferencesFlow.emit(preferences)

        val result = repository.isPrivilegeChecklistFilled.first()
        assertFalse(result)
    }

    @Test
    fun `isPrivilegeChecklistFilled returns true when saved`() = runTest {
        val preferences = mock<Preferences>()
        whenever(preferences[CHECKLIST_FILLED]).thenReturn(true)
        preferencesFlow.emit(preferences)

        val result = repository.isPrivilegeChecklistFilled.first()
        assertTrue(result)
    }
}
