package com.onitsura12.farmdel.presentation.fragments

import androidx.lifecycle.ViewModel
import com.onitsura12.farmdel.utils.AUTH
import com.onitsura12.farmdel.utils.LoginUtils.Companion.setupAccInfo
import com.onitsura12.farmdel.utils.UID


class MainViewModel() : ViewModel() {

    init {
        UID = AUTH.currentUser?.uid.toString()
        setupAccInfo()


    }

//    fun getUser(){
//        viewModelScope.launch {
//            repository.getUser().flowOn(Dispatchers.IO).collect{
//                USER = it
//                Log.i("flow", USER.toString())
//            }
//        }
//    }


}