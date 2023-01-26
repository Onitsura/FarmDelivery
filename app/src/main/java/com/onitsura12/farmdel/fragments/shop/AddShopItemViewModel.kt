package com.onitsura12.farmdel.fragments.shop


import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.onitsura12.data.storage.firebase.utils.FirebaseHelper
import com.onitsura12.data.storage.firebase.utils.FirebaseHelper.Companion.NODE_SUPPLIES
import com.onitsura12.data.storage.firebase.utils.FirebaseHelper.Companion.REF_DATABASE_ROOT
import com.onitsura12.domain.models.ShopItem
import java.io.*


class AddShopItemViewModel : ViewModel() {

    val imagePath: MutableLiveData<String> = MutableLiveData()
    val imageUrl: MutableLiveData<String> = MutableLiveData()




    fun createCopyAndReturnRealPath(context: Context, uri: Uri): String? {
        val contentResolver: ContentResolver = context.contentResolver ?: return null


        val filePath: String = context.applicationInfo.dataDir + File.separator + "temp_file"
        val file = File(filePath)
        try {
            val inputStream: InputStream = contentResolver.openInputStream(uri) ?: return null
            val outputStream: OutputStream = FileOutputStream(file)
            val buf = ByteArray(1024)
            var len: Int
            while (inputStream.read(buf).also { len = it } > 0) outputStream.write(buf, 0, len)
            outputStream.close()
            inputStream.close()
        } catch (ignore: IOException) {
            return null
        }
        return file.absolutePath
    }

    fun addShopItem(newItem: ShopItem){
        REF_DATABASE_ROOT
            .child(NODE_SUPPLIES)
            .child(newItem.title)
            .setValue(newItem)
    }



}





