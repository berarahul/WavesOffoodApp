package com.example.wavesoffood

import android.os.Bundle
import android.renderscript.ScriptGroup.Binding
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.wavesoffood.adapter.NotificationAdapter
import com.example.wavesoffood.databinding.FragmentNotificationButtonfragmentBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment


class NotificationButtonfragment : BottomSheetDialogFragment() {
  private lateinit var binding: FragmentNotificationButtonfragmentBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding =
            FragmentNotificationButtonfragmentBinding.inflate(layoutInflater, container, false)
        val notifications= listOf("Your Ordered Has Been Canceled Successfully","Order has been taken by the driver","Congrats Your Order Placed")
        val notificationimages= listOf(R.drawable.sademoji,R.drawable.truck,R.drawable.congrats)
        val adapter=NotificationAdapter(

            ArrayList(notifications),
            ArrayList(notificationimages)
        )
        binding.notificationRecyclerView.layoutManager=LinearLayoutManager(requireContext())
        binding.notificationRecyclerView.adapter=adapter
        return binding.root
    }

    companion object {

    }
}