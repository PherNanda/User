package com.android.adevinta.uicases.user

import android.os.Parcelable
import com.android.adevinta.models.*
import kotlinx.parcelize.Parcelize
import com.android.adevinta.uicases.UiModel
import java.util.*

sealed class UserUiModel : UiModel() {

    @Parcelize
    data class User(
        override val uid: Long = UUID.randomUUID().leastSignificantBits,
        val cell: String,
        val dob: Dob,
        val email: String,
        val gender: String,
        val id: Id,
        val location: Location,
        val login: Login,
        val name: Name,
        val nat: String,
        val phone: String,
        val picture: Picture,
        val registered: Registered
    ) : UserUiModel(), Parcelable

}

fun User.toUserUiModel(): UserUiModel.User{
    return UserUiModel.User(
        cell = cell,
        dob = dob,
        email = email,
        gender = gender,
        id = id,
        location = location,
        login = login,
        name = name,
        nat = nat,
        phone = phone,
        picture = picture,
        registered = registered
        )

}
