package com.android.adevinta.util

import android.content.Context
import android.content.SharedPreferences
import com.android.adevinta.models.*
import com.google.gson.annotations.SerializedName
import com.squareup.moshi.*
import java.io.IOException
import java.io.Serializable
import java.lang.reflect.Array.get
import java.lang.reflect.Array.set
import java.lang.reflect.Type

interface PersistanceStore {

    fun removedUserList(
        qty: Int,
        product: UserStore
    )

    fun getUserDeleted(): List<UserStore>

}

class DefaultPersistanceStore constructor(context: Context) : PersistanceStore {

    private val sharedPreferences: SharedPreferences = context.getSharedPreferences(
        SharedPreferencesStore.NAME_PREFERENCES, Context.MODE_PRIVATE
    )

    private val moshi = Moshi.Builder().build()

    override fun removedUserList(
        qty: Int,
        product: UserStore
    ) {
        with(sharedPreferences.edit()) {
            println("\nuserPro $product")
         try {
             //TODO
            var userRemovedList = getUserDeleted().toMutableList()

            val userTrobat = if (userRemovedList.any { it.name.first == product.name.first && it.name.last == product.name.last && it.email == product.email})
            {
               //sharedPreferences.getString(KEY_REMOVED_LIST, "")
               //sharedPreferences.edit().putString(KEY_REMOVED_LIST, product.name.first).apply()

                userRemovedList.add(product)

            } else {
                val user =
                    UserStore(
                        uid = product.uid,
                        cell = product.cell,
                        dob = product.dob,
                        email = product.email,
                        gender = product.gender,
                        id = product.id,
                        location = product.location,
                        login = product.login,
                        name = product.name,
                        nat = product.nat,
                        picture = product.picture,
                        registered = product.registered,
                        phone = product.phone
                    )
                putString(KEY_REMOVED_LIST, user.email)
                apply()
            }

                putString(KEY_REMOVED_LIST, product.email)
                apply()
            }catch (e: Exception){
                println(e.stackTrace)
                println(e.message)
            }

        }
    }

    override fun getUserDeleted(): List<UserStore> {
        val json = sharedPreferences.getString(KEY_REMOVED_LIST, null) ?: return listOf()

        val parameterizeDataType: Type = Types.newParameterizedType(
            List::class.java,
            UserStore::class.java
        )
        val jsonAdapter: JsonAdapter<List<UserStore>> = moshi.adapter(parameterizeDataType)

        return try {
            jsonAdapter.fromJson(json) ?: listOf()
        } catch (e: Exception) {
            listOf()
        }
    }

    companion object {
        private const val KEY_REMOVED_LIST = "user_removed_list"
    }

}

@JsonClass(generateAdapter = true)
data class UserStore(
    @Json(name = "uid")
    var uid: Long,

    @Json(name = "cell")
    var cell: String,

    @Json(name = "dob")
    var dob: Dob,

    @Json(name = "gender")
    var gender: String,

    @Json(name = "id")
    var id: Id,

    @Json(name = "name")
    var name: Name,

    @Json(name = "location")
    var location: Location,

    @Json(name = "nat")
    var nat: String,

    @Json(name = "phone")
    var phone: String,

    @Json(name = "login")
    var login: Login,

    @Json(name = "email")
    var email: String,

    @Json(name = "picture")
    var picture: Picture,

    @Json(name = "registered")
    var registered: Registered

)

@JsonClass(generateAdapter = true)
data class Dob(
    @Json(name = "date") var date: String,
    @Json(name = "age") var age: Int
)

@JsonClass(generateAdapter = true)
data class Id(
    @Json(name = "name")
    val name: String
)

@JsonClass(generateAdapter = true)
data class Location (
    @Json(name = "city") var city: String,
    @Json(name = "state") var state: String

)

@JsonClass(generateAdapter = true)
data class Login(
    @Json(name = "md5") val md5: String,
    @Json(name = "password") val password: String,
    @Json(name = "username") val username: String,
    @Json(name = "uuid") val uuid: String
)

@JsonClass(generateAdapter = true)
data class Name(
    @Json(name = "first") var first: String,
    @Json(name = "last") var last: String
)

@JsonClass(generateAdapter = true)
data class Picture(
    @Json(name = "large") var large: String
)

@JsonClass(generateAdapter = true)
data class Registered(
    @Json(name = "age") val age: Int,
    @Json(name = "date") val date: String
)

