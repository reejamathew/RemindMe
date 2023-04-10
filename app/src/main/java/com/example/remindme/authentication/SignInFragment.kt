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
import com.example.remindme.FireBaseDataManagement
import com.example.remindme.R
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
    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_sign_in, container, false);
        val emailTextView = view.findViewById<TextView>(R.id.inputEmailSignIn)
        val passwordTextView = view.findViewById<TextView>(R.id.inputPasswordSignIn)
        val signInButton =  view.findViewById<Button>(R.id.signInScreenSignInButton)
        auth = Firebase.auth

        signInButton.setOnClickListener {
            email = emailTextView.text.toString()
            password = passwordTextView.text.toString()
            if (validateFields()) {
                var signInTask = auth.signInWithEmailAndPassword(email, password)
                signInTask.addOnSuccessListener {
                 //   FireBaseDataManagement().deleteReminder("-NSf7o6nJ8mcBwrTstRO")
          // FireBaseDataManagement().getAllReminders("asd@gmail.com")
//                    FireBaseDataManagement().updateReminder(
//                         requireContext(),"sample3","sample text3","Mar 11, 2016 6:30:00 PM","","qwe@gmail.com","-NSf7o6nJ8mcBwrTstRO");
                  //  FireBaseDataManagement().uploadReminder(
                     //   requireContext(),"sample1","sample text1","Mar 11, 2016 6:30:00 PM","","asd@gmail.com")
                    view.findNavController().navigate(R.id.action_signInFragment_to_reminderFragment)
                }
                signInTask.addOnFailureListener{
                    Log.w("createUserWithEmail:failure", signInTask.exception)
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