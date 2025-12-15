package tt.co.jesses.privilege.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_preferences")

class UserPreferencesRepository @Inject constructor(
    private val dataStore: DataStore<Preferences>
) {
    private val CHECKLIST_FILLED = booleanPreferencesKey("checklist_filled")

    val isPrivilegeChecklistFilled: Flow<Boolean> = dataStore.data
        .map { preferences ->
            preferences[CHECKLIST_FILLED] ?: false
        }

    suspend fun setPrivilegeChecklistFilled(completed: Boolean) {
        dataStore.edit { preferences ->
            preferences[CHECKLIST_FILLED] = completed
        }
    }
}
