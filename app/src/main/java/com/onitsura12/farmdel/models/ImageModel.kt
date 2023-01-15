package com.onitsura12.farmdel.models

import android.net.Uri

data class ImageModel(
    val path: Uri,
    var isChecked: Boolean,
)