package com.example.wavesoffood.Fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.wavesoffood.adapter.menuAdapter
import com.example.wavesoffood.databinding.FragmentSearchBinding
import com.example.wavesoffood.model.MenuItem
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class SearchFragment : Fragment() {
    private lateinit var binding: FragmentSearchBinding
    private lateinit var adapter: menuAdapter
    private lateinit var database: FirebaseDatabase
    private val originalMenuItems= mutableListOf<MenuItem>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        binding = FragmentSearchBinding.inflate(inflater, container, false)

   //retrive menu items from database
           retriveMenuItem()
        //setup for searchview
        setupsearchview()


        return binding.root
    }

    private fun retriveMenuItem() {
       // get database reference
        database=FirebaseDatabase.getInstance()
        //reference to the menu node
        val foodreference:DatabaseReference=database.reference.child("menu")
        foodreference.addListenerForSingleValueEvent(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
               for (foodsnapshot:DataSnapshot in snapshot.children){
                   val menuitems: MenuItem?=foodsnapshot.getValue(MenuItem::class.java)
                   menuitems?.let {
                       originalMenuItems.add(it)
                   }
               }
                showallMenu()
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
    }

    private fun showallMenu() {
       val filteredMenuItems:ArrayList<MenuItem> = ArrayList(originalMenuItems)
        setAdapter(filteredMenuItems)
    }

    private fun setAdapter(filteredMenuItems: List<MenuItem>) {

        adapter= menuAdapter(filteredMenuItems,requireContext())
        binding.menuRecyclerview.layoutManager=LinearLayoutManager(requireContext())
        binding.menuRecyclerview.adapter=adapter
    }


    private fun setupsearchview() {


        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
            androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                // Handle the query submission here if needed
                return true

            }

            override fun onQueryTextChange(newText: String): Boolean {
                // Handle the query text change here
                filtermenuitems(newText)
                return true
            }
        })
    }

    private fun filtermenuitems(query: String) {
       val filterMenuitems:List<MenuItem> =originalMenuItems.filter {
           it.foodname?.contains(query,ignoreCase = true)==true
       }
setAdapter(filterMenuitems)
        }

    }




