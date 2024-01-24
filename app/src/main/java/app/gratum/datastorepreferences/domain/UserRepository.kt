package app.gratum.datastorepreferences.domain

import app.gratum.datastorepreferences.model.UserViewState
import kotlinx.coroutines.flow.Flow

//Capa de abastraccion
//Es una interfaz que define las operaciones que pueden realizarse con respecto al usuario,
//como guardar y recuperar datos. También define el flujo de estados de la vista (UserViewState).

interface UserRepository {
    suspend fun saveUser(name: String, checked: Boolean)
    fun getUserProfile(): Flow<UserViewState>
}
