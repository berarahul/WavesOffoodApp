//package com.example.wavesoffood.adapter
//
//import android.view.LayoutInflater
//import android.view.ViewGroup
//import androidx.recyclerview.widget.RecyclerView
//import com.example.wavesoffood.databinding.BuyagainitemBinding
//
//class BuyagainAdapter (private val buyagainfoodname:ArrayList<String>,private val buyagainfoodprice:ArrayList<String>,
//private val buyagainfoodimage:ArrayList<Int>):
//    RecyclerView.Adapter<BuyagainAdapter.BuyagainViewHolder>() {
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BuyagainAdapter.BuyagainViewHolder {
//val binding=BuyagainitemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
//
//        return BuyagainViewHolder(binding)
//    }
//
//
//
//
//    override fun getItemCount(): Int {
//        class BuyagainViewHolder(private val binding: BuyagainitemBinding) : RecyclerView.ViewHolder
//            (binding.root) {
//            fun bind(foodName: String, foodPrice: String, foodImage: Int) {
//
//binding.BuyAgainFoodName.text=foodName
//                binding.BuyAgainFoodPrice.text=foodPrice
//                binding.BuyAgainFoodImage.setImageResource(foodImage)
//            }
//        }
//    }
//
//    override fun onBindViewHolder(holder: BuyagainAdapter.BuyagainViewHolder, position: Int) {
//       holder.bind(buyagainfoodname[position],buyagainfoodprice[position],buyagainfoodimage[position])
//    }
//    class BuyagainViewHolder {
//
//
//    }
//}

package com.example.wavesoffood.adapter

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.wavesoffood.databinding.BuyagainitemBinding

class BuyagainAdapter(
    private val buyagainfoodname: MutableList<String>,
    private val buyagainfoodprice:  MutableList<String>,
    private val buyagainfoodimage:  MutableList<String>,
    private val reQuireContext: Context
) :
    RecyclerView.Adapter<BuyagainAdapter.BuyagainViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BuyagainAdapter.BuyagainViewHolder {
        val binding =
            BuyagainitemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return BuyagainViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return buyagainfoodname.size // Fix: Use the size of any of the ArrayLists, assuming they all have the same size
    }

    override fun onBindViewHolder(holder: BuyagainAdapter.BuyagainViewHolder, position: Int) {
        holder.bind(
            buyagainfoodname[position],
            buyagainfoodprice[position],
            buyagainfoodimage[position]
        )
    }

   inner class BuyagainViewHolder(private val binding: BuyagainitemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(foodName: String, foodPrice: String, foodImage: String) {
            binding.BuyAgainFoodName.text = foodName
            binding.BuyAgainFoodPrice.text = foodPrice
         val uriString:String=foodImage
            val uri:Uri= Uri.parse(uriString)
            Glide.with(reQuireContext).load(uri).into(binding.BuyAgainFoodImage)
        }
    }
}
