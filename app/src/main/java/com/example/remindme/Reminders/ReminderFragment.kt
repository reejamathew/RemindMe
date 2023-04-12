package com.example.remindme.Reminders

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import com.example.remindme.R
import com.example.remindme.RemindMeConstants
import com.example.remindme.authentication.ReminderDetailFragmentDirections
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.mdev.apsche.MyReminderRecyclerViewAdapter
import com.mdev.apsche.database.ReminderDatabase

/**
 * A fragment representing a list of Items.
 */
class ReminderFragment : Fragment() {

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_reminder, container, false)

        RemindMeConstants.showMenu = true
        val activity = activity as AppCompatActivity?
        if (activity != null) {
            activity.invalidateOptionsMenu()
        }


        val database = ReminderDatabase(requireActivity())
        val arrayList= database.getReminderDetails(RemindMeConstants.useremail)

        Log.w("1","before");
        // Set the adapter
        val itemList: RecyclerView = view.findViewById(R.id.reminderViewList)
        Log.w("2","before");
        itemList.layoutManager = LinearLayoutManager(view.context);
        Log.w("3","before");
        val aptAdapter = MyReminderRecyclerViewAdapter(arrayList)
        Log.w("4","before");
        itemList.adapter =aptAdapter
        Log.w("5","before");
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        Log.w("on created view","created view")
        super.onViewCreated(view, savedInstanceState)
        val floatingAddBtn = view.findViewById<FloatingActionButton>(R.id.addNew)
        floatingAddBtn.setOnClickListener{
            // code to navigate to add item fragment and send data along with it
            val action = ReminderFragmentDirections.actionReminderFragmentToReminderActionFragment()
            floatingAddBtn.findNavController().navigate(action)
        }
    }

}