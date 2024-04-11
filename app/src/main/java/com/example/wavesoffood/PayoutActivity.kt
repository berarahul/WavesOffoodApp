package com.example.wavesoffood

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.wavesoffood.databinding.ActivityPayoutBinding
import com.example.wavesoffood.model.OrderDetails
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class PayoutActivity : AppCompatActivity() {
    lateinit var binding:ActivityPayoutBinding

    private lateinit var auth: FirebaseAuth
    private lateinit var name:String
    private lateinit var address:String
    private lateinit var phonenumber:String
     private lateinit var  totalamount:String
     private lateinit var foodItemName:ArrayList<String>
     private lateinit var foodItemPrice:ArrayList<String>
     private lateinit var foodItemImage:ArrayList<String>
     private lateinit var foodItemDescription:ArrayList<String>
     private lateinit var foodItemIngredient:ArrayList<String>
     private lateinit var foodItemQuantities:ArrayList<Int>
     private lateinit var databaseReference: DatabaseReference
     private lateinit var userId: String





    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
       binding=ActivityPayoutBinding.inflate(layoutInflater)
        setContentView(binding.root)
           // initialize firebase and user Details

          auth=FirebaseAuth.getInstance()
        databaseReference=FirebaseDatabase.getInstance().getReference()
        //set user Data

        setuserData()

          // get user data
        val intent:Intent=intent
        foodItemName=intent.getStringArrayListExtra("FoodItemName")as ArrayList<String>
        foodItemPrice=intent.getStringArrayListExtra("FoodItemPrice")as ArrayList<String>
        foodItemImage=intent.getStringArrayListExtra("FoodItemImage")as ArrayList<String>
        foodItemDescription=intent.getStringArrayListExtra("FoodItemDescription")as ArrayList<String>
        foodItemIngredient=intent.getStringArrayListExtra("FoodItemIngredient")as ArrayList<String>
        foodItemQuantities=intent.getIntegerArrayListExtra("FoodItemQuantities")as ArrayList<Int>


        totalamount=calculateTotalAmount().toString() +"$"
        binding.totalamount.isEnabled=false
        binding.totalamount.setText(totalamount)
        binding.payoutbackbutton.setOnClickListener {
            finish()
        }
        binding.placedmyorder.setOnClickListener {

            // get data from text view
            name=binding.name.text.toString().trim()
            address=binding.address.text.toString().trim()
            phonenumber=binding.phonenumber.text.toString().trim()

            if (name.isBlank()&&address.isBlank()&&phonenumber.isBlank()){

                Toast.makeText(this,"Please Enter all the Details",Toast.LENGTH_SHORT).show()

            }else
            {
                placeorder()
            }

            val bottomsheetDialog=CongratsButtonSheet()
            bottomsheetDialog.show(supportFragmentManager,"Test")



        }

    }

    private fun placeorder() {
        userId=auth.currentUser?.uid?:""
        val time=System.currentTimeMillis()
        val itempushkey: String?=databaseReference.child("orderDetails").push().key
        val orderDetails=OrderDetails(userId,name,foodItemName,foodItemPrice,foodItemImage,foodItemQuantities,address,totalamount,phonenumber,time,itempushkey,false,false)
        val orderReference:DatabaseReference=databaseReference.child("orderDetails").child(itempushkey!!)
        orderReference.setValue(orderDetails).addOnSuccessListener {

            val bottomsheetDialog=CongratsButtonSheet()
            bottomsheetDialog.show(supportFragmentManager,"Test")
            removeItemFromCart()
           addOrderToHistory(orderDetails)
        }
            .addOnFailureListener{
                Toast.makeText(this,"Failed to Order",Toast.LENGTH_SHORT).show()
            }
    }

    private fun addOrderToHistory(orderDetails: OrderDetails) {
        databaseReference.child("user").child(userId).child("BuyHistory").child(orderDetails.itemPushKey!!).setValue(orderDetails).addOnSuccessListener {

        }
    }

    private fun removeItemFromCart() {
     val cartItemReference:DatabaseReference=databaseReference.child("user").child(userId).child("cartItems")
        cartItemReference.removeValue()
    }

    private fun calculateTotalAmount(): Int {
var totalamount=0
        for (i in 0 until foodItemPrice.size){
            var price:String=foodItemPrice[i]
            val lastchar:Char=price.last()
            val priceIntValue: Int = if (lastchar=='$'){
                price.dropLast(1).toInt()
            }else{
                price.toInt()
            }
            var quantity:Int=foodItemQuantities[i]
            totalamount+=priceIntValue*quantity
        }
        return totalamount
    }

    private fun setuserData() {
        val user:FirebaseUser?=auth.currentUser
        if (user!=null){
            val userId:String=user.uid
            val userReference:DatabaseReference=databaseReference.child("user").child(userId)
            userReference.addListenerForSingleValueEvent(object :ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()){

                        val names:String=snapshot.child("name").getValue(String::class.java)?:""
                        val addresss:String=snapshot.child("address").getValue(String::class.java)?:""
                        val phonee:String?=snapshot.child("phone").getValue(String::class.java)?:""
                        binding.apply {
                            name.setText(names)
                            address.setText(addresss)
                            phonenumber.setText(phonee)

                        }
                    }

                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }
            })
        }



    }
}