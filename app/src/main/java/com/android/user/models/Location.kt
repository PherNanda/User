package com.android.user.models

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Location (
    @SerializedName("city")
    var city: String,

    @SerializedName("state")
    var state: String,

    @SerializedName("country")
    var country:String,

    @SerializedName("postcode")
    var postcode: String,

    @SerializedName("street")
    var street: Street

): Serializable
