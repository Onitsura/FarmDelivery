package com.onitsura12.farmdel.fragments.bottomsheet


import android.content.ContentResolver
import android.content.ContentUris
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.onitsura12.farmdel.models.ImageModel


@RequiresApi(Build.VERSION_CODES.O)
class BottomSheetViewModel(contentResolver: ContentResolver) : ViewModel() {

    private val _imagesList: MutableLiveData<ArrayList<ImageModel>> = MutableLiveData()
    val imagesList: LiveData<ArrayList<ImageModel>> = _imagesList
    val selectedImage: MutableLiveData<String> = MutableLiveData()


    init {
        getImages(contentResolver = contentResolver)
    }

    fun markItem(item: ImageModel) {
        if (_imagesList.value!!.contains(item)) {
            check(item = item)
        }
    }

    private fun check(item: ImageModel) {
        val oldList = ArrayList(_imagesList.value!!)
        val index = _imagesList.value!!.indexOfFirst { it.path == item.path }
        if (index == -1) return
        if (item.isChecked) {
            val newItem: ImageModel = oldList[index].copy(isChecked = false)
            oldList[index] = newItem
            _imagesList.value = oldList
        } else {
            val newItem = oldList[index].copy(isChecked = true)
            oldList[index] = newItem
            _imagesList.value = oldList
        }


    }


    @RequiresApi(Build.VERSION_CODES.O)
    private fun getImages(contentResolver: ContentResolver) {

        val list: ArrayList<ImageModel> = ArrayList()

        val collection: Uri = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL)
        } else {
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        }

        val projection: Array<String> = arrayOf(MediaStore.Images.Media._ID)

        contentResolver.query(
            collection, projection, null, null
        ).use { cursor ->
            val idColumn: Int = cursor!!.getColumnIndexOrThrow(MediaStore.Images.Media._ID)


            while (cursor.moveToNext()) {
                val id: Long = cursor.getLong(idColumn)
                val contentUri: Uri = ContentUris.withAppendedId(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id
                )

                list.add(ImageModel(path = contentUri, isChecked = false))
            }
            cursor.close()
        }
        val imageList: ArrayList<ImageModel> = ArrayList(list.reversed())
        _imagesList.value = imageList
    }


}