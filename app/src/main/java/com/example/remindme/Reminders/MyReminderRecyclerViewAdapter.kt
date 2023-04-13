package com.mdev.apsche


import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Log.*
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.remindme.R
import com.example.remindme.Reminders.ReminderFragmentDirections
import com.example.remindme.model.Reminder
import java.io.File
import java.io.FileNotFoundException


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
        if(reminderModel.img_location != ""){
            val imgFile = File(reminderModel.img_location)

            if (imgFile.exists()) {
                val myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath())
                try {
                    val inputStream = holder.image.context.contentResolver.openInputStream(Uri.parse(reminderModel.img_location))
                    val bitmap = BitmapFactory.decodeStream(inputStream)
                    holder.image.setImageBitmap(bitmap)
                } catch (e: FileNotFoundException) {
                    Toast.makeText(holder.itemView.context, e.toString(), Toast.LENGTH_SHORT).show()
                    e.printStackTrace()
                }

            }
        }
        var  reminderId = reminder[position].key!!

        //click action
        holder.itemView.setOnClickListener{
            val action = ReminderFragmentDirections.actionReminderFragmentToReminderDetailFragment(reminderId.toInt())
            holder.itemView.findNavController().navigate(action)
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
        val image: ImageView = itemView.findViewById(R.id.imageView)


    }
}