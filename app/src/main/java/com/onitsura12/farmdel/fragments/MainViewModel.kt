package com.onitsura12.farmdel.fragments

import androidx.lifecycle.ViewModel
import com.onitsura12.data.storage.firebase.utils.AUTH
import com.onitsura12.data.storage.firebase.utils.UID
import com.onitsura12.farmdel.utils.LoginUtils.Companion.setupAccInfo


class MainViewModel : ViewModel() {

    init {
        UID = AUTH.currentUser?.uid.toString()
        setupAccInfo()
    }
}