package com.example.wavesoffood.adapter

import android.content.Context
import android.graphics.drawable.Drawable
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.example.wavesoffood.databinding.CartitemBinding
import com.example.wavesoffood.model.cartitems
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class cartAdapter(
    private val context: Context,
    private val CartItems:MutableList<String>,
    private val cartitemprice:MutableList<String>,
    private var cartDescription:MutableList<String>,
    private var Cartimage:MutableList<String>,
    private val cartQuantity:MutableList<Int>,
    private var cartIngrdient:MutableList<String>
    ) :
    RecyclerView.Adapter<cartAdapter.cartviewholder>() {

//instance Firebase

    private val auth = FirebaseAuth.getInstance()

    init {
        val database: FirebaseDatabase = FirebaseDatabase.getInstance()
        val userId: String = auth.currentUser?.uid ?: ""

        val cartitemnumber = CartItems.size
        itemQuantities = IntArray(cartitemnumber) { 1 }
        cartitemsReference = database.reference.child("user").child(userId).child("cartItems")
    }

    companion object {
        private var itemQuantities: IntArray = intArrayOf()
        private lateinit var cartitemsReference: DatabaseReference
    }


//    private  val itemquantitiy=IntArray(CartItems.size){1}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): cartviewholder {
        val binding = CartitemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return cartviewholder(binding)
    }

    override fun onBindViewHolder(holder: cartviewholder, position: Int) {
        holder.bind(position)
    }

    override fun getItemCount(): Int = CartItems.size


    //get updated quantity
    fun getUpdatedquantitites(): MutableList<Int> {
val itemquantity:MutableList<Int> = mutableListOf<Int>()


        itemquantity.addAll(cartQuantity)
        return itemquantity
    }


    inner class cartviewholder(private val binding: CartitemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int) {

            binding.apply {
                val quantity: Int = itemQuantities[position]
                cartfoodname.text = CartItems[position]
                itemprice.text = cartitemprice[position]

                //load image using glide


                val uriString: String = Cartimage[position]

                val uri = Uri.parse(uriString)
                Glide.with(context).load(uri).into(cartimage)



                CartItemquantity.text = quantity.toString()


                minusbutton.setOnClickListener {


                    decreaseQuantity(position)
                }

                plusbutton.setOnClickListener {

                    increaseQuantity(position)
                }
                delete.setOnClickListener {

                    val itemposition = adapterPosition
                    if (itemposition != RecyclerView.NO_POSITION) {

                        deleteitem(itemposition)
                    }
                }
            }
        }

        private fun increaseQuantity(position: Int) {
            if (itemQuantities[position] < 10) {
                itemQuantities[position]++
                cartQuantity[position]= itemQuantities[position]
                binding.CartItemquantity.text = itemQuantities[position].toString()
            }
        }

        private fun decreaseQuantity(position: Int) {
            if (itemQuantities[position] >= 1) {
                itemQuantities[position]--
                cartQuantity[position]= itemQuantities[position]

                binding.CartItemquantity.text = itemQuantities[position].toString()
            }
        }

        private fun deleteitem(position: Int) {
            val positionRetrive: Int = position
            getUniqueKeyposition(positionRetrive) { uniquekey ->
                if (uniquekey != null) {
                    removeitem(position, uniquekey)
                }

            }
        }


        private fun removeitem(position: Int, uniquekey: String) {
           if (uniquekey!=null){
               cartitemsReference.child(uniquekey).removeValue().addOnSuccessListener {
                   CartItems.removeAt(position)
                   Cartimage.removeAt(position)
                   cartDescription.removeAt(position)
                   cartQuantity.removeAt(position)
                   cartitemprice.removeAt(position)
                   cartIngrdient.removeAt(position)
                   Toast.makeText(context,"Item Delete",Toast.LENGTH_SHORT).show()
                   //update item queantity
                   itemQuantities= itemQuantities.filterIndexed{index, i -> index!=position }.toIntArray()
                   notifyItemRemoved(position)
                   notifyItemRangeChanged(position,CartItems.size)
               }.addOnFailureListener{
                   Toast.makeText(context,"Failed to Delete",Toast.LENGTH_SHORT).show()
               }
           }

        }

        private fun getUniqueKeyposition(positionRetrive: Int, onComplete: (String) -> Unit) {
            cartitemsReference.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {

                    var uniquekey: String? = null
                    //loop for snapshot children

                    snapshot.children.forEachIndexed() { index, dataSnapshot ->
                        if (index == positionRetrive) {
                            uniquekey = dataSnapshot.key
                            return@forEachIndexed
                        }
                    }
                    onComplete(uniquekey.toString())
                }

                override fun onCancelled(error: DatabaseError) {


                }
            })
        }
    }
}