package com.example.mindwell

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.mindwell.databinding.ItemUserBinding

data class User(val userId: String, val username: String)

class UserAdapter(
    private val onClick: (User) -> Unit
) : RecyclerView.Adapter<UserAdapter.UserViewHolder>() {

    private val userList = mutableListOf<User>()

    fun submitList(users: List<User>) {
        userList.clear()
        userList.addAll(users)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val binding = ItemUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return UserViewHolder(binding)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val user = userList[position]
        holder.bind(user, onClick)
    }

    override fun getItemCount(): Int = userList.size

    class UserViewHolder(private val binding: ItemUserBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(user: User, onClick: (User) -> Unit) {
            binding.tvUserName.text = user.username
            binding.root.setOnClickListener { onClick(user) }
        }
    }
}
