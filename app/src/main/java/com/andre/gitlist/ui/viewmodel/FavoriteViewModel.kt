package com.andre.gitlist.ui.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.andre.gitlist.models.User
import com.andre.gitlist.repository.GithubRepository
import com.andre.gitlist.repository.UserDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

class FavoriteViewModel(context: Context) : ViewModel(), CoroutineScope by MainScope() {
    private val repository: GithubRepository

    init {
        val userDao = UserDatabase.getDatabase(context).userDao()
        repository = GithubRepository(userDao)
        launch {
            _isError.value = false
            try {
                getFavorite()
            } catch (e: Exception) {
                _isError.value = true
                _errorMessage.value = e.toString()
            }
        }
    }

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _favorite = MutableLiveData<List<User>>()
    val favorite: LiveData<List<User>> = _favorite

    private val _isError = MutableLiveData<Boolean>()
    val isError: LiveData<Boolean> = _isError

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> = _errorMessage

    suspend fun getFavorite() {
        _isLoading.value = true
        _favorite.value = repository.getAllData()
        _isLoading.value = false
    }
}