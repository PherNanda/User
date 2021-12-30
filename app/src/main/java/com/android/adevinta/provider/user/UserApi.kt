package com.android.adevinta.provider.user

import com.android.adevinta.models.User
import com.android.adevinta.models.Users
import retrofit2.http.GET
import retrofit2.http.Path

interface UserApi {

    @GET("/?results={results}")
    suspend fun fetchUsers(
        @Path("results") results: String,
    ): List<Users>

    @GET("/?page={page}&results={results}&seed={seed}")
    suspend fun fetchUsersPage(
        @Path(value = "page") page: String,
        @Path(value = "results") results: String,
        @Path(value = "seed") seed: String
    ): List<Users>

    @GET("/")
    suspend fun fetchUser(
    ): List<User>
}