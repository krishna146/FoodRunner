package com.krishna.foodrunner.activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.krishna.foodrunner.R
import com.krishna.foodrunner.util.ConnectionManager
import org.json.JSONException
import org.json.JSONObject

class LoginActivity : AppCompatActivity() {

    lateinit var etMobileNumber: EditText
    lateinit var etPassword: EditText
    lateinit var btnLogin: Button
    lateinit var txtForgetPassword: TextView
    lateinit var txtSignUp: TextView
    lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedPreferences =
            getSharedPreferences(getString(R.string.preferences_file_name), (Context.MODE_PRIVATE))
        val isLoggedIn = sharedPreferences.getBoolean("isLoggedIn", false)
        setContentView(R.layout.activity_login)
        if (isLoggedIn) {
            val intent = Intent(this@LoginActivity, HomeActivity::class.java)
            startActivity(intent)
            finish()
        }

        etMobileNumber = findViewById(R.id.etMobileNumber)
        etPassword = findViewById(R.id.etPassword)
        btnLogin = findViewById(R.id.btnLogin)
        txtForgetPassword = findViewById(R.id.txtForgetPassword)
        txtSignUp = findViewById(R.id.txtSignUp)

        txtSignUp.setOnClickListener {
            val intent = Intent(this@LoginActivity, RegistrationActivity::class.java)
            startActivity(intent)
        }

        txtForgetPassword.setOnClickListener {
            val intent = Intent(this@LoginActivity, ForgetPasswordActivity::class.java)
            startActivity(intent)
        }

        btnLogin.setOnClickListener {
            if (ConnectionManager().checkConnectivity(this@LoginActivity)) {
                val queue = Volley.newRequestQueue(this@LoginActivity)
                val url = "http://13.235.250.119/v2/login/fetch_result/"
                val jsonParams = JSONObject()
                jsonParams.put("mobile_number", etMobileNumber.text)
                jsonParams.put("password", etPassword.text)

                val jsonRequest =
                    object :
                        JsonObjectRequest(Request.Method.POST, url, jsonParams, Response.Listener {
                            try {

                                val jsonObject = it.getJSONObject("data")
                                val success = jsonObject.getBoolean("success")


                                if (success) {
                                    val personData = jsonObject.getJSONObject("data")
                                    val name = personData.getString("name")
                                    val mobileNumber = personData.getString("mobile_number")
                                    val email = personData.getString("email")
                                    val address = personData.getString("address")
                                    savedPreference(name, mobileNumber, email, address)

                                    val intent =
                                        Intent(this@LoginActivity, HomeActivity::class.java)
                                    startActivity(intent)

                                } else {
                                    val errorMessage = jsonObject.getString("errorMessage")
                                    Toast.makeText(
                                        this@LoginActivity,
                                        errorMessage,
                                        Toast.LENGTH_SHORT
                                    )
                                        .show()
                                }
                            } catch (e: JSONException) {
                                Toast.makeText(
                                    this@LoginActivity,
                                    "some unexpected erroer occured",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }


                        }, Response.ErrorListener {

                            Toast.makeText(
                                this@LoginActivity,
                                "Volley error occured",
                                Toast.LENGTH_SHORT
                            ).show()

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
                Toast.makeText(this@LoginActivity, "No Inetrnet Connection", Toast.LENGTH_SHORT)
                    .show()


            }
        }


    }

    override fun onPause() {
        super.onPause()
        finish()
    }

    fun savedPreference(name: String, mobileNumber: String, email: String, address: String) {
        sharedPreferences.edit().putBoolean("isLoggedIn", true).apply()
        sharedPreferences.edit().putString("name", name).apply()
        sharedPreferences.edit().putString("mobileNumber", mobileNumber).apply()
        sharedPreferences.edit().putString("email", email).apply()
        sharedPreferences.edit().putString("address", address).apply()
    }
}
