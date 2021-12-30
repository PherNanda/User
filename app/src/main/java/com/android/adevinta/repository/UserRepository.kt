package com.android.adevinta.repository

import com.android.adevinta.models.User
import com.android.adevinta.provider.user.UserApi
import okhttp3.MultipartBody

class UserRepository(private val userApi: UserApi) {

    suspend fun user(): List<User> {
        val requestBody = MultipartBody.Builder()
            .build()

        return userApi.fetchUser()
    }

}