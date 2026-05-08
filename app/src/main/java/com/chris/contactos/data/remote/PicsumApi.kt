package com.chris.contactos.data.remote

import retrofit2.http.GET
import retrofit2.http.Query

interface PicsumApi {
    @GET("v2/list")
    suspend fun getPhotos(@Query("limit") limit: Int = 100): List<PicsumPhoto>
}