package com.example.remindme.authentication

import android.Manifest
import android.app.AlertDialog
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.example.remindme.R
import com.example.remindme.model.Reminder
import com.mdev.apsche.database.ReminderDatabase
import java.io.File
import java.io.FileNotFoundException
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

    private val PERMISSIONS_REQUEST_CODE = 123



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
                        if (reminder?.img_location != null) {

                            checkAndRequestPermission()
                        }
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
                Toast.makeText(view.context, "Cancelled", Toast.LENGTH_SHORT).show()
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

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            PERMISSIONS_REQUEST_CODE -> {
                if (grantResults.isNotEmpty() &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED &&
                    grantResults[1] == PackageManager.PERMISSION_GRANTED
                ) {
                    // Permissions granted
                    accessFile()

                } else {
                    // Permissions denied
                    Toast.makeText(requireContext(), "Application do not have permission to access images!", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    // Access the file
    private fun accessFile() {

        // Create a File object from the file path string
        val file = File(reminder.img_location)

        // Check if the file exists
        if (file.exists()) {
            // Create a Bitmap object from the file
            val bitmap = BitmapFactory.decodeFile(file.absolutePath)

            // Set the Bitmap as the image source for the ImageView
            imageView.setImageBitmap(bitmap)
        }
    }

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            // Permission is granted, load and display the image here
            accessFile()
        } else {
            // Permission is denied, show a message to the user
            Toast.makeText(requireContext(), "Permission denied", Toast.LENGTH_SHORT).show()
        }
    }

    private fun checkAndRequestPermission() {
        val permission = Manifest.permission.READ_EXTERNAL_STORAGE
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                permission
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            requestPermissionLauncher.launch(permission)
        } else {
            // Permission is already granted, load and display the image here
            accessFile()
        }
    }


}