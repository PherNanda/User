package com.android.users.uicases.user

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.android.users.R
import com.android.users.databinding.ViewUserBinding

class UserAdapter(
    private val categoryClickListener: (UserUiModel.User) -> Unit,
    private val removeUserClickListener: (UserUiModel.User) -> Unit,
    private val viewUserClickListener: (UserUiModel.User) -> Unit,
) : ListAdapter<UserUiModel.User, UserAdapter.UserViewHolder>(ListDiffUtil) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): UserViewHolder {
        return UserViewHolder(
            binding = ViewUserBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ),
            categoryClickListener = categoryClickListener,
            removeUserClickListener = removeUserClickListener,
            viewUserClickListener = viewUserClickListener
        )
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class UserViewHolder(
        private val binding: ViewUserBinding,
        private val categoryClickListener: (UserUiModel.User) -> Unit,
        private val removeUserClickListener: (UserUiModel.User) -> Unit,
        private val viewUserClickListener: (UserUiModel.User) -> Unit,
    ) : RecyclerView.ViewHolder(binding.root) {
        lateinit var uiModel: UserUiModel.User

        init {
            /**binding.checkBox.setOnCheckedChangeListener { _, checked ->
                if (checked) {

                    categoryClickListener.invoke(uiModel.copy(isChecked = true))
                }

            }**/

            binding.deleteButton.setOnClickListener {
                removeUserClickListener.invoke(uiModel)
            }

            binding.root.setOnClickListener {
                viewUserClickListener.invoke(uiModel)
            }
        }

        fun bind(item: UserUiModel.User) {
            uiModel = item

            binding.userPhone.text = uiModel.phone
            binding.userFirstName.text = uiModel.name.first
            binding.userLastName.text = uiModel.name.last
            binding.userEmail.text = uiModel.email

            binding.userImage.load(uiModel.picture.large){
                error(R.color.placeholder)
                placeholder(R.color.placeholder)
            }
        }

    }

    object ListDiffUtil : DiffUtil.ItemCallback<UserUiModel.User>() {
        override fun areItemsTheSame(
            oldItem: UserUiModel.User,
            newItem: UserUiModel.User
        ): Boolean {
            return oldItem.uid == newItem.uid
        }

        override fun areContentsTheSame(
            oldItem: UserUiModel.User,
            newItem: UserUiModel.User
        ): Boolean {
            return oldItem == newItem
        }
    }
}
