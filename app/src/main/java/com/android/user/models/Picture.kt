package com.android.user.models

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Picture(
    @SerializedName("large")
    var large: String
): Serializable
