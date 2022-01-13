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
    val persistenceStore: PersistenceStore,
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
                    _users.value = list.distinctBy { Pair(it.name.first, it.name.last) }

                    val usersListStore = getUsersDeleted()

                           //filter equals between store and from server
                           val filteredUsersFromStore = compareServerList((_users.value as MutableList<UserUiModel.User>).toMutableList(),usersListStore)
                           //remove repeated
                           val deletedUsers = distinctByList(filteredUsersFromStore)

                            if (deletedUsers.isNotEmpty()){

                                val newListUser = _users.value!!.toMutableList()
                                deletedUsers.map {

                                    for (user in newListUser){
                                        //remove from list ui
                                        newListUser.remove(it)

                                        //update list ui
                                        _users.value = newListUser
                                        listOut = newListUser
                                    }

                                }
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
    }

}