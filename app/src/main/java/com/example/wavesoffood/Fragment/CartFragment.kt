package com.example.wavesoffood.Fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.wavesoffood.CongratsButtonSheet
import com.example.wavesoffood.PayoutActivity
import com.example.wavesoffood.R
import com.example.wavesoffood.adapter.cartAdapter
import com.example.wavesoffood.databinding.FragmentCartBinding
import com.example.wavesoffood.model.cartitems
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class CartFragment : Fragment() {

private lateinit var binding :FragmentCartBinding

private lateinit var auth: FirebaseAuth
private lateinit var database: FirebaseDatabase
private lateinit var foodnames: MutableList<String>
private lateinit var foodprices: MutableList<String>
private lateinit var  foodDescription: MutableList<String>
private lateinit var foodImageUri:MutableList<String>
private lateinit var foodIngrediants:MutableList<String>
private lateinit var quantity:MutableList<Int>
private lateinit var CartAdapter:cartAdapter
private lateinit var userId:String





    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding=FragmentCartBinding.inflate(inflater,container,false)

auth=FirebaseAuth.getInstance()


        retrivecartItems()



        binding.proccedbutton.setOnClickListener {

            //get order item details before processing to check out

            getorderItemDetails()



        }

        return binding.root
    }

    private fun getorderItemDetails() {
      val  orderIdReference:DatabaseReference=database.reference.child("user").child(userId).child("cartItems")

       val foodname:MutableList<String> = mutableListOf()
       val foodprice:MutableList<String> = mutableListOf()
       val foodimage:MutableList<String> = mutableListOf()
       val foodDescription:MutableList<String> = mutableListOf()
       val foodingredient:MutableList<String> = mutableListOf()

        // get item Quantities

        val foodquantities:MutableList<Int> =CartAdapter.getUpdatedquantitites()

        orderIdReference.addListenerForSingleValueEvent(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                for (foodsnapshot:DataSnapshot in snapshot.children){

                    //get the cartitems to respective list

                    val orderitems=foodsnapshot.getValue(cartitems::class.java)
                    orderitems?.foodname?.let { foodname.add(it) }
                    orderitems?.foodprice?.let { foodprice.add(it) }
                    orderitems?.foodImage?.let { foodimage.add(it) }
                    orderitems?.foodDescription?.let { foodDescription.add(it) }
                    orderitems?.foodingrdient?.let { foodingredient.add(it) }

                }
                orderNow(foodname,foodprice,foodimage,foodDescription,foodingredient,foodquantities)
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(requireContext(),"This order Making Failed. Please try Again",Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun orderNow(
        foodname: MutableList<String>,
        foodprice: MutableList<String>,
        foodimage: MutableList<String>,
        foodDescription: MutableList<String>,
        foodingredient: MutableList<String>,
        foodquantities: MutableList<Int>
    ) {
        if (isAdded&& context!=null){

            val intent =Intent(requireContext(),PayoutActivity::class.java)
            intent.putExtra("FoodItemName",foodname as ArrayList<String>)
            intent.putExtra("FoodItemPrice",foodprice as ArrayList<String>)
            intent.putExtra("FoodItemImage",foodimage as ArrayList<String>)
            intent.putExtra("FoodItemDescription",foodDescription as ArrayList<String>)
            intent.putExtra("FoodItemIngredient",foodingredient as ArrayList<String>)
            intent.putExtra("FoodItemQuantities",foodquantities as ArrayList<Int>)
 startActivity(intent)

        }
    }


    private fun retrivecartItems() {
       // database reference to the firebase

        database=FirebaseDatabase.getInstance()
        userId=auth.currentUser?.uid?:""
        val foodReference:DatabaseReference=database.reference.child("user").child(userId).child("cartItems")
        foodnames= mutableListOf()
        foodprices= mutableListOf()
        foodDescription= mutableListOf()
        foodImageUri= mutableListOf()
        foodIngrediants= mutableListOf()
        quantity= mutableListOf()

        //fetch data from the database

        foodReference.addListenerForSingleValueEvent(object :ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
               for (foodsnapshot in snapshot.children){

                   // get the cartitems object from the child node
                   val CartItems:cartitems? =foodsnapshot.getValue(cartitems::class.java)

                   CartItems?.foodname?.let { foodnames.add(it) }
                   CartItems?.foodprice?.let { foodprices.add(it) }
                   CartItems?.foodDescription?.let { foodDescription.add(it) }
                   CartItems?.foodImage?.let { foodImageUri.add(it) }
                   CartItems?.foodQuantity?.let { quantity.add(it) }
                   CartItems?.foodingrdient?.let { foodIngrediants.add(it) }
               }
                setAdapter()
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(context,"This data Not Fetch",Toast.LENGTH_SHORT).show()
            }
        })
        }

    private fun setAdapter() {
        CartAdapter=cartAdapter(requireContext(),foodnames,foodprices,foodDescription,foodImageUri,quantity,foodIngrediants)
        binding.cartrecyclerview.layoutManager=LinearLayoutManager(requireContext(),LinearLayoutManager.VERTICAL,false)
        binding.cartrecyclerview.adapter=CartAdapter
    }


    companion object {

    }
}