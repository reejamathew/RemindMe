package com.example.remindme.Reminders

import android.util.Log
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.navigation.findNavController
import com.example.remindme.R


import com.example.remindme.databinding.FragmentReminderBinding
import com.example.remindme.model.Reminder

/**
 * [RecyclerView.Adapter] that can display a [PlaceholderItem].
 * TODO: Replace the implementation with code for your data type.
 */
class MyReminderRecyclerViewAdapter(
    private val reminderList: List<Reminder>
) : RecyclerView.Adapter<MyReminderRecyclerViewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyReminderRecyclerViewAdapter.ViewHolder {

        //inflate view
        val view = LayoutInflater.from(parent.context).inflate(R.layout.fragment_reminder_list, parent, false)
        return MyReminderRecyclerViewAdapter.ViewHolder(view)

    }
    override fun onBindViewHolder(holder: MyReminderRecyclerViewAdapter.ViewHolder, position: Int) {
        //setting view with values
        val reminderModel = reminderList[position]
        holder.title.text = reminderModel.title
        holder.description.text=reminderModel.description
        holder.date.text=reminderModel.dateTime
        var  keyReminder = reminderList[position].key!!
        Log.w("data in adapter",reminderModel.title)

        //list click action
        holder.itemView.setOnClickListener{
//            val action = NoticeFragmentDirections.actionNoticeFragmentToDetailsFragment(apartId)
//            holder.itemView.findNavController().navigate(action)
        }
    }


    override fun getItemCount(): Int {
        return reminderList.size
    }


    // Holds the views for adding it to image and text
    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        val title: TextView = itemView.findViewById(R.id.titleView)
        val description: TextView = itemView.findViewById(R.id.descriptionView)
        val date: TextView = itemView.findViewById(R.id.dateView)


    }


}