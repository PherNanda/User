package com.android.adevinta.uicases.user

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import coil.load
import com.android.adevinta.R
import com.android.adevinta.databinding.FragmentUserBinding

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class UserFragment : Fragment() {

    private var _binding: FragmentUserBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentUserBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val args = requireArguments().getParcelable<UserUiModel.User>(PARCELABLE_ARGS_USER)


        args?.let { user ->

            binding.imageViewUser.load(user.picture.large){
                error(R.color.placeholder)
                placeholder(R.color.placeholder)
            }

            binding.firstNameUser.text = user.name.first
            binding.lastName.text = user.name.last
            binding.addressUser.text = String.format("${getString(R.string.address)}: ${user.location.street.name}, ${user.location.street.number}" +
                    "\n${getString(R.string.city)}: ${user.location.city} " +
                    "\n${getString(R.string.state)}: ${user.location.state}")
            binding.genderUser.text = String.format("${getString(R.string.gender)}: ${if(user.gender == getString(R.string.gender_female)) getString(R.string.female) else getString(R.string.male)}")
            binding.emailUser.text = String.format("${getString(R.string.email)}: ${user.email}")

            val registeredDate = user.registered.date.split(DELIMITER).firstOrNull()
            binding.dateRegistered.text = String.format("${getString(R.string.date_registered)}: $registeredDate")
        }


        binding.buttonSecond.setOnClickListener {
            findNavController().navigate(R.id.action_SecondFragment_to_FirstFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        const val PARCELABLE_ARGS_USER = "argsUser"
        const val DELIMITER = "T"
    }
}