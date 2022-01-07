package com.android.adevinta


import android.content.Context
import androidx.lifecycle.*
import com.android.adevinta.models.*
import com.android.adevinta.repository.UserRepository
import com.android.adevinta.uicases.user.UserUiModel
import com.android.adevinta.uicases.user.toUserUiModel
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.launch
import timber.log.Timber


class MainActivityViewModel (
    private val userRepository: UserRepository ) : ViewModel() {

    private var usersPageNumber = 0

    private var _users: MutableLiveData<List<UserUiModel.User>> = MutableLiveData(listOf())
    val users: LiveData<List<UserUiModel.User>> = _users

    private var _user: MutableLiveData<UserUiModel.User> = MutableLiveData()
    val user: LiveData<UserUiModel.User> = _user


    /**val dataList: LiveData<List<UserUiModel.User>> =
        MediatorLiveData<List<UserUiModel.User>>().apply {

            fun updateList() {
                val mergedList = mutableListOf<UserUiModel.User>()

                val users = _users.value ?: listOf()

                mergedList.addAll(users)
                value = mergedList
            }

            addSource(_users) {
                updateList()
            }
        }**/

    fun search(firstName: String){

        if (firstName.length < LENGHT_SEARCH){
            _users.value = loadUsers(PAGE_LIMIT)
            usersPageNumber = 0
        }else {
            searchUser(firstName)
        }

    }

    fun searchLast(LastName: String){

        if (LastName.length < LENGHT_SEARCH){
            _users.value = loadUsers(PAGE_LIMIT)
            usersPageNumber = 0
        }else {
            searchLastName(LastName)
        }

    }

    fun searchEmail(email: String){

        if (email.length < 10){
            _users.value = loadUsers(PAGE_LIMIT)
            usersPageNumber = 0
        }else {
            searchEmailUser(email)
        }

    }

    /****TEST NO REPEAT USER LIST**/
    /**val u1 = UserUiModel.User(uid=-7439947363771142235L, cell="775-082-4672", dob=Dob(date="1979-08-31T00:20:30.653Z", age=43), email="melodie.jean-baptiste@example.com", gender="female", id=Id(name="", value="null"), location=Location(city="Armstrong", state="New Brunswick"), login=Login(md5="e14e21aca03342db9c1018bd70c961b8", password="booty", salt="UQhVUr8c", sha1="3e7ba5de0bd73d3e91cf2ce42526bb28e4538701", sha256="f1993d919ef27bddc6a255e1195a350334ff478ec402e83da41b4d934e0ee440", username="orangegoose429", uuid="d688a804-bbb2-4cb4-b028-55a22f66fac2"), name=Name(first="Melodie", last="Jean-Baptiste"), nat="CA", phone="944-967-1825", picture=Picture(large="https://randomuser.me/api/portraits/women/15.jpg"), registered=Registered(age=17, date="2005-01-16T02:30:46.404Z"))
    val u2 = UserUiModel.User(uid=-4725822415056598274L, cell="0170-3142179", dob=Dob(date="1991-04-20T05:00:38.383Z", age=31), email="ludwina.boll@example.com", gender="female", id=Id(name="", value="null"), location=Location(city="Freising", state="Sachsen-Anhalt"), login=Login(md5="c23e9d59f80d02824b0339cd0c9724f2", password="phillies", salt="wxPz3vO4", sha1="a5f2f2b860990104f100b1380252ef82dc22efb4", sha256="4fdfd129cea811320948f3c23c19197cef4eafe04f2a55d62136171ad6efb447", username="heavywolf628", uuid="8cb90d46-9b4e-46bd-8c3e-a1a26c4d456a"), name=Name(first="Ludwina", last="Boll"), nat="DE", phone="0548-8935964", picture=Picture(large="https://randomuser.me/api/portraits/women/53.jpg"), registered=Registered(age=8, date="2014-10-23T04:14:17.447Z"))
    val u3 = UserUiModel.User(uid=-7061372037530532270L, cell="47974995", dob=Dob(date="1958-06-22T02:00:35.743Z", age=64), email="victoria.olsen@example.com", gender="female", id=Id(name="CPR", value="220658-7635"), location=Location(city="Kongsvinger", state="Syddanmark"), login=Login(md5="f4464ca2c122879c9717774e6b1a8628", password="bullfrog", salt="5QTn76mm", sha1="daad71e7bd27ee91ef04a270a02cad1fb1068ec2", sha256="ba3f0524ba06202da58fef30812ebe41fed6a21e8a4e215b8bc0af2478ed7c24", username="blueostrich878", uuid="5aeb7e9d-b887-4edf-98ea-6042732bf5cf"), name=Name(first="Victoria", last="Olsen"), nat="DK", phone="53986211", picture=Picture(large="https://randomuser.me/api/portraits/women/75.jpg"), registered=Registered(age=20, date="2002-05-19T17:01:21.464Z"))
    val u4 = UserUiModel.User(uid=-5056767283044690892L, cell="0400-656-272", dob=Dob(date="1948-11-12T06:25:31.459Z", age=74), email="ricky.adams@example.com", gender="male", id=Id(name="TFN", value="744134336"), location=Location(city="Sydney", state="Australian Capital Territory"), login=Login(md5="9228fed2b1e0e1aceb9d7d978cbe2767", password="kokomo", salt="ZFRZIk4T", sha1="1ac24e4e7e5db9815017eb6f2721fa29b3961f20", sha256="6b1e48d01a08e5a4c031881e0e54d72c2afee989dc9e335d315b36543dd6ae4e", username="tinyfrog584", uuid="6fe6f1eb-b3fe-4332-bad4-0c84fd94117b"), name=Name(first="Ricky", last="Adams"), nat="AU", phone="07-9353-2287", picture=Picture(large="https://randomuser.me/api/portraits/men/35.jpg"), registered=Registered(age=20, date="2002-05-05T04:35:26.986Z"))
    val u5 = UserUiModel.User(uid=-7863959058655840669L, cell="0175-2355793", dob=Dob(date="1972-09-11T22:18:33.176Z", age=50), email="lieselotte.kothe@example.com", gender="female", id=Id(name="", value="null"), location=Location(city="Oer-Erkenschwick", state="Brandenburg"), login=Login(md5="985bb451e90ece0414d55841061b9d87", password="zeppelin", salt="YEYkW93q", sha1="753d7b8457c91ada086566e1c7fb87393051f684", sha256="354e62c3acb497a2baa7914175a3ac63363ca13b65670adf8b1597ca61bcefa5", username="happyostrich464", uuid="6ef9ca59-07c0-4e3d-815d-8bd82eb8d481"), name=Name(first="Lieselotte", last="Kothe"), nat="DE", phone="0513-6890318", picture=Picture(large="https://randomuser.me/api/portraits/women/53.jpg"), registered=Registered(age=13, date="2009-05-23T19:10:47.215Z"))

    val u6 = UserUiModel.User(uid=-7439947363771142235L, cell="775-082-4672", dob=Dob(date="1979-08-31T00:20:30.653Z", age=43), email="melodie.jean-baptiste@example.com", gender="female", id=Id(name="", value="null"), location=Location(city="Armstrong", state="New Brunswick"), login=Login(md5="e14e21aca03342db9c1018bd70c961b8", password="booty", salt="UQhVUr8c", sha1="3e7ba5de0bd73d3e91cf2ce42526bb28e4538701", sha256="f1993d919ef27bddc6a255e1195a350334ff478ec402e83da41b4d934e0ee440", username="orangegoose429", uuid="d688a804-bbb2-4cb4-b028-55a22f66fac2"), name=Name(first="Melodie", last="Jean-Baptiste"), nat="CA", phone="944-967-1825", picture=Picture(large="https://randomuser.me/api/portraits/women/15.jpg"), registered=Registered(age=17, date="2005-01-16T02:30:46.404Z"))
    val u7 = UserUiModel.User(uid=-4725822415056598274L, cell="0170-3142179", dob=Dob(date="1991-04-20T05:00:38.383Z", age=31), email="ludwina.boll@example.com", gender="female", id=Id(name="", value="null"), location=Location(city="Freising", state="Sachsen-Anhalt"), login=Login(md5="c23e9d59f80d02824b0339cd0c9724f2", password="phillies", salt="wxPz3vO4", sha1="a5f2f2b860990104f100b1380252ef82dc22efb4", sha256="4fdfd129cea811320948f3c23c19197cef4eafe04f2a55d62136171ad6efb447", username="heavywolf628", uuid="8cb90d46-9b4e-46bd-8c3e-a1a26c4d456a"), name=Name(first="Ludwina", last="Boll"), nat="DE", phone="0548-8935964", picture=Picture(large="https://randomuser.me/api/portraits/women/53.jpg"), registered=Registered(age=8, date="2014-10-23T04:14:17.447Z"))
    val u8 = UserUiModel.User(uid=-7061372037530532270L, cell="47974995", dob=Dob(date="1958-06-22T02:00:35.743Z", age=64), email="victoria.olsen@example.com", gender="female", id=Id(name="CPR", value="220658-7635"), location=Location(city="Kongsvinger", state="Syddanmark"), login=Login(md5="f4464ca2c122879c9717774e6b1a8628", password="bullfrog", salt="5QTn76mm", sha1="daad71e7bd27ee91ef04a270a02cad1fb1068ec2", sha256="ba3f0524ba06202da58fef30812ebe41fed6a21e8a4e215b8bc0af2478ed7c24", username="blueostrich878", uuid="5aeb7e9d-b887-4edf-98ea-6042732bf5cf"), name=Name(first="Victoria", last="Olsen"), nat="DK", phone="53986211", picture=Picture(large="https://randomuser.me/api/portraits/women/75.jpg"), registered=Registered(age=20, date="2002-05-19T17:01:21.464Z"))
    val u9 = UserUiModel.User(uid=-5056767283044690892L, cell="0400-656-272", dob=Dob(date="1948-11-12T06:25:31.459Z", age=74), email="ricky.adams@example.com", gender="male", id=Id(name="TFN", value="744134336"), location=Location(city="Sydney", state="Australian Capital Territory"), login=Login(md5="9228fed2b1e0e1aceb9d7d978cbe2767", password="kokomo", salt="ZFRZIk4T", sha1="1ac24e4e7e5db9815017eb6f2721fa29b3961f20", sha256="6b1e48d01a08e5a4c031881e0e54d72c2afee989dc9e335d315b36543dd6ae4e", username="tinyfrog584", uuid="6fe6f1eb-b3fe-4332-bad4-0c84fd94117b"), name=Name(first="Ricky", last="Adams"), nat="AU", phone="07-9353-2287", picture=Picture(large="https://randomuser.me/api/portraits/men/35.jpg"), registered=Registered(age=20, date="2002-05-05T04:35:26.986Z"))
    val u0 = UserUiModel.User(uid=-7863959058655840669L, cell="0175-2355793", dob=Dob(date="1972-09-11T22:18:33.176Z", age=50), email="lieselotte.kothe@example.com", gender="female", id=Id(name="", value="null"), location=Location(city="Oer-Erkenschwick", state="Brandenburg"), login=Login(md5="985bb451e90ece0414d55841061b9d87", password="zeppelin", salt="YEYkW93q", sha1="753d7b8457c91ada086566e1c7fb87393051f684", sha256="354e62c3acb497a2baa7914175a3ac63363ca13b65670adf8b1597ca61bcefa5", username="happyostrich464", uuid="6ef9ca59-07c0-4e3d-815d-8bd82eb8d481"), name=Name(first="Lieselotte", last="Kothe"), nat="DE", phone="0513-6890318", picture=Picture(large="https://randomuser.me/api/portraits/women/53.jpg"), registered=Registered(age=13, date="2009-05-23T19:10:47.215Z"))
**/

    fun loadUsers(users: Int): List<UserUiModel.User> {

        var listOut = _users.value?.toMutableList() ?: mutableListOf()
        viewModelScope.launch {
            val result = runCatching {
                val response =
                    userRepository.searchUsers(
                        users
                    )

                if (response.userList.isEmpty()) {
                    return@launch
                }
                if (response.userList.isNotEmpty()) {
                    val list = _users.value?.toMutableList() ?: mutableListOf()
                    list.addAll(response.userList.map { it.toUserUiModel() })
                    _users.value = list
                    _users.value = list.distinctBy { Pair(it.name.first, it.name.last) }
                    listOut = list

                    response.userList.map {
                        println("\nit.toUser ${it.toUserUiModel()}")
                    }
                }
            }

            val exception = result.exceptionOrNull()
            if (exception != null && exception !is CancellationException) {

                Timber.e(exception.message.toString())
                Timber.e("Error us: ${exception.stackTrace}")
                Timber.e("ERROR us: ${exception.stackTraceToString()}")

            }
        }

        //TEST NOT REPEAT LIST
        /**listOut = mutableListOf()
        _users.value = mutableListOf()
        listOut.add(0,u1)
        listOut.add(1,u2)
        listOut.add(2,u3)
        listOut.add(3,u4)
        listOut.add(4,u5)
        listOut.add(5,u6)
        listOut.add(6,u7)
        listOut.add(7,u8)
        listOut.add(8,u9)
        listOut.add(9,u0)
        _users.value = listOut
        _users.value = listOut.distinctBy { Pair(it.name.first, it.name.last) }**/
        return listOut
    }


    private fun searchUser(nameSearch: String): List<UserUiModel.User> {

        viewModelScope.launch {

            val filteredList = _users.value?.filter { it.name.first.lowercase() == nameSearch.lowercase() }

            if (filteredList != null) {

                if (filteredList.isNotEmpty()){
                    val list = _users.value?.toMutableList() ?: mutableListOf()
                    _users.value?.let { it -> list.addAll(it.filter { it.name.first.lowercase() == nameSearch.lowercase()}) }
                    _users.value = filteredList!!.distinctBy { Pair(it.name.first, it.name.last) }

                }
            }

        }
        return _users.value?: mutableListOf()
    }

    private fun searchLastName(lastSearch: String): List<UserUiModel.User> {

        viewModelScope.launch {

            val filteredList = _users.value?.filter { it.name.last.lowercase() == lastSearch.lowercase() }

            if (filteredList != null) {

                if (filteredList.isNotEmpty()){
                    val list = _users.value?.toMutableList() ?: mutableListOf()
                    _users.value?.let { it -> list.addAll(it.filter { it.name.last.lowercase() == lastSearch.lowercase() }) }
                    _users.value = filteredList!!.distinctBy { Pair(it.name.first, it.name.last) }
                }
            }

        }
        return _users.value?: mutableListOf()
    }

    private fun searchEmailUser(emailSearch: String): List<UserUiModel.User> {

        viewModelScope.launch {

            val filteredList = _users.value?.filter { it.email.lowercase() == emailSearch.lowercase() }

            if (filteredList != null) {

                if (filteredList.isNotEmpty()){
                    val list = _users.value?.toMutableList() ?: mutableListOf()
                    _users.value?.let { it -> list.addAll(it.filter { it.email.lowercase() == emailSearch.lowercase() }) }
                    _users.value = filteredList!!.distinctBy { Pair(it.name.first, it.name.last) }
                }
            }

        }
        return _users.value?: mutableListOf()
    }

    fun loadUser(): UserUiModel.User? {

        var user: UserUiModel.User? = _user.value

        viewModelScope.launch {
            val result = runCatching {
                val response =
                    userRepository.user()

                if (response == null) {
                    return@launch
                }
                if (response != null){
                    user = response.toUserUiModel()
                    _user.value = user!!
                }
            }

            val exception = result.exceptionOrNull()
            if (exception != null && exception !is CancellationException) {

                println(exception.message.toString())
                Timber.e("Error us: ${exception.stackTrace}")
                Timber.e("ERROR us: ${exception.stackTraceToString()}")

            }
        }
        return user
    }

    fun removeUser(name: String, lastName: String,context: Context) {
        try {
            //authStore.removeItemFromCartRef(ref, type)
            _users.value = _users.value?.toMutableList()?.apply {

                val userRemoveList = _users.value!!.filter { it.name.first.lowercase() == name.lowercase() && it.name.last == lastName}

                var userRemove: UserUiModel.User
                userRemoveList.map {

                    userRemove = UserUiModel.User(it.uid, it.cell, it.dob, it.email,it.gender,it.id,it.location,it.login,it.name,it.nat,it.phone,it.picture,it.registered)

                    if (userRemove != null){

                        _users.value!!.map { remove(userRemove) }

                    }
                }

            }?.toList()
        } catch (exception: Exception) {

            Timber.e("Error us: ${exception.stackTrace}")
        }

    }

    fun loadUsersNewPage(): List<UserUiModel.User> {

        var listOut = _users.value?.toMutableList() ?: mutableListOf()
        viewModelScope.launch {
            val result = runCatching {
                val response =
                    userRepository.searchUsersNextPage(
                        usersPageNumber,
                        PAGE_LIMIT,
                        SEED
                    )

                if (response.isEmpty()) {
                    return@launch
                }
                if (response.isNotEmpty()) {
                    val list = _users.value?.toMutableList() ?: mutableListOf()

                    //response.map { it.userList.map { itUser-> itUser.toUserUiModel() } }
                    response.map { it.userList.map { itUser-> list.add(itUser.toUserUiModel()) } }
                    _users.value = list
                    listOut = list

                    response.map {
                        println("\nit.toUserPage ${it.userList.map{
                                itUser-> itUser.toUserUiModel()
                        }}")
                    }
                }
                usersPageNumber += 1
            }

            val exception = result.exceptionOrNull()
            if (exception != null && exception !is CancellationException) {

                Timber.e(exception.message.toString())
                Timber.e("Error us: ${exception.stackTrace}")
                Timber.e("ERROR us: ${exception.stackTraceToString()}")

            }
        }
        return listOut
    }

    companion object {
        const val PAGE_LIMIT = 20
        const val LENGHT_SEARCH = 3
        const val SEED = "abc"
    }

}