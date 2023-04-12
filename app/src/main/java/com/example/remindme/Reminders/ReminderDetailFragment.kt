package com.example.remindme.authentication

import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.navigation.findNavController
import com.example.remindme.R

class ReminderDetailFragment : Fragment() {


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

        // Handle delete button click event
        val deleteButton = view.findViewById<Button>(R.id.deleteButton)
        deleteButton.setOnClickListener{
            var builder = AlertDialog.Builder(view.context)
            builder.setMessage("Are you sure you want to delete this item?")
            builder.setPositiveButton("Yes") {_, _ ->
                // Code to Delete
                Toast.makeText(view.context, "Home", Toast.LENGTH_SHORT).show()
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
            var id = 1

            // code to navigate to add item fragment and send data along with it
            val action = ReminderDetailFragmentDirections.actionReminderDetailFragmentToReminderActionFragment(id)
            editButton.findNavController().navigate(action)
        }
    }
}