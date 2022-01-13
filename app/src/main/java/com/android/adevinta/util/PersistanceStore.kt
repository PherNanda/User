package com.android.adevinta.util

import android.content.Context
import android.content.SharedPreferences
import com.android.adevinta.models.Street
import com.android.adevinta.uicases.user.UserUiModel
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.annotations.SerializedName
import com.squareup.moshi.*
import com.google.gson.reflect.TypeToken




interface PersistanceStore {

    fun removedUserList(
        product: UserStore
    )

    fun getUsersDeleted(): List<UserStore>

}

class DefaultPersistanceStore constructor(context: Context) : PersistanceStore {

    private val sharedPreferences: SharedPreferences = context.getSharedPreferences(
        SharedPreferencesStore.NAME_PREFERENCES, Context.MODE_PRIVATE
    )

    private val moshi = Moshi.Builder().build()

    override fun removedUserList(
        product: UserStore
    ) {
        with(sharedPreferences.edit()) {
            println("\nuserPro $product")
         try {
             //TODO
            val deletedList = getUsersDeleted().toMutableList()

             /**if (deletedList.size > 0){
                 deletedList.add(product)
             }**/

            /**deletedList.map { item->
            if ( item.name.first == product.name.first && item.name.last == product.name.last && item.email == product.email)
            {
                val jsonString = GsonBuilder().create().toJson(deletedList)
                sharedPreferences.edit().putString(KEY_REMOVED_LIST, jsonString).apply()
                apply()
                return@map

            } else {**/
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

                println("after user $user")
             //Save that String in SharedPreferences
             deletedList.add(product)
             val jsonString = GsonBuilder().create().toJson(deletedList)
             println("after jsonString $jsonString")
             putString(KEY_REMOVED_LIST, jsonString).apply()
             apply()
            //  }
           // }
         }catch (e: Exception){
                println(e.stackTrace)
                println(e.message)
            }

        }
    }

    override fun getUsersDeleted(): List<UserStore> {

        println(" in getUserDeleted")
        return try {
            var arrayItems: List<UserStore> = listOf()
            val serializedObject = sharedPreferences.getString(KEY_REMOVED_LIST, null) ?: return listOf()

            println(" serializedObject $serializedObject")
            if (serializedObject != null) {
                val gson = Gson()
                val type = object : TypeToken<List<UserStore?>?>() {}.type
                arrayItems = gson.fromJson(serializedObject, type)

                println(" array map ${arrayItems.map { it.name }}")
            }

            //arrayItems.map { println("it $it") }
            arrayItems
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

) {
    fun toUsersUiModel(): UserUiModel.User {

        return UserUiModel.User(
            uid = uid,
            cell = cell,
            dob = com.android.adevinta.models.Dob(dob.date,dob.age),
            email = email,
            gender = gender,
            id = com.android.adevinta.models.Id(id.name),
            location = com.android.adevinta.models.Location(location.city,location.state, location.country, location.postcode,location.street),
            login = com.android.adevinta.models.Login(login.md5,login.password,login.username,login.uuid),
            name = com.android.adevinta.models.Name(name.first,name.last),
            nat = nat,
            phone = phone,
            picture = com.android.adevinta.models.Picture(picture.large),
            registered = com.android.adevinta.models.Registered(registered.age,registered.date)
        )

    }
}

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
    @Json(name = "state") var state: String,
    @Json(name = "country") var country:String,
    @Json(name = "postcode") var postcode: String,
    @Json(name = "street") var street: Street

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

