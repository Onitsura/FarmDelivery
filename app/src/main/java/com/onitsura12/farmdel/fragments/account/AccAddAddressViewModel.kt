package com.onitsura12.farmdel.fragments.account


import androidx.lifecycle.ViewModel
import kotlin.random.Random


class AccAddAddressViewModel : ViewModel() {

    fun getId(): Int{
        return Random.nextInt(35000)
    }

}