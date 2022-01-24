package com.android.user.models

import com.google.gson.annotations.SerializedName

class UserModelResult (
    @SerializedName("results")
    var userList: ArrayList<UserModel>
)