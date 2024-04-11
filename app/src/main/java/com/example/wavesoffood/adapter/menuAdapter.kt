package com.example.wavesoffood.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View.OnClickListener
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.wavesoffood.DetailsActivity
import com.example.wavesoffood.databinding.MenuItemBinding
import com.example.wavesoffood.model.MenuItem


class menuAdapter(
    private val menuItems:List<MenuItem> ,
    private val requireContext:Context) :RecyclerView.Adapter<menuAdapter.MenuViewHolder>(){
private val itemClickListener:OnClickListener?=null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MenuViewHolder {

        val binding=MenuItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return MenuViewHolder(binding)

    }



    override fun onBindViewHolder(holder: MenuViewHolder, position: Int) {
       holder.bind(position)


    }
    override fun getItemCount(): Int =menuItems.size
 inner   class MenuViewHolder(private val binding:MenuItemBinding):RecyclerView.ViewHolder(binding.root) {

     init {
         binding.root.setOnClickListener {
             val position=adapterPosition
             if (position!=RecyclerView.NO_POSITION){
                OpenDetailedActivity(position)
             }

         }
     }

     private fun OpenDetailedActivity(position: Int) {
         val menuItem:MenuItem=menuItems[position]

         // a intent to open details activity and pass data
         val intent:Intent=Intent(requireContext,DetailsActivity::class.java).apply {
             putExtra("MenuItemName",menuItem.foodname)
             putExtra("MenuItemImage",menuItem.foodimage
             )
             putExtra("MenuItemDescription",menuItem.fooddescription)
             putExtra("MenuItemIngredients",menuItem.foodingradiants
             )
             putExtra("MenuItemPrice",menuItem.foodprice)
         }
         //start the Detailed Activity
         requireContext.startActivity(intent)

     }
// set data in to recyclerview
@SuppressLint("SuspiciousIndentation")
fun bind(position: Int) {
         val menuItem:MenuItem=menuItems[position]
              binding.apply {
                  menufoodname.text=menuItem.foodname
                  menuprice.text=menuItem.foodprice
              val uri:Uri= Uri.parse(menuItem.foodimage
              )
                  Glide.with(requireContext).load(uri).into(menuimage)



              }
        }

    }

}

