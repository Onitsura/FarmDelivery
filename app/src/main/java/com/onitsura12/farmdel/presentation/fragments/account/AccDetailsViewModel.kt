package com.onitsura12.farmdel.presentation.fragments.account

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.onitsura12.farmdel.domain.models.User
import com.onitsura12.farmdel.utils.FirebaseHelper.Companion.CHILD_FULLNAME
import com.onitsura12.farmdel.utils.FirebaseHelper.Companion.CHILD_PHONE
import com.onitsura12.farmdel.utils.FirebaseHelper.Companion.NODE_USERS
import com.onitsura12.farmdel.utils.REF_DATABASE_ROOT
import com.onitsura12.farmdel.utils.UID
import com.onitsura12.farmdel.utils.USER
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AccDetailsViewModel @Inject constructor() : ViewModel() {
    private val _isPhoneCorrect: MutableLiveData<Boolean> = MutableLiveData()
    val isPhoneCorrect: LiveData<Boolean> = _isPhoneCorrect
    private val _user: MutableLiveData<User> = MutableLiveData()
    val user: LiveData<User> = _user


    init {
        _user.value = USER
    }


    fun checkPhone(phone: String) {
        _isPhoneCorrect.value = false
        if (phone.length == 10 || phone.length == 11 || phone.length == 12) {
            _isPhoneCorrect.value = true
        }
    }

    fun saveNewName(newName: String, context: Context) {
        REF_DATABASE_ROOT.child(NODE_USERS)
            .child(UID)
            .child(CHILD_FULLNAME)
            .setValue(newName)
            .addOnCompleteListener {
                USER.fullname = newName
                _user.value = USER
                Toast.makeText(context, "Имя сохранено", Toast.LENGTH_SHORT)
                    .show()

            }
    }

    fun saveNewPhone(newPhone: String, context: Context) {
        REF_DATABASE_ROOT.child(NODE_USERS)
            .child(UID)
            .child(CHILD_PHONE)
            .setValue(newPhone)
            .addOnCompleteListener {
                USER.phone = newPhone
                _user.value = USER
                Toast.makeText(context, "Номер сохранён", Toast.LENGTH_SHORT).show()
            }
    }

}