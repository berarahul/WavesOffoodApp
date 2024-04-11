package com.example.wavesoffood.Fragment

import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.wavesoffood.R
import com.example.wavesoffood.adapter.BuyagainAdapter
import com.example.wavesoffood.databinding.BuyagainitemBinding
import com.example.wavesoffood.databinding.FragmentHistoryBinding
import com.example.wavesoffood.model.OrderDetails
import com.example.wavesoffood.recentOrderItems
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.Query
import com.google.firebase.database.ValueEventListener

class HistoryFragment : Fragment() {
private lateinit var binding: FragmentHistoryBinding
private lateinit var buyagainAdapter: BuyagainAdapter

private lateinit var database: FirebaseDatabase
private lateinit var auth: FirebaseAuth
private lateinit var userId:String
private var lisdtofOrderItem:MutableList<OrderDetails> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding=FragmentHistoryBinding.inflate(layoutInflater,container,false)
        //Initialize firebase auth
        auth=FirebaseAuth.getInstance()
        //Initialize firebase database
        database=FirebaseDatabase.getInstance()
        // Retrive and Display the user order History
        retrivebyHistory()

        //recent buy button click
    binding.recentbuyitem.setOnClickListener {
        seeItemsRecentBuy()
    }
        binding.recivedbutton.setOnClickListener {
            upDateOrderStatus()
        }
        return binding.root
    }

    private fun upDateOrderStatus() {
        val itempushkey:String?=lisdtofOrderItem[0].itemPushKey
        val completeorderReference:DatabaseReference=database.reference.child("CompletedOrder").child(itempushkey!!)
        completeorderReference.child("paymentReceived").setValue(true)
    }

    //Function to see item recent buy
    private fun seeItemsRecentBuy() {
        lisdtofOrderItem.firstOrNull()?.let { recentBuy ->

            val intent =Intent(requireContext(),recentOrderItems::class.java)

            intent.putExtra("RecentBuyOrderItem",ArrayList(lisdtofOrderItem))
            startActivity(intent)

        }
    }
    //Function to see item retrive buy history
    private fun retrivebyHistory() {
        userId=auth.currentUser?.uid?:""
        val buyitemReference:DatabaseReference=database.reference.child("user").child(userId).child("BuyHistory")
        val sortingQuery:Query=buyitemReference.orderByChild("currentTime")
       sortingQuery.addListenerForSingleValueEvent(object : ValueEventListener{
           override fun onDataChange(snapshot: DataSnapshot) {
               for (buysnapshot in snapshot.children){
                   val buyHistoryItem:OrderDetails?=buysnapshot.getValue(OrderDetails::class.java)
                   buyHistoryItem?.let {
                       lisdtofOrderItem.add(it)
                   }
               }
               lisdtofOrderItem.reverse()
               if (lisdtofOrderItem.isNotEmpty()){
                   //display the most recent order details
                   setDatainRecentBuyItem()
                   //setup the recyclerview with previous order details
                   setPreviousBuyItemRecyclerView()
               }
           }

           override fun onCancelled(error: DatabaseError) {
               TODO("Not yet implemented")
           }
       })

    }
    // function to display the most recent order details
    private fun setDatainRecentBuyItem() {
        val recentorderItem:OrderDetails? = lisdtofOrderItem.firstOrNull()
        recentorderItem?.let {
            with(binding){
                BuyAgainFoodName.text=it.foodNames?.firstOrNull()?:""
                BuyAgainFoodPrice.text=it.foodprices?.firstOrNull()?:""
                val image:String=it.foodImages?.firstOrNull()?:""
                val uri:Uri=Uri.parse(image)
                Glide.with(requireContext()).load(uri).into(BuyAgainFoodImage)
              val isOrderAccepted:Boolean=lisdtofOrderItem[0].orderAccepted

                if (isOrderAccepted){
                    BuyAgainFoodButton.background.setTint(Color.GREEN)
                    recivedbutton.visibility=View.VISIBLE
                }
            }
        }
    }
    //function setup the recyclerview with previous order details
    private fun setPreviousBuyItemRecyclerView() {
        val buyagainFoodname= mutableListOf<String>()
        val buyagainFoodprice= mutableListOf<String>()
        val buyagainFoodImage= mutableListOf<String>()
        for (i : Int in 1 until lisdtofOrderItem.size){
            lisdtofOrderItem[i].foodNames?.firstOrNull()?.let { buyagainFoodname.add(it) }
            lisdtofOrderItem[i].foodprices?.firstOrNull()?.let { buyagainFoodprice.add(it) }
            lisdtofOrderItem[i].foodImages?.firstOrNull()?.let { buyagainFoodImage.add(it) }
        }
        val rv:RecyclerView = binding.BuyAgainRecyclerView
        rv.layoutManager=LinearLayoutManager(requireContext())
        buyagainAdapter=BuyagainAdapter(buyagainFoodname,buyagainFoodprice,buyagainFoodImage,requireContext())
        rv.adapter= buyagainAdapter
    }

          }



