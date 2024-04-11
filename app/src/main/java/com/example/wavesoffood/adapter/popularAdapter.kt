package com.example.wavesoffood.adapter
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.wavesoffood.DetailsActivity
import com.example.wavesoffood.databinding.PopularitemBinding
import com.example.wavesoffood.model.MenuItem

class popularAdapter(private val items: List<MenuItem>,
                     private val image: Context,
                     private val price:List<String>,
                     private val requireContext:Context):
    RecyclerView.Adapter<popularAdapter.popularViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): popularAdapter.popularViewHolder {
       return popularViewHolder(PopularitemBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: popularAdapter.popularViewHolder, position: Int) {
        val item= items[position]
//        val images= image[position]
        val pricee=price[position]
//        holder.bind(item,images,pricee)
        holder.itemView.setOnClickListener {
            val intent= Intent(requireContext, DetailsActivity::class.java)
//            intent.putExtra("MenuItemName",item)
//            intent.putExtra("MenuItmImage",images)
            requireContext.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
      return items.size
    }
    class popularViewHolder (private val binding:PopularitemBinding):RecyclerView.ViewHolder(binding.root) {
      private  val imagesView=binding.foodimage
        fun bind(item: String, images: Int, pricee: String) {
             binding.foodname.text=item
            binding.foodprice.text=pricee
            imagesView.setImageResource(images)
        }

    }

}

