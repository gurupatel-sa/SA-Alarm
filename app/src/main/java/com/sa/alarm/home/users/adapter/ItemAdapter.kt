package com.sa.alarm.home.users.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProviders
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.sa.alarm.R
import com.sa.alarm.auth.register.model.User
import com.sa.alarm.home.users.UsersViewModel
import kotlinx.android.synthetic.main.item_user.view.*

class ItemAdapter(var context: Context): PagedListAdapter<User, ItemAdapter.ItemViewHolder>(diffCallback)
{
    var userViewModel :UsersViewModel?
    init {
        userViewModel = ViewModelProviders.of(context as FragmentActivity).get(UsersViewModel::class.java)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {

        val view = LayoutInflater.from(context).inflate(R.layout.item_user, parent, false)
        return ItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val item = getItem(position)
        holder.itemView.tvUserName.setText(item?.displayName)
        Glide.with(context)
            .load(item?.photoUrl)
            .into(holder.itemView.civUserrofile)

        holder.itemView.btnFollow.setOnClickListener {
            userViewModel?.setFollowData(item?.uid.toString())
            holder.itemView.btnFollow.visibility =View.GONE
            holder.itemView.btnFollowing.visibility =View.VISIBLE
        }

        holder.itemView.btnFollowing.setOnClickListener {
            holder.itemView.btnFollowing.visibility =View.GONE
            holder.itemView.btnFollow.visibility =View.VISIBLE
        }
    }

    class ItemViewHolder(view :View) : RecyclerView.ViewHolder(view){

    }

    companion object {

        //compares adapter data
        private val diffCallback = object : DiffUtil.ItemCallback<User>() {
            override fun areItemsTheSame(oldItem: User, newItem: User): Boolean =
                oldItem.uid == newItem.uid


            override fun areContentsTheSame(oldItem: User, newItem: User): Boolean =
                oldItem == newItem
        }
    }
}