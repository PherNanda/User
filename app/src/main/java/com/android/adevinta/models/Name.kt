package com.android.adevinta.models

import com.google.gson.annotations.SerializedName
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import java.io.Serializable

data class Name(
    @SerializedName("first")
    var first: String,

    @SerializedName("last")
    var last: String

): Serializable