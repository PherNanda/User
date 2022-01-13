package com.android.adevinta


import androidx.lifecycle.*
import com.android.adevinta.models.*
import com.android.adevinta.repository.UserRepository
import com.android.adevinta.uicases.user.UserUiModel
import com.android.adevinta.uicases.user.toUserUiModel
import com.android.adevinta.util.*
import com.android.adevinta.util.Dob
import com.android.adevinta.util.Id
import com.android.adevinta.util.Location
import com.android.adevinta.util.Login
import com.android.adevinta.util.Name
import com.android.adevinta.util.Picture
import com.android.adevinta.util.Registered
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.launch
import timber.log.Timber


class MainActivityViewModel (
    private val userRepository: UserRepository,
    val persistenceStore: PersistanceStore,
    ) : ViewModel() {

    private var usersPageNumber = 0

    private val _users: MutableLiveData<List<UserUiModel.User>> = MutableLiveData(listOf())
    val users: LiveData<List<UserUiModel.User>> = _users
    val usersList: LiveData<List<UserUiModel.User>> = _users

    private var _user: MutableLiveData<UserUiModel.User> = MutableLiveData()
    val user: LiveData<UserUiModel.User> = _user



    fun search(firstName: String){

        if (firstName.length < LENGTH_SEARCH){
            _users.value = loadUsers(PAGE_LIMIT)
            usersPageNumber = 0
        }else {
            searchUser(firstName)
        }

    }

    fun searchLast(LastName: String){

        if (LastName.length < LENGTH_SEARCH){
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


    fun loadUsers(users: Int): MutableList<UserUiModel.User> {
//UserStore(uid=-8770523008959979010, cell=045-517-97-94, dob=Dob(date=1971-08-14T21:22:10.947Z, age=51), gender=male, id=Id(name=HETU), name=Name(first=Peetu, last=Makinen), location=Location(city=Valtimo, state=Åland), nat=FI, phone=06-827-412, login=Login(md5=f859cde8cbbafce03a18ca48a3f21f72, password=model, username=tinylion816, uuid=c8743efb-e779-479a-b9e9-c74e8d4c7249), email=peetu.makinen@example.com, picture=Picture(large=https://randomuser.me/api/portraits/men/92.jpg), registered=Registered(age=16, date=2006-10-21T14:23:23.220Z))
//User(uid=-4824835609656902906, cell=224-318-5178, dob=Dob(date=1969-06-28T03:12:17.604Z, age=53), email=owen.roy@example.com, gender=male, id=Id(name=), location=Location(city=Windsor, state=Saskatchewan, country=Canada, postcode=T2Y 0T1, street=Street(number=3276, name=Arctic Way)), login=Login(md5=da3e8b981188413a8a2aa4dff1f2f0fc, password=coolman, username=blackmeercat980, uuid=bc052fef-5836-4256-9429-f7593f3fe5a8), name=Name(first=Owen, last=Roy), nat=CA, phone=191-983-6506, picture=Picture(large=https://randomuser.me/api/portraits/men/58.jpg), registered=Registered(age=14, date=2008-03-27T11:44:30.934Z))
val userTest = UserUiModel.User(
    cell = "224-318-5178",
    dob = com.android.adevinta.models.Dob("1969-06-28T03:12:17.604Z",53),
    gender = "male",
    id = com.android.adevinta.models.Id(""),
    name = com.android.adevinta.models.Name(first="Owen", last="Roy"),
    location = com.android.adevinta.models.Location(city="Windsor", state="Saskatchewan", country="Canada", postcode="T2Y0T1", street=Street(number="3276", name="Arctic Way")),
    nat = "CA",
    phone = "191-983-6506",
    email = "owen.roy@example.com",
    login = com.android.adevinta.models.Login(md5="da3e8b981188413a8a2aa4dff1f2f0fc", password="coolman", username="blackmeercat980", uuid="bc052fef-5836-4256-9429-f7593f3fe5a8"),
    picture = com.android.adevinta.models.Picture(large="https://randomuser.me/api/portraits/men/58.jpg"),
    registered = com.android.adevinta.models.Registered(age=14, date="2008-03-27T11:44:30.934Z"),

)
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

                    val usersListStore = getUsersDeleted()

                    if (_users.value != null) {

                       //*start -only for test
                       val newListTest = _users.value!!.toMutableList()
                       newListTest.add(userTest) //add user deleted testing persistance
                       _users.value = newListTest //only for testing persistance
                       //*end -only for test

                            //filtra iguales entre guardado y response recibido del servidor
                           val filteredUsersFromStore = compareServerList((_users.value as MutableList<UserUiModel.User>).toMutableList(),usersListStore)
                           //val filteredUsersFromStore = newListTest.filter { it2-> it.name.first.lowercase() == it2.name.first.lowercase() && it.name.last.lowercase() == it2.name.last.lowercase() }

                            //quita repetidos
                            val deletedUsers = distinctByList(filteredUsersFromStore)
                            //val deletedUsers = filteredUsersFromStore.distinctBy { Pair(it.name.first, it.name.last) }

                            if (deletedUsers.isNotEmpty()){

                                val newListUser = _users.value!!.toMutableList()
                                deletedUsers.map {

                                    for (user in newListUser){

                                        println("remove it  $it \nremove it :: ${it.name}")

                                        //remove from list
                                        newListUser.remove(it)

                                        //actualiza la lista actual
                                        _users.value = newListUser
                                        listOut = newListUser
                                    }

                                }
                            }


                    }
                }
                response.userList.map { println("it.toUserUiModel() ${it.toUserUiModel()}") }
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


    private fun searchUser(nameSearch: String): List<UserUiModel.User> {

        viewModelScope.launch {

            val filteredList = _users.value?.filter { it.name.first.lowercase() == nameSearch.lowercase() }

            if (filteredList != null) {

                if (filteredList.isNotEmpty()){
                    _users.value = filteredList.distinctBy { Pair(it.name.first, it.name.last) }

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
                    _users.value = filteredList.distinctBy { Pair(it.name.first, it.name.last) }
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
                    _users.value = filteredList.distinctBy { Pair(it.name.first, it.name.last) }
                }
            }

        }
        return _users.value?: mutableListOf()
    }

    fun removeUser(name: String, lastName: String) {
        try {

            _users.value = _users.value?.toMutableList()?.apply {

                val userRemoveList = _users.value!!.filter { it.name.first.lowercase() == name.lowercase() && it.name.last == lastName}

                var userRemove: UserUiModel.User
                userRemoveList.map {

                    userRemove = UserUiModel.User(it.uid, it.cell, it.dob, it.email,it.gender,it.id,it.location,it.login,it.name,it.nat,it.phone,it.picture,it.registered)
                    //println("\nuserRemove $userRemove")
                    if (userRemove != null){

                        _users.value!!.map { remove(userRemove) }

                        //save persistance list deleted users
                        persistenceStore.removedUserList(
                            UserStore(
                                uid = userRemove.uid,
                                cell = userRemove.cell,
                                dob = Dob(userRemove.dob.date,userRemove.dob.age),
                                email = userRemove.email,
                                gender = userRemove.gender,
                                id = Id(userRemove.id.name),
                                location = Location(userRemove.location.city,userRemove.location.state,userRemove.location.country,userRemove.location.postcode,userRemove.location.street),
                                login = Login(userRemove.login.md5,userRemove.login.password,userRemove.login.username,userRemove.login.uuid),
                                name = Name(userRemove.name.first,userRemove.name.last),
                                nat = userRemove.nat,
                                picture = Picture(userRemove.picture.large),
                                registered = Registered(userRemove.registered.age,userRemove.registered.date),
                                phone = userRemove.phone
                               )
                            )

                    }
                }

            }?.toList()
        } catch (exception: Exception) {

            Timber.e("Error us: ${exception.stackTrace}")
        }
        //persistenceStore.getUsersDeleted()
    }

    private fun getUsersDeleted(): List<UserStore> {
        return persistenceStore.getUsersDeleted()
    }

    private fun distinctByList(oldList: List<UserUiModel.User>): List<UserUiModel.User> {
        return oldList.distinctBy { Pair(it.name.first, it.name.last) }
    }

    private fun compareServerList(responseListServer: List<UserUiModel.User>,storeDeletedList: List<UserStore>): List<UserUiModel.User> {
        var filteredUsersFromStore: List<UserUiModel.User> = listOf()
        storeDeletedList.map {
            filteredUsersFromStore = responseListServer.filter { it2-> it.name.first.lowercase() == it2.name.first.lowercase() && it.name.last.lowercase() == it2.name.last.lowercase() }
        }
        return filteredUsersFromStore
    }

    companion object {
        const val PAGE_LIMIT = 10
        const val LENGTH_SEARCH = 3
        const val SEED = "abc"
    }

}