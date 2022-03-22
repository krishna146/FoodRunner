package com.krishna.foodrunner.activity

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.krishna.foodrunner.R
import com.krishna.foodrunner.util.ConnectionManager
import kotlinx.android.synthetic.main.fragment_home.*
import org.json.JSONException
import org.json.JSONObject


class ResetPasswordActivity : AppCompatActivity() {

    lateinit var etOtp: EditText
    lateinit var etNewPassword: EditText
    lateinit var etConfirmPassword: EditText
    lateinit var btnSubmit: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_reset_password)
        val mobileNumber = intent.getStringExtra(
            "mobileNumber"
        )


etOtp = findViewById(R.id.etOtp)
etNewPassword = findViewById(R.id.etNewPassword)
etConfirmPassword = findViewById(R.id.etConfirmPassword)
btnSubmit = findViewById(R.id.btnSubmit)
val url = " http://13.235.250.119/v2/reset_password/fetch_result"
val queue = Volley.newRequestQueue(this@ResetPasswordActivity)

btnSubmit.setOnClickListener {
    val jsonParams = JSONObject()
    jsonParams.put("mobile_number", mobileNumber)
    jsonParams.put("password", etNewPassword.text)
    jsonParams.put("otp", etOtp.text)
    if (ConnectionManager().checkConnectivity(this@ResetPasswordActivity)) {
        val jsonRequest =
            object : JsonObjectRequest(
                Request.Method.POST,
                url,
                jsonParams,
                Response.Listener {
                    try {
                        println("response is $it")
                        val jsonObject = it.getJSONObject("data")
                        val success = jsonObject.getBoolean("success")

                        if (success) {
                            val successMessage = jsonObject.getString("successMessage")
                            val intent = Intent(
                                this@ResetPasswordActivity,
                                LoginActivity::class.java
                            )
                            startActivity(intent)
                            finish()
                            Toast.makeText(
                                this@ResetPasswordActivity,
                                successMessage,
                                Toast.LENGTH_SHORT
                            ).show()
                        } else {

                            val errorMessage = jsonObject.getString("errorMessage")
                            Toast.makeText(
                                this@ResetPasswordActivity,
                                errorMessage,
                                Toast.LENGTH_SHORT
                            )
                                .show()
                        }

                    } catch (e: JSONException) {
                        Toast.makeText(
                            this@ResetPasswordActivity,
                            "some error occured",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
                ,
                Response.ErrorListener {


                }) {
                override fun getHeaders(): MutableMap<String, String> {
                    val headers = HashMap<String, String>()
                    headers["Content-type"] = "application/jsom"
                    headers["token"] = "0f3097da6dee50"
                    return headers

                }

            }
        queue.add(jsonRequest)
    } else {

        Toast.makeText(this@ResetPasswordActivity, "No Internet Connection", Toast.LENGTH_SHORT)
            .show()

    }

}


}
}
