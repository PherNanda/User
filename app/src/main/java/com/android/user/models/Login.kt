package com.android.user.models

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Login(
    @SerializedName("md5")
    val md5: String,
    @SerializedName("password")
    val password: String,
    @SerializedName("username")
    val username: String,
    @SerializedName("uuid")
    val uuid: String
): Serializable