package tt.co.jesses.privilege.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_preferences")

class UserPreferencesRepository
@Inject
constructor(private val dataStore: DataStore<Preferences>) {
    private val checklistFilled = booleanPreferencesKey("checklist_filled")

    val isPrivilegeChecklistFilled: Flow<Boolean> =
        dataStore.data
            .map { preferences ->
                preferences[checklistFilled] ?: false
            }

    suspend fun setPrivilegeChecklistFilled(completed: Boolean) {
        dataStore.edit { preferences ->
            preferences[checklistFilled] = completed
        }
    }
}
