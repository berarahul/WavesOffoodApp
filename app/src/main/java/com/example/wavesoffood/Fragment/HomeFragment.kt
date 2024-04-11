package com.example.wavesoffood.Fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.denzcoskun.imageslider.constants.ScaleTypes
import com.denzcoskun.imageslider.interfaces.ItemClickListener
import com.denzcoskun.imageslider.models.SlideModel
import com.example.wavesoffood.MenuButtonfragment
import com.example.wavesoffood.R
import com.example.wavesoffood.adapter.menuAdapter
import com.example.wavesoffood.adapter.popularAdapter
import com.example.wavesoffood.databinding.FragmentHomeBinding
import com.example.wavesoffood.model.MenuItem
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener




class HomeFragment : Fragment() {
private lateinit  var binding:FragmentHomeBinding

private lateinit var database: FirebaseDatabase
private lateinit var menuItems:MutableList<MenuItem>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding=FragmentHomeBinding.inflate(inflater,container,false)
        binding.viewallmenu.setOnClickListener{
            val bottomsheetDialog=MenuButtonfragment()
            bottomsheetDialog.show(parentFragmentManager,"Test")
        }

        //retrive popularmenuItem
        retriveandDisplaypopularItem()


        return binding.root
    }

    private fun retriveandDisplaypopularItem() {
        //get reference to the database
        database=FirebaseDatabase.getInstance()
        val foodref:DatabaseReference=database.reference.child("menu")
        menuItems= mutableListOf()
        //retrive menuitem from the database
        foodref.addListenerForSingleValueEvent(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
              for (foodsnapshot:DataSnapshot in snapshot.children){

                  val menuItem:MenuItem? = foodsnapshot.getValue(MenuItem::class.java)
                  menuItem?.let { menuItems.add(it)  }
              }
                //Display Random Popular Item

                RandomPopularitems()
            }

            override fun onCancelled(error: DatabaseError) {

            }


        })
    }

    private fun RandomPopularitems() {
       // Create a shuffled list of menu items

        val index: List<Int> = menuItems.indices.toList().shuffled()
        val numItemtoshow =6
        val subsetmenuitem: List<MenuItem> = index.take(numItemtoshow).map{menuItems[it]}
        setpopularitemsAdapter(subsetmenuitem)
    }

    private fun setpopularitemsAdapter(subsetmenuitem: List<MenuItem>) {
        val adapter = menuAdapter(subsetmenuitem, requireContext())
        binding.popularrecyclerview.layoutManager = LinearLayoutManager(requireContext())
        binding.popularrecyclerview.adapter = adapter
    }




    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val imageList=ArrayList<SlideModel>()
        imageList.add(SlideModel(R.drawable.banner1,scaleType = ScaleTypes.FIT))
        imageList.add(SlideModel(R.drawable.banner2,scaleType = ScaleTypes.FIT))
        imageList.add(SlideModel(R.drawable.banner3,scaleType = ScaleTypes.FIT))


        val imageslider=binding.imageSlider
        imageslider.setImageList(imageList)
        imageslider.setImageList(imageList,ScaleTypes.FIT)
        imageslider.setItemClickListener(object : ItemClickListener{
            override fun doubleClick(position: Int) {
                TODO("Not yet implemented")
            }

            override fun onItemSelected(position: Int) {

                val itemposition=imageList[position]
                val itemMessage="Selected Image $position"
                Toast.makeText(requireContext(),itemMessage,Toast.LENGTH_SHORT).show()
            }
        })


    }
}
