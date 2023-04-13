package com.example.remindme.Reminders


import android.Manifest
import android.app.*
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.net.Uri
import android.opengl.Visibility
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.example.remindme.MainActivity
import com.example.remindme.R
import com.example.remindme.RemindMeConstants
import com.example.remindme.model.Reminder
import com.google.android.material.textfield.TextInputEditText
import com.mdev.apsche.database.ReminderDatabase
import java.io.File
import java.io.FileNotFoundException
import java.io.IOException
import java.io.InputStream
import java.net.MalformedURLException
import java.net.URL
import java.text.SimpleDateFormat
import java.util.*


class ReminderActionFragment : Fragment() {
    private lateinit var titleInput: TextInputEditText
    private lateinit var descriptionInput: TextInputEditText
    private lateinit var dateInput: TextInputEditText
    private lateinit var timeInput: TextInputEditText
    private lateinit var addItemButton: Button
    private lateinit var cancelButton: Button
    private lateinit var pickImageButton: Button
    private lateinit var imageView: ImageView
    private lateinit var imageContainer: LinearLayout


    var myContext: Context? = null
    // Request code for opening the camera intent
    private val REQUEST_IMAGE_CAPTURE = 1

    private val PERMISSIONS_REQUEST_CODE = 123

    // Define the request code
    private val PICK_IMAGE_REQUEST = 1

    // UI elements
    private lateinit var inputField: EditText
    private lateinit var openCameraButton: Button
    private var imageURI: String = ""
    private lateinit var dateTime: Date

    private lateinit var reminder: Reminder

    private val calendar = Calendar.getInstance()



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_reminder_action, container, false)
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
        pickImageButton = view.findViewById(R.id.pickImageButton)
        imageView = view.findViewById(R.id.imageView)
        imageContainer = view.findViewById(R.id.imageContainer)

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

                if (reminder?.img_location != null) {

                    val imgFile = File(reminder.img_location)
                    imageURI = reminder.img_location

                    if (imgFile.exists()) {
                        checkAndRequestPermission()
                        imageContainer.visibility = View.VISIBLE
                    }
                }


                val timeFormatter = SimpleDateFormat("hh:mm a", Locale.US) // Specify the desired format
                val time = SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.US).parse(reminder.dateTime)
                val formattedTime = timeFormatter.format(time)
                timeInput.setText(formattedTime) // Update the UI with the formatted date
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

        // Setup image picker
        pickImageButton.setOnClickListener {
            launchImagePicker()
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
                    // Create notification
                    val dateTimeFormatter = SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.getDefault())
                    val dateTime = dateTimeFormatter.parse(reminder.dateTime)
                    val timestamp = dateTime.time
                    createNotification(title, description, timestamp, view.context)
                } else {
                    Toast.makeText(requireContext(), "Error updating reminder!", Toast.LENGTH_SHORT).show()
                }
            } else {
                if(database.insertReminder(title, description, dateTime.toString(), imageURI, RemindMeConstants.useremail)){
                    val dateTimeFormatter = SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.getDefault())
                    val dateTime = dateTimeFormatter.parse(dateTime.toString())
                    val timestamp = dateTime.time
                    createNotification(title, description, timestamp, view.context)
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
        imageView.setImageURI(null)
        imageContainer.visibility = View.INVISIBLE
    }

    private fun showDatePickerDialog() {
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

    // Create a function to launch the image picker
    private fun launchImagePicker() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, PICK_IMAGE_REQUEST)
    }

    // Override the onActivityResult function to get the result
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null) {
            // Get the URI of the selected image
            val imageUri: Uri? = data.data
            val imagePath = imageUri?.let { getRealPathFromURI(requireContext(), it) }
            imageContainer.visibility = View.VISIBLE
            imageView.setImageURI(imageUri)
            imageURI = imagePath.toString()
        }
    }

    // Helper function to get the real path of an image URI
    private fun getRealPathFromURI(context: Context, uri: Uri): String? {
        val cursor = context.contentResolver.query(uri, null, null, null, null)
        cursor?.let {
            if (it.moveToFirst()) {
                val columnIndex = it.getColumnIndex(MediaStore.Images.Media.DATA)
                if (columnIndex >= 0) {
                    val path = it.getString(columnIndex)
                    cursor.close()
                    return path
                }
            }
            cursor.close()
        }
        return null
    }



    private fun cancel() {
        resetFields()
        val action = ReminderActionFragmentDirections.actionReminderActionFragmentToReminderFragment()
        cancelButton.findNavController().navigate(action)
    }

    private fun createNotification(title: String, message: String, timestamp: Long, context: Context) {
        // Get the NotificationManager
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // Create an intent that will be triggered when the user taps on the notification
        val intent = Intent(context, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        val pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_IMMUTABLE)

        // Create the notification
        val builder = NotificationCompat.Builder(context, "default")
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle(title)
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .setWhen(timestamp)

        // Show the notification
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel("default", "Default", NotificationManager.IMPORTANCE_DEFAULT)
            notificationManager.createNotificationChannel(channel)
        }
        notificationManager.notify(0, builder.build())
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