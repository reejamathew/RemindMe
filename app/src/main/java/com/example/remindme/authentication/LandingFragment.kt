package com.example.remindme.authentication

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.example.remindme.R

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [LandingFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class LandingFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_landing, container, false)

        //button and actions
        val signInButton =  view.findViewById<Button>(R.id.initialSignIn)
        signInButton.setOnClickListener{
            view.findNavController().navigate(R.id.action_landingFragment_to_signInFragment)

        }
        val signUpButton =  view.findViewById<Button>(R.id.initialSignUp)
        signUpButton.setOnClickListener{
            view.findNavController().navigate(R.id.action_landingFragment_to_signUpFragment)
        }
        return view
    }

}