package com.example.pokescanner.pricing

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Url

interface PricingService {
    @GET
    fun fetchPricingPage(@Url url: String): Call<ResponseBody>
}
