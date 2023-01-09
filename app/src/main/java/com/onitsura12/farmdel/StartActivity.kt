package com.onitsura12.farmdel

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.onitsura12.farmdel.databinding.ActivityMainBinding
import com.onitsura12.farmdel.databinding.ActivityStartBinding

class StartActivity : AppCompatActivity() {

    private lateinit var binding: ActivityStartBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStartBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}