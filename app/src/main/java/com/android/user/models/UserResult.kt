package com.android.user.models

import com.google.gson.annotations.SerializedName

data class UserResult(
    @SerializedName("results")
    var userList: ArrayList<User>
)
