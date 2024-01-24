package app.gratum.datastorepreferences.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.gratum.datastorepreferences.domain.UserRepository
import app.gratum.datastorepreferences.model.UserViewState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class UserViewModel(private val userRepository: UserRepository) : ViewModel() {

    private val _userViewState = MutableStateFlow<UserViewState>(UserViewState.Loading)
    val userViewState: StateFlow<UserViewState> = _userViewState

    fun saveUser(name: String, checked: Boolean) {
        viewModelScope.launch {
            userRepository.saveUser(name, checked)
        }
    }

    fun getUserProfile() {
        viewModelScope.launch {
            userRepository.getUserProfile().collect {
                _userViewState.value = it
            }
        }
    }
}
