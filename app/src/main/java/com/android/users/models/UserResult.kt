package com.android.users.models

import com.google.gson.annotations.SerializedName

data class UserResult(
    @SerializedName("results")
    var userList: ArrayList<User>
)
