package com.wiryadev.binarnote.ui.register

import androidx.lifecycle.*
import com.wiryadev.binarnote.data.local.entity.UserEntity
import com.wiryadev.binarnote.data.repositories.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@FlowPreview
@ExperimentalCoroutinesApi
@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    val queryChannel = MutableStateFlow("")

    private val _uiState: MutableStateFlow<RegisterUiState> = MutableStateFlow(RegisterUiState())
    val uiState: LiveData<RegisterUiState> get() = _uiState.asLiveData()

    val checkUserExist = queryChannel
        .debounce(200)
        .filter {
            it.trim().isNotEmpty()
        }
        .mapLatest {
            userRepository.checkUserExist(it)
        }
        .catch {
            _uiState.value = RegisterUiState(
                errorMessage = "Something Went Wrong"
            )
        }
        .asLiveData(Dispatchers.IO)

    fun register(user: UserEntity) {
        _uiState.update {
            it.copy(isLoading = true)
        }
        viewModelScope.launch {
            try {
                userRepository.register(user = user)
                _uiState.update {
                    it.copy(isLoading = false, isSuccess = true)
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(isLoading = false, errorMessage = e.message)
                }
            }
        }
    }

}

data class RegisterUiState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val isSuccess: Boolean = false,
)