package com.andre.gitlist.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.andre.gitlist.config.ApiConfig
import com.andre.gitlist.models.User
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

class SearchViewModel : ViewModel(), CoroutineScope by MainScope() {
    private val _userName = MutableLiveData<String>()
    val userName: LiveData<String> = _userName

    private val _listSearchedUser = MutableLiveData<List<User>>()
    val listSearchedUser: LiveData<List<User>> = _listSearchedUser

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _isError = MutableLiveData<Boolean>()
    val isError: LiveData<Boolean> = _isError

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> = _errorMessage

    fun getSearchedUser() {
        _isError.value = false
        if (!(userName.value.isNullOrBlank() && userName.value.isNullOrEmpty())) {
            launch {
                try {
                    _isLoading.value = true
                    val service = ApiConfig.getInstance().getService()
                    _listSearchedUser.value = service.searchUsers(_userName.value.toString()).item
                    _isLoading.value = false
                } catch (e: Exception) {
                    _errorMessage.value = e.toString()
                    _isError.value = true
                }
            }
        }
    }

    fun setUserName(userName: String) {
        _userName.value = userName
    }
}