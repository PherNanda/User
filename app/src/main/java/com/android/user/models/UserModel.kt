package com.android.user.models

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class UserModel(

    @SerializedName("name")
    var name: String,

    @SerializedName("birthdate")
    var birthdate: String,

   @SerializedName("id")
   var id: Int

) : Serializable
