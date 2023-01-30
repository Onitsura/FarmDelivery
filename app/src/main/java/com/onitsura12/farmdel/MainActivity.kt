package com.onitsura12.farmdel

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.activityViewModels
import com.google.firebase.iid.FirebaseInstanceIdReceiver
import com.google.firebase.iid.internal.FirebaseInstanceIdInternal
import com.google.firebase.installations.FirebaseInstallations
import com.google.firebase.messaging.FirebaseMessaging
import com.onitsura12.farmdel.databinding.ActivityMainBinding
import com.onitsura12.data.storage.firebase.utils.FirebaseHelper
import com.onitsura12.data.storage.firebase.utils.FirebaseHelper.Companion.initFirebase
import com.onitsura12.farmdel.fragments.MainViewModel
import com.onitsura12.farmdel.utils.notification.PushService
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)

        initFirebase()
        PushService.sharedPref = getSharedPreferences("sharedPref", Context.MODE_PRIVATE)
        FirebaseMessaging.getInstance().token.addOnSuccessListener { result ->
            if(result != null){
                PushService.token = result
            }
        }
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        setContentView(binding.root)

    }

}