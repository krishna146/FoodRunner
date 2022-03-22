package com.krishna.foodrunner.activity

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.view.MenuItem
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import com.krishna.foodrunner.R
import com.krishna.foodrunner.fragment.*
import kotlinx.android.synthetic.main.drawer_header.*
import kotlinx.android.synthetic.main.drawer_header.view.*

class HomeActivity : AppCompatActivity() {

    var name: String? = "kalpesh"
    var mobileNumber: String? = "7782017779"
    lateinit var drawerLayout: DrawerLayout
    lateinit var sharedPreferences: SharedPreferences
    lateinit var navigationView: NavigationView
    lateinit var toolbar: androidx.appcompat.widget.Toolbar
    lateinit var txtName: TextView
    lateinit var txtMobileNumber: TextView
    var previousMenuItem: MenuItem? = null

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        sharedPreferences =
            getSharedPreferences(getString(R.string.preferences_file_name), Context.MODE_PRIVATE)

        setContentView(R.layout.activity_home)

        name = sharedPreferences.getString("name", "kalpesh")
        mobileNumber = sharedPreferences.getString("mobileNumber", "7782017779")

        navigationView = findViewById(R.id.navigationView)
        drawerLayout = findViewById(R.id.drawerLayout)
        toolbar = findViewById(R.id.toolbar)

        setSupportActionBar(toolbar)
        supportActionBar?.title = "Home"
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)


        val actionBarDrawerToggle = ActionBarDrawerToggle(
            this@HomeActivity,
            drawerLayout,
            R.string.open_drawer,
            R.string.close_drawer
        )

        drawerLayout.addDrawerListener(actionBarDrawerToggle)
        actionBarDrawerToggle.syncState()

        val hView = navigationView.getHeaderView(0)
        txtName = hView.findViewById(R.id.txtName)
        txtMobileNumber = hView.findViewById(R.id.txtMobileNumber)
        txtName.text = name
        txtMobileNumber.text = mobileNumber

        openHome()



        navigationView.setNavigationItemSelectedListener {

            if (previousMenuItem != null) {
                previousMenuItem?.isChecked = false
            }
            it.isCheckable = true
            it.isChecked = true
            previousMenuItem = it


            when (it.itemId) {

                R.id.home -> {
                    openHome()
                    drawerLayout.closeDrawers()

                }
                R.id.myProfile -> {

                    val transaction = supportFragmentManager.beginTransaction()
                        .replace(R.id.frame, ProfileFragment())
                    transaction.commit()
                    supportActionBar?.title = "My profile"
                    drawerLayout.closeDrawers()

                }

                R.id.favouriteRestaurants -> {
                    val transaction = supportFragmentManager.beginTransaction()
                        .replace(R.id.frame, FavouriteFragment())
                    transaction.commit()
                    supportActionBar?.title = "Favourites Restaurants"
                    drawerLayout.closeDrawers()


                }

                R.id.orderHistory -> {
                    val transaction = supportFragmentManager.beginTransaction()
                        .replace(R.id.frame, OrderHistoryFragment())
                    transaction.commit()
                    supportActionBar?.title = "My Previous Orders"
                    drawerLayout.closeDrawers()


                }

                R.id.faqs -> {
                    val transaction = supportFragmentManager.beginTransaction()
                        .replace(R.id.frame, FaqSFragment())
                    transaction.commit()
                    supportActionBar?.title = "Frequently Asked Questions"
                    drawerLayout.closeDrawers()


                }

                R.id.logOut -> {

                    val dialog = AlertDialog.Builder(this@HomeActivity)
                    dialog.setTitle("Confirmation")
                    dialog.setMessage("Are you sure you want to log out?")
                    dialog.setPositiveButton("yes") { text, Listener ->
                        startActivity(Intent(this@HomeActivity, LoginActivity::class.java))
                        sharedPreferences.edit().clear().apply()
                        finish()
                        Toast.makeText(
                            this@HomeActivity,
                            "Logged Out Successfuly",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    dialog.setNegativeButton("no") { text, Listener ->

                    }
                    dialog.create()
                    dialog.show()
                    drawerLayout.closeDrawers()
                    it.isCheckable = false
                    it.isChecked = false


                }

            }

            return@setNavigationItemSelectedListener true

        }


    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        val id = item.itemId
        if (id == android.R.id.home) {
            drawerLayout.openDrawer(GravityCompat.START)
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        val frag = supportFragmentManager.findFragmentById(R.id.frame)
        when (frag) {

            !is HomeFragment -> openHome()
            else -> super.onBackPressed()

        }
    }

    fun openHome() {
        val transaction =
            supportFragmentManager.beginTransaction().replace(R.id.frame, HomeFragment())
        transaction.commit()
        supportActionBar?.title = "All Restaurants"
        navigationView.setCheckedItem(R.id.home)
    }


}
