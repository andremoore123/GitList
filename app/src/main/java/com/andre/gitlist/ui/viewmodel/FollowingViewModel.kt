package com.andre.gitlist.ui.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.andre.gitlist.config.ApiConfig
import com.andre.gitlist.models.User
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

class FollowingViewModel : ViewModel(), CoroutineScope by MainScope() {

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _following = MutableLiveData<List<User>>()
    val following: LiveData<List<User>> = _following

    private val _isError = MutableLiveData<Boolean>()
    val isError: LiveData<Boolean> = _isError

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> = _errorMessage

    fun getFollowing(userName: String) {
        _isError.value = false
        launch {
            try {
                _isLoading.value = true
                val service = ApiConfig.getInstance().getService()
                _following.value = service.getFollowing(userName)
                _isLoading.value = false
            } catch (e: Exception) {
                Log.d("Errror", e.toString())
                _isError.value = true
                _errorMessage.value = e.toString()
            }
        }
    }
}