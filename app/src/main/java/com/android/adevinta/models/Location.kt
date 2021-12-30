package com.android.adevinta.models

data class Location(
    val city: String,
    val coordinates: Coordinates,
    val country: String,
    val postcode: Any,
    val state: String,
    val street: Street,
    val timezone: Timezone
)