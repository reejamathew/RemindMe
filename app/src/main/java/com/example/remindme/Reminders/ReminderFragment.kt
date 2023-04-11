package com.example.remindme.Reminders

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.remindme.FireBaseDataManagement
import com.example.remindme.R

/**
 * A fragment representing a list of Items.
 */
class ReminderFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_reminder_list, container, false)




        val reminderList = FireBaseDataManagement().getAllReminders("asd2@gmail.com")
        Log.w("reminder list in list",reminderList.toString())
        // Set the adapter
        val itemList: RecyclerView = view.findViewById(R.id.reminderViewList)
        itemList.layoutManager = LinearLayoutManager(view.context);
        val aptAdapter = MyReminderRecyclerViewAdapter(reminderList)
        itemList.adapter =aptAdapter
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        Log.w("on created view","created view")
        super.onViewCreated(view, savedInstanceState)
    }

}