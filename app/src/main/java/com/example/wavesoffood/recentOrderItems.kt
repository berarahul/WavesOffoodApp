package com.example.wavesoffood

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.wavesoffood.adapter.RecentBuyAdapter
import com.example.wavesoffood.databinding.ActivityRecentOrderItemsBinding
import com.example.wavesoffood.model.OrderDetails

class recentOrderItems : AppCompatActivity() {

    private val binding:ActivityRecentOrderItemsBinding by lazy {
        ActivityRecentOrderItemsBinding.inflate(layoutInflater)
    }

    private lateinit var allfoodName:ArrayList<String>
    private lateinit var allfoodImage:ArrayList<String>
    private lateinit var allfoodPrice:ArrayList<String>
    private lateinit var allfoodQuantities:ArrayList<Int>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
binding.backButton.setOnClickListener {
    finish()
}

        val recentOrderItems=intent.getSerializableExtra("RecentBuyOrderItem") as ArrayList<OrderDetails>
        recentOrderItems?.let { orderDetails->
            if (orderDetails.isNotEmpty()){
                val recentorderItem:OrderDetails =orderDetails[0]
                allfoodName=recentorderItem.foodNames as ArrayList<String>
                allfoodImage=recentorderItem.foodImages as ArrayList<String>
                allfoodPrice=recentorderItem.foodprices as ArrayList<String>
                allfoodQuantities=recentorderItem.foodQuantities as ArrayList<Int>

            }
        }
        setAdapter()
    }

    private fun setAdapter() {
      val rv:RecyclerView=binding.recyclerviewItem
        rv.layoutManager=LinearLayoutManager(this)
        val adapter= RecentBuyAdapter(this,allfoodName,allfoodImage,allfoodPrice,allfoodQuantities)
        rv.adapter=adapter
    }
}