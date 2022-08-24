package com.lads.truecaller.interfaces

import com.lads.truecaller.model.ApiDataItem
import retrofit2.Call
import retrofit2.http.GET

interface ApiInterface {

    @GET("posts")
    fun getData(): Call<List<ApiDataItem>>
}