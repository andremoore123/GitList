package com.andre.gitlist.repository

import com.andre.gitlist.models.User

class GithubRepository(private val userDao: UserDao) {

    suspend fun getAllData(): List<User> {
        return userDao.getAllUsers()
    }

    suspend fun insert(user: User) {
        userDao.insertUser(user)
    }

    suspend fun delete(user: User) {
        userDao.deleteUser(user)
    }

    suspend fun update(user: User) {
        userDao.updateUser(user)
    }

    suspend fun checkUser(user: String): Boolean {
        return userDao.isUserExist(user)
    }
}
