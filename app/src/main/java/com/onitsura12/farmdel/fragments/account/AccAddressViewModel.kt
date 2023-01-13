package com.onitsura12.farmdel.fragments.account

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.onitsura12.domain.models.Address
import com.onitsura12.domain.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AccAddressViewModel @Inject constructor(userRepository: UserRepository) : ViewModel() {

    private val _addressList: MutableLiveData<ArrayList<Address>> = MutableLiveData()
    val addressList: LiveData<ArrayList<Address>> = _addressList


    init{
        _addressList.value = ArrayList()
    }


    fun convertToAddress(list: ArrayList<String?>): Address {
        return Address(
            city = list[0],
            street = list[1],
            entrance = list[2],
            floor = list[3],
            flat = list[4],
            id = list[5]!!
        )
    }

}