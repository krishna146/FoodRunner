package com.krishna.foodrunner.activity

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.krishna.foodrunner.R
import com.krishna.foodrunner.util.ConnectionManager
import org.json.JSONObject


class RegistrationActivity : AppCompatActivity() {

    lateinit var etName: EditText
    lateinit var etEmail: EditText
    lateinit var etMobileNumber: EditText
    lateinit var etDeliveryAddress: EditText
    lateinit var etPassword: EditText
    lateinit var etConfirmPassword: EditText
    lateinit var btnRegister: Button
    lateinit var toolbar: androidx.appcompat.widget.Toolbar
    lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_registration)
        sharedPreferences = getSharedPreferences(getString(R.string.preferences_file_name),Context.MODE_PRIVATE)


        etName = findViewById(R.id.etName)
        etEmail = findViewById(R.id.etEmail)
        etMobileNumber = findViewById(R.id.etMobileNumber)
        etDeliveryAddress = findViewById(R.id.etDeliveryAddress)
        etPassword = findViewById(R.id.etPassword)
        etConfirmPassword = findViewById(R.id.etConfirmPassword)
        btnRegister = findViewById(R.id.btnRegister)
        toolbar = findViewById(R.id.toolbar)

        setSupportActionBar(toolbar)
        supportActionBar?.title = "Register Yourself"
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val queue = Volley.newRequestQueue(this@RegistrationActivity)
        val url = "http://13.235.250.119/v2/register/fetch_result"

        val jsonParams = JSONObject()


        btnRegister.setOnClickListener {


            if (ConnectionManager().checkConnectivity(this@RegistrationActivity)) {

                jsonParams.put("name", etName.text)
                jsonParams.put("mobile_number", etMobileNumber.text)
                jsonParams.put("password", etPassword.text)
                jsonParams.put("address", etDeliveryAddress.text)
                jsonParams.put("email", etEmail.text)

                val jsonRequest =
                    object :
                        JsonObjectRequest(Request.Method.POST, url, jsonParams, Response.Listener {
                            //here we will handle the response
                            val jsonObject = it.getJSONObject("data")
                            val success = jsonObject.getBoolean("success")

                            if (success) {

                                val personData = jsonObject.getJSONObject("data")
                                val name = personData.getString("name")
                                val mobileNumber = personData.getString("mobile_number")
                                val email = personData.getString("email")
                                val address = personData.getString("address")

                                val intent =
                                    Intent(this@RegistrationActivity, HomeActivity::class.java)
                                savedPreference(name, mobileNumber, email, address)
                                startActivity(intent)
                                finish()

                            } else {
                                val errorMessage = jsonObject.getString("errorMessage")

                                Toast.makeText(
                                    this@RegistrationActivity,
                                    errorMessage,
                                    Toast.LENGTH_SHORT
                                )
                                    .show()
                            }


                        }, Response.ErrorListener {
                            //here we will handle the errors
                            Toast.makeText(this@RegistrationActivity, "Volley error occured", Toast.LENGTH_SHORT).show()


                        }) {
                        override fun getHeaders(): MutableMap<String, String> {
                            val headers = HashMap<String, String>()
                            headers["Content-type"] = "application/json"
                            headers["token"] = "0f3097da6dee50"
                            return headers
                        }

                    }
                queue.add(jsonRequest)
            } else {

                Toast.makeText(
                    this@RegistrationActivity,
                    "No Internet Connection",
                    Toast.LENGTH_SHORT
                ).show()

            }

        }


    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
    fun savedPreference(name: String, mobileNumber: String, email: String, address: String) {
        sharedPreferences.edit().putBoolean("isLoggedIn", true).apply()
        sharedPreferences.edit().putString("name", name).apply()
        sharedPreferences.edit().putString("mobileNumber", mobileNumber).apply()
        sharedPreferences.edit().putString("email", email).apply()
        sharedPreferences.edit().putString("address", address).apply()
    }

}


