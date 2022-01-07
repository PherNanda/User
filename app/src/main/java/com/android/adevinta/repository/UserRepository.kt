package com.android.adevinta.repository


import com.android.adevinta.models.User
import com.android.adevinta.models.UserResult
import com.android.adevinta.provider.user.UserApi

class UserRepository(private val userApi: UserApi) {

    suspend fun user(): User {
        return userApi.fetchUser()
    }

    suspend fun searchUsers(
        results: Int,
    ): UserResult {
        return userApi.fetchUsers(
            result = results
        )
    }

    suspend fun searchUsersNextPage(
        page: Int,
        results: Int,
        seed: String
    ): List<UserResult> {
        return userApi.fetchUsersNextPage(
            page = page,
            results = results,
            seed = seed
        )
    }

}