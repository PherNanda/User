package com.android.user.models

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Id(
    @SerializedName("name")
    val name: String
): Serializable