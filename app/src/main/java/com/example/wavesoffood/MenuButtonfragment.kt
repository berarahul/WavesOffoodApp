package com.example.wavesoffood

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.view.menu.MenuAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.wavesoffood.adapter.menuAdapter
import com.example.wavesoffood.databinding.FragmentMenuButtonfragmentBinding
import com.example.wavesoffood.model.MenuItem
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener



class MenuButtonfragment : BottomSheetDialogFragment(){
    private lateinit var binding:FragmentMenuButtonfragmentBinding
private lateinit var database: FirebaseDatabase
private lateinit var menuItems: MutableList<MenuItem>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        binding= FragmentMenuButtonfragmentBinding.inflate(inflater,container,false)

        binding.buttonback.setOnClickListener {
            dismiss()
        }
      retrivemenuItems()

        return binding.root
    }

    private fun retrivemenuItems() {
        database=FirebaseDatabase.getInstance()
        val foodRef:DatabaseReference=database.reference.child("menu")
        menuItems= mutableListOf()
        foodRef.addListenerForSingleValueEvent(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
          for (foodSnapshot:DataSnapshot in snapshot.children){
              val menuItem:Any?=foodSnapshot.getValue(MenuItem::class.java)
              menuItem.let {menuItems.add(it as MenuItem)}

          }
                Log.d("Items","OnDataChange: Data Received")
                setAdapter()
            }

            override fun onCancelled(error: DatabaseError) {


            }
        })
    }

    private fun setAdapter() {
        if (menuItems.isNotEmpty()){
            val adapter=menuAdapter(menuItems,requireContext())
            binding.menuRecyclerview.layoutManager= LinearLayoutManager(requireContext())
            binding.menuRecyclerview.adapter=adapter
            Log.d("Items","SetAdapter: data set")
        }else
        {
            Log.d("Items","SetAdapter: data not set")
        }

    }


}