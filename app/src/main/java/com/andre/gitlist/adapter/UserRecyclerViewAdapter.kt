package com.andre.gitlist.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.andre.gitlist.R
import com.andre.gitlist.models.User
import com.bumptech.glide.Glide
import com.google.android.material.textview.MaterialTextView

class UserRecyclerViewAdapter(
    private val context: Context,
    private var userList: List<User>,
    private val onClickListener: (User) -> Unit
) : RecyclerView.Adapter<UserRecyclerViewAdapter.UserViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.card_user, parent, false)
        return UserViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val user = userList[position]
        holder.bind(user)
        holder.itemView.setOnClickListener { onClickListener(user) }
    }

    override fun getItemCount(): Int {
        return userList.size
    }

    inner class UserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(user: User) {
            itemView.findViewById<MaterialTextView>(R.id.cardName).text = user.name
            itemView.findViewById<MaterialTextView>(R.id.cardName).text = user.login
            Glide.with(context).load(user.avatar).into(itemView.findViewById(R.id.cardImage))
        }
    }

    fun updateData(users: List<User>) {
        userList = users
        notifyDataSetChanged()
    }
}

