package com.krishna.foodrunner.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.krishna.foodrunner.R
import com.krishna.foodrunner.activity.RestaurantMenuActivity
import com.krishna.foodrunner.database.RestaurantEntity
import com.krishna.foodrunner.model.Restaurant
import com.squareup.picasso.Picasso

class HomeRecyclerAdapter(val context: Context, val itemList: ArrayList<Restaurant>) :
    RecyclerView.Adapter<HomeRecyclerAdapter.HomeViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeViewHolder {

        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.recycler_home_single_row, parent, false)
        return HomeViewHolder(view)

    }

    override fun getItemCount(): Int {
        return itemList.size

    }

    override fun onBindViewHolder(holder: HomeViewHolder, position: Int) {
        val restaurant = itemList[position]
        holder.txtRestaurantName.text = restaurant.restaurantName
        holder.txtCostPerPerson.text = restaurant.costPerPerson
        holder.txtRestaurantRating.text = restaurant.restaurantRating
        Picasso.get().load(restaurant.restaurantImage).error(R.drawable.default_restaurnt_image)
            .into(holder.imgRestaurant)

        holder.llContent.setOnClickListener {

            val intent = Intent(context, RestaurantMenuActivity::class.java)
            intent.putExtra("restaurant_name", restaurant.restaurantName)
            intent.putExtra("restaurant_id", restaurant.restaurantId)
            context.startActivity(intent)

        }
        val restaurantEntity = RestaurantEntity(
        restaurant.restaurantId?.toInt(),
        restaurant.restaurantName,
        restaurant.costPerPerson,
        restaurant.restaurantRating,
        restaurant.restaurantImage
        )
        val checkFav = RestaurantMenuActivity.DBAsync(context, restaurantEntity, 1).execute()
        val isFav = checkFav.get()
        if(isFav){

            val imageView =
                ContextCompat.getDrawable(context, R.drawable.ic_remove_from_favourites)
            holder.imgAddToFavourites.setImageDrawable(imageView)
        }else{

            val imageView =
                ContextCompat.getDrawable(context, R.drawable.ic_add_to_favourites)
            holder.imgAddToFavourites.setImageDrawable(imageView)
        }

        holder.imgAddToFavourites.setOnClickListener {

            if(!RestaurantMenuActivity.DBAsync(context, restaurantEntity, 1).execute().get()){
                val asyc = RestaurantMenuActivity.DBAsync(context, restaurantEntity, 2).execute()
                val success = asyc.get()
                if(success){
                    Toast.makeText(context, "Restaurant added to Favourites", Toast.LENGTH_SHORT).show()
                    val imageView =
                        ContextCompat.getDrawable(context, R.drawable.ic_remove_from_favourites)
                    holder.imgAddToFavourites.setImageDrawable(imageView)

                }else{
                    Toast.makeText(context, "Some Error Occured", Toast.LENGTH_SHORT).show()

                }

            }else{
                val asyc = RestaurantMenuActivity.DBAsync(context, restaurantEntity, 3).execute()
                val success = asyc.get()
                if(success){
                    Toast.makeText(context, "Restaurant Removed From Favourites", Toast.LENGTH_SHORT).show()
                    val imageView =
                        ContextCompat.getDrawable(context, R.drawable.ic_add_to_favourites)
                    holder.imgAddToFavourites.setImageDrawable(imageView)

                }else{
                    Toast.makeText(context, "Some Error Occured", Toast.LENGTH_SHORT).show()

                }

            }

        }


    }


    class HomeViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var imgRestaurant: ImageView = view.findViewById(R.id.imgRestaurant)
        var txtRestaurantName: TextView = view.findViewById(R.id.txtRestaurantName)
        var txtCostPerPerson: TextView = view.findViewById(R.id.txtCostPerPerson)
        var txtRestaurantRating: TextView = view.findViewById(R.id.txtRestaurantRating)
        var llContent: LinearLayout = view.findViewById(R.id.llContent)
        var imgAddToFavourites: ImageView = view.findViewById(R.id.imgAddToFavourites)


    }


}