package com.android.adevinta.util

import android.content.Context
import android.content.SharedPreferences
import com.android.adevinta.models.*
import com.squareup.moshi.*
import java.lang.reflect.Type

interface PersistanceStore {

    fun removedUserList(
        qty: Int,
        product: UserStore
    )

    fun removeUserFromList(
        id: Int,
        fisrtName: String,
        lastName: String
    )

    fun getCart(): List<UserStore>

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

         try {

            val parameterizeDataType: Type = Types.newParameterizedType(
                List::class.java,
                UserStore::class.java
            )
            val jsonAdapter: JsonAdapter<List<UserStore>> = moshi.adapter(parameterizeDataType)
            val userRemovedList = getCart().toMutableList()

            val json = if (userRemovedList.any { it.name.first == product.name.first && it.name.last == product.name.last && it.email == product.email})
            {
                println(" it.productId cart[0] ${userRemovedList[0]}")
                val newList = userRemovedList.map { item ->
                    if (item.name.first == product.name.first && item.name.last == product.name.last && item.phone == product.phone) {
                        return@map item.copy()
                    }

                    return@map item.copy()
                }
                jsonAdapter.toJson(newList)
            } else {
                userRemovedList.add(
                    UserStore(
                        uid = product.uid,
                        cell= product.cell,
                        dob= product.dob,
                        email= product.email,
                        gender= product.gender,
                        id= product.id,
                        location= product.location,
                        login= product.login,
                        name= product.name,
                        nat = product.nat,
                        picture= product.picture,
                        registered= product.registered,
                        phone = product.phone
                    )

                )

                jsonAdapter.toJson(userRemovedList)
            }

                putString(KEY_REMOVED_LIST, json)
                apply()
            }catch (e: Exception){
                println(e.stackTrace)
                println(e.message)
            }

        }
    }

    override fun removeUserFromList(
        id: Int,
        fisrtName: String,
        lastName: String
    ){

    }

    override fun getCart(): List<UserStore> {
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
    @Json(name = "uid") val uid: Long,
    @Json(name = "cell") val cell: String,
    @Json(name = "dob") val dob: Dob,
    @Json(name = "email") val email: String,
    @Json(name = "gender") val gender: String,
    @Json(name = "id") val id: Id,
    @Json(name = "location") val location: Location,
    @Json(name = "login") val login: Login,
    @Json(name = "name") val name: Name,
    @Json(name = "nat") val nat: String,
    @Json(name = "picture") val picture: Picture,
    @Json(name = "registered") val registered: Registered,
    @Json(name = "phone") val phone: String
)

@JsonClass(generateAdapter = true)
data class Dob(
    @Json(name = "date") var date: String,
    @Json(name = "age") var age: Int
)

@JsonClass(generateAdapter = true)
data class Id(
    @Json(name = "name") val name: String,
    @Json(name = "value") val value: String
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
    @Json(name = "salt") val salt: String,
    @Json(name = "sha1") val sha1: String,
    @Json(name = "sha256") val sha256: String,
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