package tt.co.jesses.privilege.data

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.MutablePreferences
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.doAnswer
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

class UserPreferencesRepositoryTest {

    private val dataStore = mock<DataStore<Preferences>>()
    private val repository = UserPreferencesRepository(dataStore)
    private val CHECKLIST_FILLED = booleanPreferencesKey("checklist_filled")

    @Test
    fun `isPrivilegeChecklistFilled returns false by default`() = runTest {
        val preferences = mock<Preferences>()
        whenever(preferences[CHECKLIST_FILLED]).thenReturn(null)
        whenever(dataStore.data).thenReturn(flowOf(preferences))

        val result = repository.isPrivilegeChecklistFilled.first()
        assertFalse(result)
    }

    @Test
    fun `isPrivilegeChecklistFilled returns true when saved`() = runTest {
        val preferences = mock<Preferences>()
        whenever(preferences[CHECKLIST_FILLED]).thenReturn(true)
        whenever(dataStore.data).thenReturn(flowOf(preferences))

        val result = repository.isPrivilegeChecklistFilled.first()
        assertTrue(result)
    }
}
