package com.example.wavesoffood

import android.content.Intent
import android.os.Binder
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.wavesoffood.databinding.ActivityMainBinding
import com.example.wavesoffood.databinding.FragmentCongratsButtonSheetBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment


class CongratsButtonSheet : BottomSheetDialogFragment() {
private lateinit var binding:FragmentCongratsButtonSheetBinding

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding= FragmentCongratsButtonSheetBinding.inflate(layoutInflater,container,false)
        binding.gohome.setOnClickListener {
            val intent=Intent(requireContext(),MainActivity::class.java)
            startActivity(intent)
        }
        return binding.root
    }

    companion object {

    }
}