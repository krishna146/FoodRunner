package com.krishna.foodrunner.activity

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.opengl.Visibility
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.krishna.foodrunner.R
import com.krishna.foodrunner.util.ConnectionManager
import org.json.JSONException
import org.json.JSONObject


class ForgetPasswordActivity : AppCompatActivity() {
    lateinit var etMobileNumber: EditText
    lateinit var etEmail: EditText
    lateinit var btnNext: Button
    lateinit var progrssLayout: RelativeLayout
    lateinit var progressBar: ProgressBar


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_forget_password)

        progrssLayout = findViewById(R.id.progressLayout)
        progressBar = findViewById(R.id.progressBar)
        progrssLayout.visibility = View.GONE
        etMobileNumber = findViewById(R.id.etMobileNumber)
        etEmail = findViewById(R.id.etEmail)
        btnNext = findViewById(R.id.btnNext)
        val queue = Volley.newRequestQueue(this@ForgetPasswordActivity)
        val url = "http://13.235.250.119/v2/forgot_password/fetch_result"
        val jsonParams = JSONObject()
        btnNext.setOnClickListener {

            jsonParams.put("mobile_number", etMobileNumber.text)
            jsonParams.put("email", etEmail.text)
            if (ConnectionManager().checkConnectivity(this@ForgetPasswordActivity)) {

                val jsonRequest =
                    @RequiresApi(Build.VERSION_CODES.M)
                    object :
                        JsonObjectRequest(Request.Method.POST, url, jsonParams, Response.Listener {
                            try {

                                val jsonObject = it.getJSONObject("data")
                                val success = jsonObject.getBoolean("success")
                                if (success) {
                                    val firstTry = jsonObject.getBoolean("first_try")

                                    if (firstTry) {
                                        val intent =
                                            Intent(
                                                this@ForgetPasswordActivity,
                                                ResetPasswordActivity::class.java
                                            )
                                        intent.putExtra(
                                            "mobileNumber",
                                            etMobileNumber.text.toString()
                                        )

                                        startActivity(intent)
                                        finish()
                                    } else {
                                        progrssLayout.visibility = View.VISIBLE
                                        btnNext.visibility = View.GONE

                                        val dialog =
                                            AlertDialog.Builder(this@ForgetPasswordActivity)
                                        dialog.setTitle("Information")
                                        dialog.setMessage("Please refer to the previous email for the OTP")
                                        dialog.setPositiveButton("OK") { text, listener ->

                                            val intent =
                                                Intent(
                                                    this@ForgetPasswordActivity,
                                                    ResetPasswordActivity::class.java
                                                )

                                            intent.putExtra(
                                                "mobileNumber",
                                                etMobileNumber.text.toString()
                                            )
                                            startActivity(intent)
                                            finish()

                                        }
                                        dialog.create()
                                        dialog.show()


                                    }


                                } else {
                                    val errormessage = jsonObject.getString("errorMessage")
                                    Toast.makeText(
                                        this@ForgetPasswordActivity,
                                        errormessage,
                                        Toast.LENGTH_SHORT
                                    )
                                        .show()

                                }


                            } catch (e: JSONException) {
                                Toast.makeText(
                                    this@ForgetPasswordActivity,
                                    "some error occured",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }, Response.ErrorListener {

                            Toast.makeText(
                                this@ForgetPasswordActivity,
                                "Volley error occcured",
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
                Toast.makeText(
                    this@ForgetPasswordActivity,
                    "No Internet Connection",
                    Toast.LENGTH_SHORT
                ).show()

            }


        }


    }
}

