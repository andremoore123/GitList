package com.andre.gitlist.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.andre.gitlist.config.ApiConfig
import com.andre.gitlist.models.User
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

class HomeViewModel : ViewModel(), CoroutineScope by MainScope() {

    init {
        launch {
            try {
                getUser()
            } catch (e: Exception) {
                _isError.value = true
                _errorMessage.value = e.toString()
            }
        }
    }

    // Loading
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    // Result
    private val _activeUser = MutableLiveData<Int>()
    val activeUser: LiveData<Int> = _activeUser

    private val _activeOrg = MutableLiveData<Int>()
    val activeOrg: LiveData<Int> = _activeOrg

    private val _topFollowers = MutableLiveData<List<User>>()
    val topFollowers: LiveData<List<User>> = _topFollowers

    private val _isError = MutableLiveData<Boolean>()
    val isError: LiveData<Boolean> = _isError

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> = _errorMessage

    private suspend fun getUser() {
        _isLoading.value = true
        _isError.value = false
        val service = ApiConfig.getInstance().getService()
        _activeUser.value = service.getTotalActiveUser().total
        _activeOrg.value = service.getTotalActiveOrg().total
        _topFollowers.value = service.getMostFollowers().item.take(5)
        _isLoading.value = false

    }

    override fun onCleared() {
        super.onCleared()
        cancel() //cancel all coroutines when ViewModel is cleared
    }
}
