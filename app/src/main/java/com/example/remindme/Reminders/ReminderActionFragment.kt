package com.example.remindme.Reminders

import android.app.Activity
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.navigation.findNavController
import com.example.remindme.R
import com.example.remindme.RemindMeConstants
import com.example.remindme.model.Reminder
import com.google.android.material.textfield.TextInputEditText
import com.mdev.apsche.database.ReminderDatabase
import java.io.File
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

class ReminderActionFragment : Fragment() {
    private lateinit var titleInput: TextInputEditText
    private lateinit var descriptionInput: TextInputEditText
    private lateinit var dateInput: TextInputEditText
    private lateinit var timeInput: TextInputEditText
    private lateinit var addItemButton: Button
    private lateinit var cancelButton: Button


    var myContext: Context? = null
    // Request code for opening the camera intent
    private val REQUEST_IMAGE_CAPTURE = 1

    // UI elements
    private lateinit var inputField: EditText
    private lateinit var openCameraButton: Button
    private var imageURI: String = ""
    private lateinit var dateTime: Date

    private lateinit var reminder: Reminder



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_reminder_action, container, false)

        // Get references to the UI elements
        openCameraButton = view.findViewById(R.id.open_camera_button)

        // Set a click listener for the "Open Camera" button
        openCameraButton.setOnClickListener {
            // When the button is clicked, open the camera
            dispatchTakePictureIntent()
        }

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize views
        titleInput = view.findViewById(R.id.titleInput)
        descriptionInput = view.findViewById(R.id.descriptionInput)
        dateInput = view.findViewById(R.id.dateEditText)
        timeInput = view.findViewById(R.id.timeEditText)
        addItemButton = view.findViewById(R.id.submitButton)
        cancelButton = view.findViewById(R.id.cancelButton)

        // Initialize database
        val database = ReminderDatabase(requireActivity())

        // Get argument sent from another fragment
        val args = requireArguments()
        val reminderId = args?.getInt("id") ?: 0

        if (reminderId != 0) {
            if(database.getRemindersById(reminderId.toString()).size > 0) {
                reminder = database.getRemindersById(reminderId.toString())[0]
                titleInput.setText(reminder.title)
                descriptionInput.setText(reminder.description)


                val dateFormatter = SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.US)
                val date = dateFormatter.parse(reminder.dateTime)
                val formattedDate = SimpleDateFormat("dd/MM/yyyy", Locale.US).format(date)
                dateInput.setText(formattedDate) // Update the UI with the formatted date

                // set dateTime to global dateTime variable
                val inputFormat = SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.getDefault())
                dateTime = inputFormat.parse(reminder.dateTime)

                // set image location to global image location
                imageURI = reminder.img_location


                val timeFormatter = SimpleDateFormat("hh:mm a", Locale.US) // Specify the desired format
                val time = SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.US).parse(reminder.dateTime)
                val formattedTime = timeFormatter.format(time)
                timeInput.setText(formattedTime) // Update the UI with the formatted date
//                if(reminder.img_location != ""){
//                    val imgFile = File(reminder.img_location)
//
//                    if (imgFile.exists()) {
//                        val myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath())
//
//                        imageView.setImageBitmap(myBitmap)
//                    }
//                }
            }
        }



        // Set up date picker
        dateInput.setOnClickListener {
            showDatePickerDialog()
        }

        // Set up time picker
        timeInput.setOnClickListener {
            showTimePickerDialog()
        }

        // Set up cancel click listener
        cancelButton.setOnClickListener {
            cancel()
        }

        // Set up button click listener
        addItemButton.setOnClickListener {
            // Get input values
            val title = titleInput.text.toString()
            val description = descriptionInput.text.toString()
            val date = dateInput.text.toString()
            val time = timeInput.text.toString()

            // Validate input
            if (title.isEmpty() || description.isEmpty() || date.isEmpty() || time.isEmpty()) {
                Toast.makeText(requireContext(), "Please fill all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Save item to database

            if (reminderId > 0 && reminder != null) {
                if(database.updateReminder(reminderId.toString(), title, description, imageURI, dateTime.toString(), RemindMeConstants.useremail)){
                    cancel()
                } else {
                    Toast.makeText(requireContext(), "Error updating reminder!", Toast.LENGTH_SHORT).show()
                }
            } else {
                if(database.insertReminder(title, description, dateTime.toString(), imageURI, RemindMeConstants.useremail)){
                    cancel()
                } else {
                    Toast.makeText(requireContext(), "Error adding reminder!", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    // Reset all input fields
    private fun resetFields() {
        titleInput.text = null
        descriptionInput.text = null
        dateInput.text = null
        timeInput.text = null
        imageURI = ""
    }

    private fun showDatePickerDialog() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(requireContext(), { _, y, m, d ->

            calendar.set(Calendar.YEAR, y)
            calendar.set(Calendar.MONTH, m)
            calendar.set(Calendar.DAY_OF_MONTH, d)
            // Save the date and time in the same variable
            dateTime = calendar.time

            // Update UI
            val date = "${d}/${m + 1}/${y}"
            dateInput.setText(date)
        }, year, month, dayOfMonth)

        datePickerDialog.show()
    }

    private fun showTimePickerDialog() {
        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)

        val timePickerDialog = TimePickerDialog(requireContext(), { _, h, m ->
            // Save the date and time in the same variable
            calendar.set(Calendar.HOUR_OF_DAY, h)
            calendar.set(Calendar.MINUTE, m)

            dateTime = calendar.time

            // Update UI
            val time = "${h}:${m}"
            timeInput.setText(time)
        }, hour, minute, true)

        timePickerDialog.show()
    }

    // Function to open the camera
    private fun dispatchTakePictureIntent() {
        // Create a new intent to open the camera app
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            // Make sure there's a camera app to handle the intent
            takePictureIntent.resolveActivity(requireActivity().packageManager)?.also {
                // Start the camera activity and wait for a result
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
            }
        }
    }
    // Handle the camera intent result
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Check if the result is from the camera intent and is successful
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {
            // Get the captured image URI
            val imageUri = data?.data
            // Get the image name from the input field
            val imageName = inputField.text.toString()

            // Save the image URI to the database
            imageURI = imageUri.toString()
        }
    }

    private fun cancel() {
        resetFields()
        val action = ReminderActionFragmentDirections.actionReminderActionFragmentToReminderFragment()
        cancelButton.findNavController().navigate(action)
    }


}