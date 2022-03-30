package com.wiryadev.binarnote.ui.notes.home

import androidx.lifecycle.*
import com.wiryadev.binarnote.data.preference.AuthModel
import com.wiryadev.binarnote.data.repositories.NoteRepository
import com.wiryadev.binarnote.data.repositories.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val noteRepository: NoteRepository,
) : ViewModel() {

    val userSession = MutableLiveData<AuthModel>()

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
           userRepository.getUserSession().collectLatest {
               userSession.value = it
           }
        }
    }

}