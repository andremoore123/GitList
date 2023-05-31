package com.andre.gitlist

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.andre.gitlist.models.User
import com.andre.gitlist.repository.UserDao
import com.andre.gitlist.repository.UserDatabase
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class UserDatabaseTest {

    private lateinit var userDao: UserDao
    private lateinit var userDatabase: UserDatabase

    @Before
    fun setUp() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        userDatabase = Room.inMemoryDatabaseBuilder(context, UserDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        userDao = userDatabase.userDao()
    }

    @After
    fun tearDown() {
        userDatabase.close()
    }

    @Test
    fun testInsertAndGetUser() = runBlocking {
        val user = User(
            name = "Sidiq Permana",
            login = "sidiqpermana",
            location = "Jakarta Indonesia",
            repository = 77,
            gist = 2,
            company = "Nusantara Beta Studio",
            followers = 616,
            following = 14,
            avatar = "https://avatars.githubusercontent.com/u/4090245?v=4"
        )

        userDao.insertUser(user)

        val result = userDao.getAllUsers()[0]

        assertEquals(user, result)
    }

}