package com.jasall.retrofitdemo.retrofit

import retrofit2.http.GET

interface ProductApi {
    @GET("products/1")
    suspend fun getProductById(): Product
}