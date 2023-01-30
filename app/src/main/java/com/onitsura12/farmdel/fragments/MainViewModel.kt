package com.onitsura12.farmdel.fragments

import android.util.Log
import androidx.lifecycle.ViewModel
import com.onitsura12.data.storage.firebase.utils.AUTH

import com.onitsura12.data.storage.firebase.utils.FirebaseHelper.Companion.initFirebase
import com.onitsura12.data.storage.firebase.utils.UID
import com.onitsura12.farmdel.utils.LoginUtils.Companion.setupAccInfo
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


class MainViewModel : ViewModel() {

    init {
        UID = AUTH.currentUser?.uid.toString()
        setupAccInfo()
    }
}