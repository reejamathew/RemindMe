package com.example.remindme

import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.navigation.findNavController
import androidx.navigation.ui.onNavDestinationSelected
import com.google.android.material.appbar.MaterialToolbar

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        RemindMeConstants.useremail=""
        RemindMeConstants.password=""
        RemindMeConstants.showMenu=false
        var toolbar =  findViewById<MaterialToolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        print("on create")
        if(RemindMeConstants.showMenu) {

            menuInflater.inflate(R.menu.menu_toolbar, menu)
        }
        return super.onCreateOptionsMenu(menu)
    }



    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // when any menuitem clicked
        val navController = findNavController(R.id.nav_host_fragment)
        showPopup()
        return  item.onNavDestinationSelected(navController) || super.onOptionsItemSelected(item)

    }
    //popup to show logout
    private fun showPopup() {
        val alert: android.app.AlertDialog.Builder = android.app.AlertDialog.Builder(this@MainActivity)
        alert.setMessage("Are you sure you want to Logout?")
            .setPositiveButton("Logout", DialogInterface.OnClickListener { dialog, which ->
                logOut() // Last step. Logout function
            }).setNegativeButton("Cancel", null)
        val alert1: android.app.AlertDialog? = alert.create()
        if (alert1 != null) {
            alert1.show()
        }
    }
    //logging out
    private fun logOut(){
        val i = Intent(this, MainActivity::class.java)
// set the new task and clear flags
        i.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(i)
    }
}