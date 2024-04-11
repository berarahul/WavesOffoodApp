package com.example.wavesoffood.Fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.wavesoffood.R
import com.example.wavesoffood.databinding.FragmentProfileBinding
import com.example.wavesoffood.model.UserModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class profileFragment : Fragment() {
private lateinit var binding: FragmentProfileBinding
    private val auth=FirebaseAuth.getInstance()
    private val database=FirebaseDatabase.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentProfileBinding.inflate(inflater, container, false)
        // Inflate the layout for this fragment

        setuserdata()
        binding.apply {

            name.isEnabled = false
            email.isEnabled = false
            address.isEnabled = false
            phone.isEnabled = false

        binding.editbutton.setOnClickListener {
            name.isEnabled=!name.isEnabled
            email.isEnabled = !email.isEnabled
            address.isEnabled = !address.isEnabled
            phone.isEnabled = !phone.isEnabled
        }

            }

        binding.saveinformationbutton.setOnClickListener {
            val name=binding.name.text.toString()
            val address=binding.address.text.toString()
            val email=binding.email.text.toString()
            val phone=binding.phone.text.toString()

            updateUserdata(name,address,email,phone)
        }
        return binding.root

    }

    private fun updateUserdata(name: String, address: String, email: String, phone: String) {

        val userId:String?=auth.currentUser?.uid
        if (userId!=null){


            val userReferencee:DatabaseReference=database.getReference("user").child(userId)
            val userData= hashMapOf(
                "name" to name,
                "address" to address,
                "email" to email,
                "phone" to phone





            )
            userReferencee.setValue(userData).addOnSuccessListener {
                Toast.makeText(requireContext(),"Profile Update SuccessFully",Toast.LENGTH_SHORT).show()
            }.addOnFailureListener {
                Toast.makeText(requireContext(),"Profile Update Failed",Toast.LENGTH_SHORT).show()

            }
        }
    }


    private fun setuserdata() {
        val userId: String?=auth.currentUser?.uid
        if (userId!=null){

            val userReeference:DatabaseReference=database.getReference("user").child(userId)


            userReeference.addListenerForSingleValueEvent(object :ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()){

                        val userprofile:UserModel?=snapshot.getValue(UserModel::class.java)


                        if (userprofile!=null){

                            binding.name.setText(userprofile.name)
                            binding.address.setText(userprofile.address)
                            binding.email.setText(userprofile.email)
                            binding.phone.setText(userprofile.phone)
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }
            })

        }


    }

    companion object {

    }
}