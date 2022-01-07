package com.android.adevinta


import androidx.lifecycle.*
import com.android.adevinta.models.*
import com.android.adevinta.repository.UserRepository
import com.android.adevinta.uicases.user.UserUiModel
import com.android.adevinta.uicases.user.toUserUiModel
import com.android.adevinta.util.PersistanceStore
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.launch
import timber.log.Timber


class MainActivityViewModel (
    private val userRepository: UserRepository,
    val persistenceStore: PersistanceStore,
    ) : ViewModel() {

    private var usersPageNumber = 0

    private var _users: MutableLiveData<List<UserUiModel.User>> = MutableLiveData(listOf())
    val users: LiveData<List<UserUiModel.User>> = _users

    private var _user: MutableLiveData<UserUiModel.User> = MutableLiveData()
    val user: LiveData<UserUiModel.User> = _user


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

                        // TODO persistence
                        /**persistenceStore.removedUserList(
                            1,
                            UserStore(
                                uid = userRemove.uid,
                                cell= userRemove.cell,
                                dob= com.android.adevinta.util.Dob(userRemove.dob.date,userRemove.dob.age),
                                email= userRemove.email,
                                gender= userRemove.gender,
                                id= com.android.adevinta.util.Id(userRemove.id.name,userRemove.id.value),
                                location= com.android.adevinta.util.Location(userRemove.location.city,userRemove.location.state),
                                login= com.android.adevinta.util.Login(userRemove.login.md5,userRemove.login.password,userRemove.login.salt,userRemove.login.sha1,userRemove.login.sha256,userRemove.login.username,userRemove.login.uuid),
                                name= com.android.adevinta.util.Name(userRemove.name.first,userRemove.name.last),
                                nat = userRemove.nat,
                                picture= com.android.adevinta.util.Picture(userRemove.picture.large),
                                registered= com.android.adevinta.util.Registered(userRemove.registered.age,userRemove.registered.date),
                                phone = userRemove.phone
                               )
                            )**/

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