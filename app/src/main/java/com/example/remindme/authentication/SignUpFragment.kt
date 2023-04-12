package com.example.remindme.authentication

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.navigation.findNavController
import com.example.remindme.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.mdev.apsche.database.ReminderDatabase

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [SignUpFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class SignUpFragment : Fragment() {

    var email:String = ""
    var password:String = ""
    var confirmPassword:String = ""




    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_sign_up, container, false);
        val emailTextView = view.findViewById<TextView>(R.id.inputEmailSignUp)
        val passwordTextView = view.findViewById<TextView>(R.id.inputPasswordSignUp)
        val confirmPasswordTextView = view.findViewById<TextView>(R.id.inputConfirmPasswordSignUp)
        //intialize database
        val database = ReminderDatabase(requireActivity())
        val signUpButton =  view.findViewById<Button>(R.id.signUpScreenSignUpButton)

        signUpButton.setOnClickListener {
            email = emailTextView.text.toString()
            password = passwordTextView.text.toString()
            confirmPassword = confirmPasswordTextView.text.toString()
            if(validateFields()) {

                if (!database.checkEmail(email)) {
                    database.insertUser(email,password)
                    Log.d("reached here", "signup")
                    view.findNavController().navigate(R.id.action_signUpFragment_to_signInFragment)
                } else {
                    Toast.makeText(requireActivity(), "Emailid already exists", Toast.LENGTH_SHORT)
                        .show()
                }

            }
        }
        val signInTextview =  view.findViewById<TextView>(R.id.signInInSignUpTextView)
        signInTextview.setOnClickListener{
            view.findNavController().navigate(R.id.action_signUpFragment_to_signInFragment)
        }
        return view
    }
    private fun validateFields(): Boolean {

        if(email.isEmpty()){
            Toast.makeText(this@SignUpFragment.requireActivity(), "Please enter your email id", Toast.LENGTH_SHORT).show()
            return false
        }else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            Toast.makeText(
                this@SignUpFragment.requireActivity(), "Please enter valid email id", Toast.LENGTH_SHORT).show()
            return false
        } else if(password.length<6){
            Toast.makeText(this@SignUpFragment.requireActivity(), "Password should be minimum six characters", Toast.LENGTH_SHORT).show()
            return false
        }else if(confirmPassword.isEmpty()){
            Toast.makeText(this@SignUpFragment.requireActivity(), "Confirm Password should be minimum six characters", Toast.LENGTH_SHORT).show()
            return false
        }
        else if (password.isNotEmpty() && confirmPassword.isNotEmpty() && password != confirmPassword) {
            Toast.makeText(this@SignUpFragment.requireActivity(), "Passwords doesn't match", Toast.LENGTH_SHORT).show()
            return false
        }

        return true
    }


}