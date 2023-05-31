package com.andre.gitlist.service

import com.andre.gitlist.models.ActiveOrg
import com.andre.gitlist.models.ActiveUser
import com.andre.gitlist.models.SearchUsers
import com.andre.gitlist.models.User
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface GithubApiService {

    @GET("search/users")
    suspend fun searchUsers(@Query("q") query: String): SearchUsers

    @GET("users/{username}")
    suspend fun getUserDetail(@Path("username") username: String): User

    @GET("users/{username}/followers")
    suspend fun getFollowers(@Path("username") username: String): List<User>

    @GET("users/{username}/following")
    suspend fun getFollowing(@Path("username") username: String): List<User>

    @GET("search/users?q=type%3Auser")
    suspend fun getTotalActiveUser(): ActiveUser

    @GET("search/users?q=type%3Aorg")
    suspend fun getTotalActiveOrg(): ActiveOrg

    @GET("search/users?q=followers%3A>%3D1000&ref=searchresults&s=followers&type=Users")
    suspend fun getMostFollowers(): SearchUsers
}
