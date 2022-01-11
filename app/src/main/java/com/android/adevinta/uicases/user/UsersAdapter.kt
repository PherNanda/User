package com.android.adevinta.uicases.user

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.android.adevinta.R
import com.android.adevinta.databinding.ViewUserBinding


class UsersAdapter(
    private val itensCart: List<UserUiModel.User>,
    private val removeUserClickListener: (UserUiModel.User) -> Unit,
    private val viewUserClickListener: (UserUiModel.User) -> Unit,
) : ListAdapter<UserUiModel,UsersAdapter.ViewHolder>(ListDiffUtil) {

    inner class ViewHolder(val binding: ViewUserBinding) : RecyclerView.ViewHolder(binding.root)



    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): UsersAdapter.ViewHolder {
        val binding = ViewUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: UsersAdapter.ViewHolder, position: Int) {

        with(holder){
            val uiModel = itensCart[position]
            with(itensCart[position]){

                binding.userPhone.text = itensCart[position].phone
                binding.userFirstName.text = itensCart[position].name.first
                binding.userLastName.text = itensCart[position].name.last
                binding.userEmail.text = itensCart[position].email

                binding.userImage.load(itensCart[position].picture.large){
                    error(R.color.placeholder)
                    placeholder(R.color.placeholder)
                }
            }

            binding.deleteButton.setOnClickListener {
                removeUserClickListener.invoke(uiModel)
            }

            binding.root.setOnClickListener {
                viewUserClickListener.invoke(uiModel)
            }
        }
    }

    override fun getItemCount(): Int {
        return itensCart.size
    }


    object ListDiffUtil : DiffUtil.ItemCallback<UserUiModel>() {
        override fun areItemsTheSame(oldItem: UserUiModel, newItem: UserUiModel): Boolean {
            return oldItem.uid == newItem.uid
        }

        override fun areContentsTheSame(
            oldItem: UserUiModel,
            newItem: UserUiModel
        ): Boolean {
            return oldItem == newItem
        }
    }

}



