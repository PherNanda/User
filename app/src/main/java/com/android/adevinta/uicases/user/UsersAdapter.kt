package com.android.adevinta.uicases.user

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.android.adevinta.R
import com.android.adevinta.databinding.ViewUserBinding
import com.android.adevinta.util.updateDataList

class UsersAdapter (private val context: Context,
                    private val itensCart: List<UserUiModel.User>,
                    private val removeItemClickListener: (UserUiModel.User) -> Unit,
) : RecyclerView.Adapter<UsersAdapter.ViewHolder>() {

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

                if(this.name != null){
                    binding.userPhone.text = itensCart[position].phone
                    binding.userFirstName.text = itensCart[position].name.first
                    binding.userLastName.text = itensCart[position].name.last
                    binding.userEmail.text = itensCart[position].email
                }

                binding.userImage.load(itensCart[position].picture.large){
                    error(R.color.placeholder)
                    placeholder(R.color.placeholder)
                }
            }

            with(removeItemClickListener){

                binding.deleteButton.setOnClickListener {
                    removeItemClickListener.invoke(uiModel)
                }
            }

        }
    }

    override fun getItemCount(): Int {
        return itensCart.size
    }

}

class OnScrollListener(
    val layoutManager: LinearLayoutManager,
    val adapter: RecyclerView.Adapter<UsersAdapter.ViewHolder>,
    val dataList: MutableList<Int>
) : RecyclerView.OnScrollListener()
{
    var previousTotal = 0
    var loading = true
    val visibleThreshold = 10
    var firstVisibleItem = 0
    var visibleItemCount = 0
    var totalItemCount = 0

    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)

        visibleItemCount = recyclerView.childCount
        totalItemCount = layoutManager.itemCount
        firstVisibleItem = layoutManager.findFirstVisibleItemPosition()

        if (loading) {
            if (totalItemCount > previousTotal) {
                loading = false
                previousTotal = totalItemCount
            }
        }

        if (!loading && (totalItemCount - visibleItemCount) <= (firstVisibleItem + visibleThreshold)) {
            val initialSize = dataList.size
            updateDataList(dataList)
            val updatedSize = dataList.size
            recyclerView.post { adapter.notifyItemRangeInserted(initialSize, updatedSize) }
            loading = true
        }
    }
}

