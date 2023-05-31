package com.andre.gitlist.ui.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.andre.gitlist.config.ApiConfig
import com.andre.gitlist.models.User
import com.andre.gitlist.repository.GithubRepository
import com.andre.gitlist.repository.UserDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

class DetailViewModel(context: Context): ViewModel(), CoroutineScope by MainScope() {

    private val repository: GithubRepository

    init {
        val userDao = UserDatabase.getDatabase(context).userDao()
        repository = GithubRepository(userDao)
        checkExistUser()
    }

    private val _isExist = MutableLiveData<Boolean>()
    val isExist: LiveData<Boolean> = _isExist

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _userName = MutableLiveData<String>()
    val userName: LiveData<String> = _userName

    private val _userData = MutableLiveData<User>()
    val userData: LiveData<User> = _userData

    private val _isError = MutableLiveData<Boolean>()
    val isError: LiveData<Boolean> = _isError

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> = _errorMessage

    fun getUserData(){
        _isError.value = false
        launch {
            try{
                _isLoading.value = true
                val service = ApiConfig.getInstance().getService()
                _userData.value = service.getUserDetail(_userName.value.toString())
                _isLoading.value = false

            } catch (e: Exception){
                _isError.value = true
                _errorMessage.value = e.toString()
            }
        }
    }

    fun setUsername(userName: String){
        _userName.value = userName
    }

    fun insertFavUser(){
        launch {
            _userData.value?.let { repository.insert(it) }
        }
    }

    fun checkExistUser(){
        launch {
            _isExist.value = _userName.value?.let { repository.checkUser(it) }
        }
    }

    fun deleteUser(){
        launch {
            _userData.value?.let { repository.delete(it) }
        }
    }

    fun favButton(){
        checkExistUser()
        if (_isExist.value == true){
            deleteUser()
        } else {
            insertFavUser()
        }
        checkExistUser()
    }
}