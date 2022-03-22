package com.krishna.foodrunner.activity


import android.content.Context
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.krishna.foodrunner.R
import com.krishna.foodrunner.adapter.RestaurantMenuRecyclerAdapter
import com.krishna.foodrunner.database.RestaurantDatabase
import com.krishna.foodrunner.database.RestaurantEntity
import com.krishna.foodrunner.model.RestaurantMenu
import com.krishna.foodrunner.util.ConnectionManager
import org.json.JSONException


class RestaurantMenuActivity : AppCompatActivity() {

    lateinit var toolbar: androidx.appcompat.widget.Toolbar
    lateinit var recyclerRestaurantMenu: RecyclerView
    lateinit var layoutManager: RecyclerView.LayoutManager
    var restaurantMenuInfo = arrayListOf<RestaurantMenu>()
    lateinit var recyclerAdapter: RestaurantMenuRecyclerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_restaurant_menu)

        recyclerRestaurantMenu = findViewById(R.id.recyclerRestaurantMenu)
        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.title = "${intent.getStringExtra("restaurant_name")}"
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val restaurantId = intent.getStringExtra("restaurant_id")
        val queue = Volley.newRequestQueue(this)
        val url = "http://13.235.250.119/v2/restaurants/fetch_result/$restaurantId"

        if (ConnectionManager().checkConnectivity(this@RestaurantMenuActivity)) {

            val jsonRequest =
                object : JsonObjectRequest(Request.Method.GET, url, null, Response.Listener {
                    try {
                        val data = it.getJSONObject("data")
                        val success = data.getBoolean("success")
                        if (success) {
                            val jsonArray = data.getJSONArray("data")
                            for (i in 0 until jsonArray.length()) {

                                val jsonObject = jsonArray.getJSONObject(i)
                                val restaurantMenuObject = RestaurantMenu(
                                    jsonObject.getString("id"),
                                    jsonObject.getString("name"),
                                    jsonObject.getString("cost_for_one"),
                                    jsonObject.getString("restaurant_id")
                                )
                                restaurantMenuInfo.add(restaurantMenuObject)


                            }



                            layoutManager = LinearLayoutManager(this)
                            recyclerRestaurantMenu.layoutManager = layoutManager
                            recyclerAdapter = RestaurantMenuRecyclerAdapter(
                                this,
                                restaurantMenuInfo
                            )
                            recyclerRestaurantMenu.adapter = recyclerAdapter


                        }


                    } catch (e: JSONException) {

                        Toast.makeText(
                            this@RestaurantMenuActivity,
                            "Some unexpected Error Occured",
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                }, Response.ErrorListener {
                    Toast.makeText(
                        this@RestaurantMenuActivity,
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


            Toast.makeText(
                this@RestaurantMenuActivity,
                "No Internet Connection",
                Toast.LENGTH_SHORT
            ).show()
        }

    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    class DBAsync(context: Context, val restaurantEntity: RestaurantEntity, val mode: Int) :
        AsyncTask<Void, Void, Boolean>() {

        /* Mode 1 -> check DB if the restaurant is favourites or not
           Mode 2 -> save the restaurant into DB as favourite
           Mode # -> Remove the favourite restaurant
         */
        val db =
            Room.databaseBuilder(context, RestaurantDatabase::class.java, "restaurants-db").build()


        override fun doInBackground(vararg p0: Void?): Boolean {


            when (mode) {

                1 -> {
                    //check if the restaurant if favourite or not
                    val restaurant: RestaurantEntity? = db.restaurantDao().getRestaurantById(restaurantEntity.restaurant_id.toString())
                    db.close()
                    return restaurant != null

                }
                2 -> {
                    //save the restaurant into db as favourites
                    db.restaurantDao().insertRestaurant(restaurantEntity)
                    db.close()
                    return true

                }
                3 -> {
                    //remove the favourite restaurant
                    db.restaurantDao().deleteRestaurant(restaurantEntity)
                    db.close()
                    return true
                }

            }

            return false

        }

    }


}