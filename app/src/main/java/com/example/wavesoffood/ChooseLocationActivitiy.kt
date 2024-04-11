package com.example.wavesoffood

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import com.example.wavesoffood.databinding.ActivityChooseLocationActivitiyBinding

class ChooseLocationActivitiy : AppCompatActivity() {
    private val binding:ActivityChooseLocationActivitiyBinding by lazy {
        ActivityChooseLocationActivitiyBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        val locationList:Array<String> = arrayOf("jaipur","Odisha","Kolkata","Pune")
        val adapter = ArrayAdapter(this,android.R.layout.simple_list_item_1,locationList)
        val autoCompleteTextView=binding.listoflocation
        autoCompleteTextView.setAdapter(adapter)
    }
}