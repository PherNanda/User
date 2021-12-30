package com.android.adevinta.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Users(
    @Json(name = "info") val info: Info,
    @Json(name = "results") val results: List<User>
)