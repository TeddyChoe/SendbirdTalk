package com.sendbird.sendbirdtalk

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class LoginViewModel: ViewModel() {
    private val _userId: MutableStateFlow<String> = MutableStateFlow("")
    val userId: StateFlow<String> = _userId.asStateFlow()

    fun edit(updateUserIds: String) {
        _userId.update {
            updateUserIds
        }
    }
}