package com.android.users.models

import com.google.gson.annotations.SerializedName
import java.io.Serializable


data class User(
    @SerializedName("cell")
    var cell: String,

    @SerializedName("dob")
    var dob: Dob,

    @SerializedName("gender")
    var gender: String,

    @SerializedName("id")
    var id: Id,

    @SerializedName("name")
    var name: Name,

    @SerializedName("location")
    var location: Location,

    @SerializedName("nat")
    var nat: String,

    @SerializedName("phone")
    var phone: String,

    @SerializedName("login")
    var login: Login,

    @SerializedName("email")
    var email: String,

    @SerializedName("picture")
    var picture: Picture,

    @SerializedName("registered")
    var registered: Registered

): Serializable