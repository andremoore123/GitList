package com.andre.gitlist.repository

import androidx.room.*
import com.andre.gitlist.models.User

@Dao
interface UserDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: User)

    @Update
    suspend fun updateUser(user: User)

    @Delete
    suspend fun deleteUser(user: User)

    @Query("SELECT EXISTS(SELECT * FROM user_database WHERE login = :userId)")
    suspend fun isUserExist(userId: String): Boolean

    @Query("SELECT * FROM user_database")
    suspend fun getAllUsers(): List<User>

}