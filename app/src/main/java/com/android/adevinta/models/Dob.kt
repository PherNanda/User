package com.android.adevinta.models

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Dob(

        @SerializedName("date")
        var date: String,

        @SerializedName("age")
        var age: Int
): Serializable
