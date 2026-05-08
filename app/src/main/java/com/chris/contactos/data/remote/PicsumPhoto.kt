package com.chris.contactos.data.remote

import com.google.gson.annotations.SerializedName

data class PicsumPhoto(
    val id: String,
    val author: String,
    @SerializedName("download_url") val downloadUrl: String
)
