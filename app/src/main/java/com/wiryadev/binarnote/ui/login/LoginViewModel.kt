package com.wiryadev.binarnote.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.wiryadev.binarnote.data.preference.AuthModel
import com.wiryadev.binarnote.data.repositories.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.lang.Exception
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    private val _uiState: MutableStateFlow<LoginUiState> = MutableStateFlow(LoginUiState())
    val uiState: LiveData<LoginUiState> get() = _uiState.asLiveData()

    init {
        checkAuthSession()
    }

    private fun checkAuthSession() {
        viewModelScope.launch {
            userRepository.getUserSession().collectLatest { user ->
                if (user.email.isNotBlank()) {
                    _uiState.update {
                        it.copy(
                            isLoggedIn = true,
                            errorMessage = null,
                        )
                    }
                }
            }
        }
    }

    fun login(
        email: String,
        password: String,
    ) {
        _uiState.update {
            it.copy(isLoading = true)
        }

        viewModelScope.launch {
            try {
                val loginResult = userRepository.login(
                    email = email,
                    password = password,
                )
                loginResult.collectLatest { user ->
                    userRepository.saveUserSession(
                        AuthModel(
                            username = user.username,
                            email = user.email,
                        )
                    )
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                        )
                    }
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = "User Not Found",
                    )
                }
            }
        }
    }

}

data class LoginUiState(
    val isLoggedIn: Boolean = false,
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
)