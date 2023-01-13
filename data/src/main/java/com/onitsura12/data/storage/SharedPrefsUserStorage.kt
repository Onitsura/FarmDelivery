package com.onitsura12.data.storage

import android.content.Context
import com.onitsura12.data.UserStorage
import com.onitsura12.data.models.StorageUserModel

private const val SHARED_PREFS_NAME = "shared_prefs_name"
private const val KEY_FIRST_NAME = "firstName"
private const val KEY_LAST_NAME = "lastName"
private const val KEY_PHONE = "phone"
private const val KEY_EMAIL = "email"


class SharedPrefsUserStorage(context: Context) : UserStorage {

    private val sharedPreferences = context.getSharedPreferences(SHARED_PREFS_NAME, Context.MODE_PRIVATE)

    override fun saveUser(user: StorageUserModel) {

        sharedPreferences.edit().putString(KEY_FIRST_NAME, user.name).apply()
        sharedPreferences.edit().putString(KEY_LAST_NAME, user.lastName).apply()
        sharedPreferences.edit().putString(KEY_PHONE, user.phone).apply()
        sharedPreferences.edit().putString(KEY_EMAIL, user.eMail).apply()

    }

    override fun getUser(): StorageUserModel {
        val firstName = sharedPreferences.getString(KEY_FIRST_NAME, "")
        val lastName = sharedPreferences.getString(KEY_LAST_NAME, "")
        val phone = sharedPreferences.getString(KEY_PHONE, "")
        val email = sharedPreferences.getString(KEY_EMAIL, "")

        return StorageUserModel(
            name = firstName,
            lastName = lastName,
            phone = phone!!,
            eMail = email,
            cart = emptyList(),
            address = null,
            orders = emptyList()
        )
    }
}