package com.onitsura12.farmdel.fragments.account


import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.onitsura12.domain.models.Address
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlin.random.Random


class AccAddAddressViewModel() : ViewModel() {

    val newAddress: MutableLiveData<Address> = MutableLiveData()


    init {


    }


    fun getId(): Int{
        return Random.nextInt(35000)
    }

    fun convertToArrayList(address: Address): ArrayList<String?>{
        return arrayListOf(
            address.city.toString(),
            address.street.toString(),
            address.entrance.toString(),
            address.floor.toString(),
            address.flat.toString(),
            address.id
            )

    }
}