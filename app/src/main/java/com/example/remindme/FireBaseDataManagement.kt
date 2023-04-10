package com.example.remindme

import android.app.ProgressDialog
import android.content.Context
import android.util.Log
import android.widget.Toast
import com.example.remindme.model.Reminder
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class FireBaseDataManagement {
    private lateinit var database: DatabaseReference
    private lateinit var progressBar: ProgressDialog


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
}