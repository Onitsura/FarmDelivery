package com.onitsura12.farmdel.fragments.shop


import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.onitsura12.data.storage.firebase.utils.FirebaseHelper.Companion.NODE_SUPPLIES
import com.onitsura12.data.storage.firebase.utils.FirebaseHelper.Companion.REF_DATABASE_ROOT
import com.onitsura12.domain.models.ShopItem
import com.onitsura12.farmdel.models.ImageModel
import java.io.*
import kotlin.random.Random


class AddShopItemViewModel : ViewModel() {

    val imagePath: MutableLiveData<String> = MutableLiveData()
    val imageUrl: MutableLiveData<String> = MutableLiveData()
    val imagesList: MutableLiveData<ArrayList<String>> = MutableLiveData()

    init {
        imagesList.value = arrayListOf()
    }




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

    fun addShopItem(newItem: ShopItem, context: Context){
        REF_DATABASE_ROOT
            .child(NODE_SUPPLIES)
            .child(newItem.title)
            .setValue(newItem)
            .addOnCompleteListener {
                Toast.makeText(context, "Товар добавлен", Toast.LENGTH_SHORT).show()
            }

    }

    fun createShopItem(
        title: String,
        cost: String = "0",
        imagePath: String?,
        weight: String? = "0",
        deliveryDate: String?,
        description: String?,
        imagesArray: ArrayList<String>
    ): ShopItem {
        return ShopItem(
            title = title,
            cost = cost,
            imagePath = imagePath,
            count = "0",
            weight = weight,
            deliveryDate = deliveryDate,
            description = description,
            imagesArray = imagesArray
        )
    }

    fun stringToImageModel(list: ArrayList<String>): ArrayList<ImageModel>{
        val newList = arrayListOf<ImageModel>()
        for (i in list.indices){
            newList.add(ImageModel(path = Uri.parse(list[i]), isChecked = false))
        }
        return newList
    }

    fun getRandomName(): Int{
        return Random.nextInt(80000)
    }




}





