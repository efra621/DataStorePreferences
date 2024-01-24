package app.gratum.datastorepreferences.model

data class UserModel(val name: String, val rol: Boolean) {
    companion object {
        const val KEY_USER = "user_key"
        const val KEY_CHECKED = "checked_key"
    }
}

//Posibles estados de la vista
sealed class UserViewState {
    object Loading : UserViewState()
    data class Success(val userModel: UserModel) : UserViewState()
    data class Error(val errorMessage: String) : UserViewState()
}