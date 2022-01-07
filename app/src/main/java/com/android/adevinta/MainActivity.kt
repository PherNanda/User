package com.android.adevinta

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import android.view.Menu
import android.view.MenuItem
import androidx.core.os.bundleOf
import androidx.core.widget.doAfterTextChanged
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.adevinta.databinding.ActivityMainBinding
import com.android.adevinta.uicases.user.UserFragment
import com.android.adevinta.uicases.user.UserUiModel
import com.android.adevinta.uicases.user.UsersAdapter
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding

    private val viewModel by viewModel<MainActivityViewModel>()

    private lateinit var user: List<UserUiModel.User>
    private lateinit var listAdapter: UsersAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        loadUser()

        val navController = findNavController(R.id.nav_host_fragment_content_main)
        appBarConfiguration = AppBarConfiguration(navController.graph)
        setupActionBarWithNavController(navController, appBarConfiguration)


        //search first name
        val inputName = binding.usersInput.text
        binding.usersLayout.editText?.text = inputName

        var value: String
        binding.usersInput.doAfterTextChanged { input ->

            if(input != null && input.isNotEmpty()){
                value = binding.usersInput.text.toString()
               
                if(value.isNotEmpty() && value.length > MIN_SEARCH) {
                    viewModel.search(value.lowercase())

                }
                if(value.isNotEmpty() && value.length < MIN_SEARCH) {
                    viewModel.search(value)

                }
            }
        }

        //search last name
        val inputLastName = binding.lastNameInput.text
        binding.lastLayout.editText?.text = inputLastName

        var valueLast: String
        binding.lastNameInput.doAfterTextChanged { input ->

            if(input != null && input.isNotEmpty()){
                valueLast = binding.lastNameInput.text.toString()

                if(valueLast.isNotEmpty() && valueLast.length > MIN_SEARCH) {
                    viewModel.searchLast(valueLast)

                }
                if(valueLast.isNotEmpty() && valueLast.length < MIN_SEARCH) {
                    viewModel.searchLast(valueLast)

                }
            }
        }

        //search email
        val inputEmail = binding.emailInput.text
        binding.emailLayout.editText?.text = inputEmail

        var valueEmail: String
        binding.emailInput.doAfterTextChanged { input ->

            if(input != null && input.isNotEmpty()){
                valueEmail = binding.emailInput.text.toString()

                if(valueEmail.isNotEmpty() && valueEmail.length > MIN_SEARCH_EMAIL) {
                    viewModel.searchEmail(valueEmail)

                }
                if(valueEmail.isNotEmpty() && valueEmail.length < MIN_SEARCH_EMAIL) {
                    viewModel.searchEmail(valueEmail)

                }
            }
        }


        val dataList = mutableListOf<Int>()


        viewModel.users.observe(this){
            println("user main ${it.size}")
            println("user main two ${it.map { it2-> it2 }}")
            listAdapter =
                UsersAdapter(
                    context = this,
                    itensCart = it,
                    removeUserClickListener = { userItem ->
                        userItem.name.let {
                            viewModel.removeUser(name = userItem.name.first, lastName = userItem.name.last)
                            }
                        },
                     viewUserClickListener = {
                         bundleOf(UserFragment.PARCELABLE_ARGS_USER to it).apply {
                             navController.navigate(
                             R.id.action_FirstFragment_to_SecondFragment,
                             this
                         )
                         it
                     }
                    }
                )



            binding.recyclerViewUsersMain.apply {
                layoutManager = LinearLayoutManager(
                    context,
                    LinearLayoutManager.VERTICAL,
                    false
                )
                adapter = listAdapter
            }
            return@observe
        }


    }

    private fun loadUser(){
        user = viewModel.loadUsers(MAX_LIMIT)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration)
                || super.onSupportNavigateUp()
    }

    companion object {
        const val MAX_LIMIT = 20
        const val MIN_SEARCH = 3
        const val MIN_SEARCH_EMAIL = 10
    }

}