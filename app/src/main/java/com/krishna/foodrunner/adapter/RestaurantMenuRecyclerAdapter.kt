package com.krishna.foodrunner.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.TextureView
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.krishna.foodrunner.R
import com.krishna.foodrunner.model.RestaurantMenu

class RestaurantMenuRecyclerAdapter(context: Context,val itemList: ArrayList<RestaurantMenu>) :
    RecyclerView.Adapter<RestaurantMenuRecyclerAdapter.RestaurantMenuViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RestaurantMenuViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.recycler_restaurant_menu_single_row, parent, false)
        return RestaurantMenuViewHolder(view)
    }

    override fun getItemCount(): Int {
        return itemList.size

    }

    override fun onBindViewHolder(holder: RestaurantMenuViewHolder, position: Int) {
        val menu = itemList[position]
        holder.txtMenuItemName.text = menu.name
        holder.txtMenuItemNumber.text = Integer.toString(position+1)
        holder.txtMenuItemPrice.text =  "Rs. ${menu.costForOne}"
        holder.btnAddToCart.setOnClickListener {


        }


    }

    class RestaurantMenuViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val txtMenuItemName: TextView = view.findViewById(R.id.txtMenuItemName)
        val txtMenuItemPrice: TextView = view.findViewById(R.id.txtMenuItemPrice)
        val btnAddToCart: Button = view.findViewById(R.id.btnAddToCart)
        val txtMenuItemNumber: TextView = view.findViewById(R.id.txtMenuItemNumber)


    }


}