package com.sa.alarm.home.reminder.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sa.alarm.R
import com.sa.alarm.db.model.Reminder
import kotlinx.android.synthetic.main.item_reminder.view.*
import kotlinx.android.synthetic.main.item_header.view.*

class MyReminderAdapter(var context :Context ) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    var list : List<Reminder> = ArrayList()
    var TYPE_HEADER=0
    var TYPE_ITEM=1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (viewType == TYPE_HEADER) {
            val layoutView = LayoutInflater.from(context).inflate(R.layout.item_header, parent, false)
            return HeaderViewHolder(layoutView)
        } else if (viewType == TYPE_ITEM) {
            val view = LayoutInflater.from(context).inflate(R.layout.item_reminder, parent, false)
            return MyViewHolder(view)
        }
        else{
            val view = LayoutInflater.from(context).inflate(R.layout.item_reminder, parent, false)
            return MyViewHolder(view)
        }
    }

    fun setData(items: List<Reminder>) {
        list = items
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        if(list.isEmpty()){
            return 0
        }
     return list.size+1
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is HeaderViewHolder) {
            (holder as HeaderViewHolder).itemView.tvHeader.setText(context.resources.getText(R.string.my_reminders))

        } else if (holder is MyViewHolder) {
            val mObject = list.get(position-1)
            holder.itemView.tvEventTitle.setText(mObject.eventTitle)
            holder.itemView.tvDate.setText(mObject.date)
            holder.itemView.tvTime.setText(mObject.time)
        }
    }

    class MyViewHolder(view : View) : RecyclerView.ViewHolder(view) {

    }

    class HeaderViewHolder(view : View) :RecyclerView.ViewHolder(view){

    }

    override fun getItemViewType(position: Int): Int {
        return if (isPositionHeader(position)) TYPE_HEADER else TYPE_ITEM
    }

    private fun isPositionHeader(position: Int): Boolean {
        return position == 0
    }
}