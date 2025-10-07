package com.ar11.mobilecryptowallet.adapter

import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ar11.mobilecryptowallet.R
import com.ar11.mobilecryptowallet.databinding.CardCryptoBinding
import com.ar11.mobilecryptowallet.databinding.CardUsersBinding
import com.ar11.mobilecryptowallet.dto.Cryptos
import com.ar11.mobilecryptowallet.model.UserModel2
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions

interface OnInteractionListenerUser {
    fun onView(user: UserModel2) {}

    fun getTheme(): Drawable? {
        return null
    }
}



class UsersAdapter(
    private val onInteractionListener: OnInteractionListenerUser,
) : ListAdapter<UserModel2, UserViewHolder>(UserDiffCallback()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val binding = CardUsersBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return UserViewHolder(binding, onInteractionListener)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val post = getItem(position)
        holder.bind(post)
    }
}

class UserViewHolder(
    private val binding: CardUsersBinding,
    private val onInteractionListener: OnInteractionListenerUser,
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(user: UserModel2) {
        binding.apply {

            userMail.text = user.email
            userName.text = user.name

            binding.usCFr.background = onInteractionListener.getTheme()

            root.setOnClickListener {
                onInteractionListener.onView(user)
            }
        }
    }
}

class UserDiffCallback : DiffUtil.ItemCallback<UserModel2>() {
    override fun areItemsTheSame(oldItem: UserModel2, newItem: UserModel2): Boolean {
        return oldItem.email == newItem.email
    }

    override fun areContentsTheSame(oldItem: UserModel2, newItem: UserModel2): Boolean {
        return oldItem == newItem
    }
}
