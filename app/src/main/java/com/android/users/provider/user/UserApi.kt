package com.android.users.provider.user

import com.android.users.models.User
import com.android.users.models.UserResult
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface UserApi {

    @Headers("Accept: application/json")
    @GET(".")
    suspend fun fetchUsers(
        @Query("results") result: Int
    ): UserResult


    @Headers("Accept: application/json")
    @GET(".")
    suspend fun fetchUsersNextPage(
        @Query(value = "page") page: Int,
        @Query(value = "results") results: Int,
        @Query(value = "seed") seed: String = "abc"
    ): List<UserResult>

    @Headers("Accept: application/json")
    @GET("/")
    suspend fun fetchUser(
    ): User
}