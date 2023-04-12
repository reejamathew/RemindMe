package com.mdev.apsche

import android.util.Log.*
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.remindme.R
import com.example.remindme.model.Reminder


/**
 * [RecyclerView.Adapter] that can display a [PlaceholderItem].
 * TODO: Replace the implementation with code for your data type.
 */
class MyReminderRecyclerViewAdapter(
    private val reminder: List<Reminder>
) : RecyclerView.Adapter<MyReminderRecyclerViewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        //inflate view
        val view = LayoutInflater.from(parent.context).inflate(R.layout.fragment_reminder_list, parent, false)
        return ViewHolder(view)

    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        //setting each view value
        val reminderModel = reminder[position]
        holder.title.text = reminderModel.title
        holder.description.text=reminderModel.description
        holder.date.text=reminderModel.dateTime
        var  apartId = reminder[position].key!!

        //click action
        holder.itemView.setOnClickListener{
//            val action = HomeFragmentDirections.actionHomeFragmentToDetailsFragment(apartId)
//            holder.itemView.findNavController().navigate(action)
        }
    }

    override fun getItemCount(): Int {

        return reminder.size
    }


    // Holds the views for adding it to image and text
    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        val title: TextView = itemView.findViewById(R.id.titleView)
        val description: TextView = itemView.findViewById(R.id.descriptionView)
        val date: TextView = itemView.findViewById(R.id.dateView)


    }
}