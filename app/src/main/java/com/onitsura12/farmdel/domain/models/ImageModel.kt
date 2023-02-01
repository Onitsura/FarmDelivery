package com.onitsura12.farmdel.domain.models

import android.net.Uri

data class ImageModel(
    val path: Uri,
    var isChecked: Boolean,
)