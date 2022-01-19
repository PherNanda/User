package com.android.user.models

import com.google.gson.annotations.SerializedName

data class Street(

    @SerializedName("number")
    var number: String,

    @SerializedName("name")
    var name: String
)
