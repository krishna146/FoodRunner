package com.krishna.foodrunner.fragment

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.FragmentActivity

import com.krishna.foodrunner.R
import com.krishna.foodrunner.activity.HomeActivity
import com.krishna.foodrunner.activity.LoginActivity
import com.krishna.foodrunner.activity.RegistrationActivity

/**
 * A simple [Fragment] subclass.
 */
class ProfileFragment : Fragment() {

    lateinit var sharedPreferences: SharedPreferences
    lateinit var txtUsername: TextView
    lateinit var txtUserMobileNumber: TextView
    lateinit var txtUserEmail: TextView
    lateinit var txtUserAddress: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        val view = inflater.inflate(R.layout.fragment_profile, container, false)

        sharedPreferences = (activity as HomeActivity).getSharedPreferences(
            getString(R.string.preferences_file_name),
            Context.MODE_PRIVATE
        )

        txtUsername = view.findViewById(R.id.txtUserName)
        txtUserMobileNumber = view.findViewById(R.id.txtUserMobileNumber)
        txtUserEmail = view.findViewById(R.id.txtUserEmail)
        txtUserAddress = view.findViewById(R.id.txtUserAddress)


        val userName = sharedPreferences.getString("name", "kalpesh")
        val mobileNumber = sharedPreferences.getString("mobileNumber", "+91-7782017779")
        val email = sharedPreferences.getString("email", "kalpesh@gmail.com")
        val address = sharedPreferences.getString("address", "madhepur")
        txtUsername.text = userName
        txtUserMobileNumber.text = "+91-${mobileNumber}"
        txtUserEmail.text = email
        txtUserAddress.text = address







        return view
    }

}
