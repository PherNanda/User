package com.android.adevinta.models

import com.google.gson.annotations.SerializedName

data class UserResult(
    @SerializedName("results")
    var userList: ArrayList<User>
)
