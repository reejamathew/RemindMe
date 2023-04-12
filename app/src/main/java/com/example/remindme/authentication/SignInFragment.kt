package com.example.remindme.authentication

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.navigation.findNavController
import com.example.remindme.R
import com.example.remindme.RemindMeConstants
import com.mdev.apsche.database.ReminderDatabase
import com.example.remindme.ReminderActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [SignInFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class SignInFragment : Fragment() {
    var email:String = ""
    var password:String = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_sign_in, container, false);
        val emailTextView = view.findViewById<TextView>(R.id.inputEmailSignIn)
        val passwordTextView = view.findViewById<TextView>(R.id.inputPasswordSignIn)
        val signInButton =  view.findViewById<Button>(R.id.signInScreenSignInButton)
        //intialize database
        val database = ReminderDatabase(requireActivity())

        signInButton.setOnClickListener {
            email = emailTextView.text.toString()
            password = passwordTextView.text.toString()
            if (validateFields()) {
                if(database.checkLogin(email,password)){
                    RemindMeConstants.useremail=email
                    RemindMeConstants.password=password
//                    view.findNavController().navigate(R.id.action_signInFragment_to_reminderFragment)

                    val intent = Intent(context, ReminderActivity::class.java)
                    startActivity(intent)
                }
                else{
                    //email and password doesn't match with database
                    Toast.makeText(requireActivity(), "Invalid Credentials", Toast.LENGTH_SHORT)
                        .show()
                }

            }
        }
            val signUpTextView =  view.findViewById<TextView>(R.id.signUpInSignInTextView)
            signUpTextView.setOnClickListener{
                view.findNavController().navigate(R.id.action_signInFragment_to_signUpFragment)

            }
        // Inflate the layout for this fragment
        return view
    }

    fun validateFields(): Boolean {

        if (email == "" && !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(this@SignInFragment.requireActivity(), "Please enter valid email", Toast.LENGTH_SHORT).show()
            return false
        } else if (password.length<6) {
            Toast.makeText(this@SignInFragment.requireActivity(), "Password should be minimum six characters", Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }
}