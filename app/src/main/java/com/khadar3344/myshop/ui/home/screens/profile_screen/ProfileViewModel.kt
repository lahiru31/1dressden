package com.khadar3344.myshop.ui.home.screens.profile_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.khadar3344.myshop.data.auth.repository.AuthRepository
import com.khadar3344.myshop.model.User
import com.khadar3344.myshop.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val repository: AuthRepository
) : ViewModel() {
    private val _userInfo = MutableStateFlow<Resource<User>>(Resource.Loading)
    val userInfo: StateFlow<Resource<User>> = _userInfo

    init {
        getUserInfoFromFirebase()
    }

    private fun getUserInfoFromFirebase() = viewModelScope.launch {
        try {
            _userInfo.value = Resource.Loading
            val result = repository.retrieveData()
            _userInfo.value = result
        } catch (e: Exception) {
            _userInfo.value = Resource.Failure(e)
        }
    }

    fun updateUserInfoFirebase(user: User) = viewModelScope.launch {
        try {
            _userInfo.value = Resource.Loading
            repository.updateData(user)
            // Refresh user data after update
            getUserInfoFromFirebase()
        } catch (e: Exception) {
            _userInfo.value = Resource.Failure(e)
        }
    }

    fun logout() {
        repository.logout()
        _userInfo.value = Resource.Failure(Exception("User logged out"))
    }

    fun retryLoadingUserInfo() {
        getUserInfoFromFirebase()
    }
}
