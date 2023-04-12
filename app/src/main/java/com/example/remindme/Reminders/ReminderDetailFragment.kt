package com.example.remindme.authentication

import android.app.AlertDialog
import android.graphics.BitmapFactory
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.navigation.findNavController
import com.example.remindme.R
import com.example.remindme.Reminders.ReminderActionFragmentDirections
import com.example.remindme.model.Reminder
import com.mdev.apsche.database.ReminderDatabase
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

class ReminderDetailFragment : Fragment() {

    private var reminderId: Int = 0
    private lateinit var title: TextView
    private lateinit var description: TextView
    private lateinit var dateView: TextView
    private lateinit var timeView: TextView
    private lateinit var imageView: ImageView

    private lateinit var reminder: Reminder


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_reminder_detail, container, false)


        return  view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        // Initialize views
        title = view.findViewById(R.id.reminderTitle)
        dateView = view.findViewById(R.id.reminderDate)
        timeView = view.findViewById(R.id.reminderTime)
        description = view.findViewById(R.id.reminderDescription)
        imageView = view.findViewById(R.id.imageView)

        //  Instantiate Database
        val database = ReminderDatabase(requireActivity())

        val args = requireArguments()
        reminderId = args?.getInt("id") ?: 0

        if (reminderId != 0) {
            if(database.getRemindersById(reminderId.toString()).size > 0) {
                reminder = database.getRemindersById(reminderId.toString())[0]
                title.text = reminder.title
                description.text = reminder.description


                // Format date and display it in the respective date text view
                val dateFormatter = SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.US)
                val date = dateFormatter.parse(reminder.dateTime)
                val formattedDate = SimpleDateFormat("dd/MM/yyyy", Locale.US).format(date)
                dateView.text = formattedDate // Update the UI with the formatted date

                // Format time and display it in the respective time text view
                val timeFormatter = SimpleDateFormat("hh:mm a", Locale.US) // Specify the desired format
                val time = SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.US).parse(reminder.dateTime)
                val formattedTime = timeFormatter.format(time)
                timeView.text = formattedTime // Update the UI with the formatted date
                if(reminder.img_location != ""){
                    val imgFile = File(reminder.img_location)

                    if (imgFile.exists()) {
                        val myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath())

                       imageView.setImageBitmap(myBitmap)
                    }
                }
            }
        }

        // Handle delete button click event
        val deleteButton = view.findViewById<Button>(R.id.deleteButton)
        deleteButton.setOnClickListener{
            var builder = AlertDialog.Builder(view.context)
            builder.setMessage("Are you sure you want to delete this item?")
            builder.setPositiveButton("Yes") {_, _ ->
                // Code to Delete
                try {
                    // Code to Delete
                    val result = database.deleteReminder(reminderId.toString())
                    if (result) {
                        Toast.makeText(view.context, "Reminder deleted", Toast.LENGTH_SHORT).show()
                        val action = ReminderDetailFragmentDirections.actionReminderDetailFragmentToReminderFragment()
                        deleteButton.findNavController().navigate(action)
                    } else {
                        Toast.makeText(view.context, "Failed to delete reminder", Toast.LENGTH_SHORT).show()
                    }
                } catch (e: Exception) {
                    Toast.makeText(view.context, "Cancelled", Toast.LENGTH_SHORT).show()
                }
            }
            builder.setNegativeButton("No") { _, _ ->
                Toast.makeText(view.context, "Home", Toast.LENGTH_SHORT).show()
            }
            val dialog = builder.create()
            dialog.show()
        }


        // Handle edit button click event
        val editButton = view.findViewById<Button>(R.id.editButton)
        editButton.setOnClickListener {
            // code to navigate to add item fragment and send data along with it
            if (reminderId != 0) {
                val action = ReminderDetailFragmentDirections.actionReminderDetailFragmentToReminderActionFragment(reminderId)
                editButton.findNavController().navigate(action)
            }
        }
    }
}