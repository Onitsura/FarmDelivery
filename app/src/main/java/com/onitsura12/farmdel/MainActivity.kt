package com.onitsura12.farmdel

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.onitsura12.farmdel.databinding.ActivityMainBinding
import com.onitsura12.farmdel.utils.FirebaseHelper
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

    }

    override fun onStart() {
        super.onStart()
        FirebaseHelper.initFirebase()
    }
}