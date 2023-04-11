package com.example.remindme

import android.app.ProgressDialog
import android.content.ContentValues.TAG
import android.content.Context
import android.util.Log
import com.example.remindme.model.Reminder
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase


class FireBaseDataManagement {
    private lateinit var database: DatabaseReference
    private lateinit var progressBar: ProgressDialog


    // Function to create a reminder
    fun uploadReminder(
        context: Context,
        title: String,
        description: String,
        date: String,
        imageFileUri: String,
        emailId: String
    ) {

        database = Firebase.database.reference
        val reminder = Reminder(title, description,date,imageFileUri,emailId)

        val key = database.child("reminders").push().key
        if (key == null) {
            Log.w("error", "Couldn't get push key for posts")
            return
        }

        val childUpdates = hashMapOf<String, Any>(
            "/reminders/$key" to reminder
        )

        database.updateChildren(childUpdates)

    }

    // Function to get all reminders
    fun getAllReminders(email:String): ArrayList<Reminder> {
        database = Firebase.database.reference.child("reminders");
        val reminders = arrayListOf<Reminder>()

        database.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
                //   Toast.makeText(applicationContext,"Yolunda gitmeyen şeyler oldu..", Toast.LENGTH_LONG).show()
            }

            override fun onDataChange(snapshot: DataSnapshot) {

                for (e in snapshot.children) {

                    val reminder = e.getValue(Reminder::class.java)
                    Log.w("reminder key",e.key.toString())
                    if (reminder != null) {
                        if(reminder.email == email){
                            reminders.add(reminder!!)
                            Log.w("data",reminder.email)
                        }

                    }



                }

            }
        })


        return reminders
    }

    // Function to update specific reminder
    fun updateReminder(
        context: Context,
        title: String,
        description: String,
        date: String,
        imageFileUri: String,
        emailId: String,key:String
    ) {

        database = Firebase.database.reference
        val reminder = Reminder(title, description,date,imageFileUri,emailId)

        database.child("reminders").child(key).setValue(reminder)
            .addOnSuccessListener{

                Log.w("on success","success")
            }
            .addOnFailureListener{
                Log.w("on failure","failure")
            }

    }

    // Function to delete specific reminder
    fun deleteReminder(key:String){
        val ref = FirebaseDatabase.getInstance().reference
        val applesQuery = ref.child("reminders").child(key)

        applesQuery.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (appleSnapshot in dataSnapshot.children) {
                    appleSnapshot.ref.removeValue()
                    Log.e("success","success")
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.e("cancelled", "onCancelled", databaseError.toException())
            }
        })
    }

    // Function to get specific Reminder through Id
    fun getReminderById(reminderId: String, callback: (Reminder?) -> Unit) {
        val databaseReference = Firebase.database.reference.child("reminders").child(reminderId)

        databaseReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val reminder = snapshot.getValue(Reminder::class.java)
                // Return the reminder to the callback function
                callback(reminder)
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle the error here
                Log.e(TAG, "Error getting reminder from Firebase: ${error.message}")
                callback(null)
            }
        })
    }

}