package com.wiryadev.binarnote.ui.notes.home

import androidx.lifecycle.*
import com.wiryadev.binarnote.data.Result
import com.wiryadev.binarnote.data.local.entity.NoteEntity
import com.wiryadev.binarnote.data.preference.AuthModel
import com.wiryadev.binarnote.data.repositories.NoteRepository
import com.wiryadev.binarnote.data.repositories.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val noteRepository: NoteRepository,
) : ViewModel() {

    private val _userSession: MutableLiveData<AuthModel> = MutableLiveData()
    val userSession: LiveData<AuthModel> get() = _userSession

    private val _uiState: MutableStateFlow<HomeUiState> = MutableStateFlow(HomeUiState())
    val uiState: LiveData<HomeUiState> get() = _uiState.asLiveData()

    init {
        checkUserSession()
    }

    fun logout() {
        viewModelScope.launch {
            userRepository.deleteUserSession()
        }
    }

    private fun checkUserSession() {
        viewModelScope.launch {
            userRepository.getUserSession().collect {
                _userSession.value = it
            }
        }
    }

    fun getNoteByEmail(email: String) {
        _uiState.update {
            it.copy(
                isLoading = true,
            )
        }
        viewModelScope.launch(Dispatchers.IO) {
            try {
                noteRepository.getAllNotesByEmail(email).collect { notes ->
                    _uiState.update {
                        it.copy(
                            notes = notes,
                            isLoading = false,
                        )
                    }
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = e.message,
                    )
                }
            }
        }
    }

    fun createNote(note: NoteEntity) {
        _uiState.update {
            it.copy(
                isLoading = true,
                result = 0,
                action = Action.CREATE,
            )
        }
        viewModelScope.launch {
            val result = noteRepository.addNote(note = note)

            _uiState.update {
                when (result) {
                    is Result.Success -> it.copy(
                        isLoading = false,
                        result = result.data.toInt(),
                    )
                    is Result.Error -> it.copy(
                        isLoading = false,
                        errorMessage = result.exception.message
                    )
                }
            }
        }
    }

    fun updateNote(note: NoteEntity) {
        _uiState.update {
            it.copy(
                isLoading = true,
                result = 0,
                action = Action.UPDATE,
            )
        }
        viewModelScope.launch {
            val result = noteRepository.updateNote(note = note)

            _uiState.update {
                when (result) {
                    is Result.Success -> it.copy(
                        isLoading = false,
                        result = result.data,
                    )
                    is Result.Error -> it.copy(
                        isLoading = false,
                        errorMessage = result.exception.message
                    )
                }
            }
        }
    }

    fun deleteNote(note: NoteEntity) {
        _uiState.update {
            it.copy(
                isLoading = true,
                result = 0,
                action = Action.DELETE,
            )
        }
        viewModelScope.launch {
            val result = noteRepository.deleteNote(note = note)

            _uiState.update {
                when (result) {
                    is Result.Success -> it.copy(
                        isLoading = false,
                        result = result.data,
                    )
                    is Result.Error -> it.copy(
                        isLoading = false,
                        errorMessage = result.exception.message
                    )
                }
            }
        }
    }

}

data class HomeUiState(
    val isLoading: Boolean = false,
    val result: Int = 0,
    val action: Action = Action.CREATE,
    val notes: List<NoteEntity> = emptyList(),
    val errorMessage: String? = null,
)

enum class Action {
    CREATE, UPDATE, DELETE,
}