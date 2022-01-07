package com.android.adevinta.models

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Id(
    @SerializedName("name")
    val name: String,
    @SerializedName("value")
    val value: String
): Serializable