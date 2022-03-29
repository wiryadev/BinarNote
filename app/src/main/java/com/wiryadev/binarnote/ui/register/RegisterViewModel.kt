package com.wiryadev.binarnote.ui.register

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.wiryadev.binarnote.data.local.entity.UserEntity
import com.wiryadev.binarnote.data.repositories.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.mapLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@FlowPreview
@ExperimentalCoroutinesApi
@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    val queryChannel = MutableStateFlow("")

    val checkUserExist = queryChannel
        .debounce(200)
        .filter {
            it.trim().isNotEmpty()
        }
        .mapLatest {
            userRepository.checkUserExist(it)
        }
        .asLiveData(Dispatchers.IO)

    fun register(user: UserEntity) {
        viewModelScope.launch {
            userRepository.register(user = user)
        }
    }

}