package ru.netology.mydiploma.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import ru.netology.mydiploma.R
import ru.netology.mydiploma.databinding.CardUserBinding
import ru.netology.mydiploma.dto.User


class UserAdapter : ListAdapter<User, UserViewHolder>(UserDiffCallBack()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val binding = CardUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return UserViewHolder(binding)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val user = getItem(position)
        holder.bind(user)
    }

}

class UserViewHolder(
    private val binding: CardUserBinding
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(user: User) {
        with(binding) {
            userName.text = user.name
            Glide.with(avatar)
                .load(user.avatar)
                .placeholder(R.drawable.ic_cloud_download_24)
                .error(R.drawable.ic_person_24)
                .circleCrop()
                .timeout(30_000)
                .into(avatar)
        }
    }
}

class UserDiffCallBack : DiffUtil.ItemCallback<User>() {
    override fun areItemsTheSame(oldItem: User, newItem: User): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: User, newItem: User): Boolean {
        return oldItem == newItem
    }

}