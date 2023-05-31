package com.andre.gitlist.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.andre.gitlist.config.ApiConfig
import com.andre.gitlist.models.User
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

class FollowersViewModel : ViewModel(), CoroutineScope by MainScope() {

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _followers = MutableLiveData<List<User>>()
    val followers: LiveData<List<User>> = _followers

    private val _isError = MutableLiveData<Boolean>()
    val isError: LiveData<Boolean> = _isError

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> = _errorMessage

    fun getFollowers(userName: String) {
        _isError.value = false
        launch {
            try {
                _isLoading.value = true
                val service = ApiConfig.getInstance().getService()
                _followers.value = service.getFollowers(userName)
                _isLoading.value = false
            } catch (e: Exception) {
                _isError.value = true
                _errorMessage.value = e.toString()
            }
        }
    }
}