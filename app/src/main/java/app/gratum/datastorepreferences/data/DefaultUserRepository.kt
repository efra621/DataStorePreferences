package app.gratum.datastorepreferences.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import app.gratum.datastorepreferences.model.UserModel
import app.gratum.datastorepreferences.domain.UserRepository
import app.gratum.datastorepreferences.model.UserViewState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_prefs")

class DefaultUserRepository(private val context: Context) : UserRepository {

    override suspend fun saveUser(name: String, checked: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[stringPreferencesKey(UserModel.KEY_USER)] = name
            preferences[booleanPreferencesKey(UserModel.KEY_CHECKED)] = checked
        }
    }

    override fun getUserProfile(): Flow<UserViewState> {
        return context.dataStore.data.map { preferences ->
            try {
                val userModel = UserModel(
                    name = preferences[stringPreferencesKey(UserModel.KEY_USER)].orEmpty(),
                    rol = preferences[booleanPreferencesKey(UserModel.KEY_CHECKED)] ?: false
                )
                UserViewState.Success(userModel)
            } catch (e: Exception) {
                UserViewState.Error(e.message ?: "An error occurred")
            }
        }.catch { e ->
            emit(UserViewState.Error(e.message ?: "An error occurred"))
        }.onStart {
            emit(UserViewState.Loading)
        }.flowOn(Dispatchers.IO)
    }
}