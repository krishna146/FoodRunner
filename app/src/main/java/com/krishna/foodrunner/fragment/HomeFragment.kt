package com.krishna.foodrunner.fragment

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley

import com.krishna.foodrunner.R
import com.krishna.foodrunner.adapter.HomeRecyclerAdapter
import com.krishna.foodrunner.model.Restaurant
import com.krishna.foodrunner.util.ConnectionManager
import kotlinx.android.synthetic.*
import org.json.JSONException


class HomeFragment : Fragment() {

    lateinit var recyclerHome: RecyclerView
    lateinit var layoutManager: RecyclerView.LayoutManager
    val restaurantInfoList = arrayListOf<Restaurant>()
    lateinit var recyclerAdapter: HomeRecyclerAdapter
    lateinit var progressLayout: RelativeLayout
    lateinit var progressBar: ProgressBar

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_home, container, false)

        recyclerHome = view.findViewById(R.id.recyclerdHome)
        progressBar = view.findViewById(R.id.progressBar)
        progressLayout = view.findViewById(R.id.progressLayout)
        progressLayout.visibility = View.VISIBLE

        val queue = Volley.newRequestQueue(activity as Context)
        val url = "http://13.235.250.119/v2/restaurants/fetch_result/"

        if (ConnectionManager().checkConnectivity(activity as Context)) {

            val jsonRequest = object :
                JsonObjectRequest(Request.Method.GET, url, null, Response.Listener {
                    try {

                        val data = it.getJSONObject("data")
                        val success = data.getBoolean("success")

                        if (success) {

                            progressLayout.visibility = View.GONE
                            val jsonArray = data.getJSONArray("data")
                            for (i in 0 until jsonArray.length()) {

                                val jsonObject = jsonArray.getJSONObject(i)
                                val restaurantObject = Restaurant(
                                    jsonObject.getString("id"),
                                    jsonObject.getString("name"),
                                    jsonObject.getString("rating"),
                                    jsonObject.getString("cost_for_one"),
                                    jsonObject.getString("image_url")
                                )
                                restaurantInfoList.add(restaurantObject)

                            }

                            // setting layout manager as linear layout manager
                            layoutManager = LinearLayoutManager(activity)

                            //sttigng layout manager for recycler view
                            recyclerHome.layoutManager = layoutManager

                            // setting adapter and ViewHolder for Recyler view
                            recyclerAdapter =
                                HomeRecyclerAdapter(activity as Context, restaurantInfoList)
                            recyclerHome.adapter = recyclerAdapter


                        } else {

                            Toast.makeText(
                                activity as Context,
                                "some error occured",
                                Toast.LENGTH_SHORT
                            ).show()

                        }

                    } catch (e: JSONException) {

                        Toast.makeText(
                            activity as Context,
                            "Some unexpected error Occured",
                            Toast.LENGTH_SHORT
                        ).show()

                    }

                }, Response.ErrorListener {

                    Toast.makeText(activity as Context, "Volley error occured", Toast.LENGTH_SHORT)
                        .show()

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

            val dialog = AlertDialog.Builder(activity as Context)
            dialog.setTitle("Error")
            dialog.setMessage("No Internet Connection found.\nPlease Connect to the internet and\nre-open the app.")
            dialog.setPositiveButton("ok") { text, Listener ->

                ActivityCompat.finishAffinity(activity as Activity)

            }

            dialog.create()
            dialog.show()

        }

        return view

    }

}
