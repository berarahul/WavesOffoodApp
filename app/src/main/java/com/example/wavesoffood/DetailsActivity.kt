package com.example.wavesoffood

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.bumptech.glide.Glide
import com.example.wavesoffood.databinding.ActivityDetailsBinding
import com.example.wavesoffood.model.cartitems
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class DetailsActivity : AppCompatActivity() {

    private lateinit var binding:ActivityDetailsBinding

    private var foodname:String?=null
    private var foodImage:String?=null
    private var foodDescription:String?=null
    private var foodingredients:String?=null
    private var foodprice:String?=null
private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //initialize auth
        auth=FirebaseAuth.getInstance()
       foodname=intent.getStringExtra("MenuItemName")
        foodDescription=intent.getStringExtra("MenuItemDescription")
        foodingredients=intent.getStringExtra("MenuItemIngredients")
        foodprice=intent.getStringExtra("MenuItemPrice")
       foodImage=intent.getStringExtra("MenuItemImage")

        with(binding){
            detailedfoodname.text=foodname
            descriptiontextview.text=foodDescription
            ingrediantstextview.text=foodingredients
            Glide.with(this@DetailsActivity).load(Uri.parse(foodImage)).into(detailedfoodimage)

        }
        binding.imageButton.setOnClickListener {
            finish()
        }
binding.addtocartbutton.setOnClickListener {
    additemtocart()
}
    }

    private fun additemtocart() {
        val database:DatabaseReference=FirebaseDatabase.getInstance().reference
        val userId: String = auth.currentUser?.uid?:""


        //create a cartItems object

        val cartitem=cartitems(foodname.toString(),foodprice.toString(),foodDescription.toString(),foodImage.toString(),1)


        //save data to cart item to firebase database
        database.child("user").child(userId).child("cartItems").push().setValue(cartitem).addOnSuccessListener {
            Toast.makeText(this,"Items added into cart successfully",Toast.LENGTH_SHORT).show()

        }.addOnFailureListener{
            Toast.makeText(this,"Item Not Added",Toast.LENGTH_SHORT).show()
        }
    }
}