package com.nikitasutulov.lab6

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface CurrencyApi {
    @GET("NBUStatService/v1/statdirectory/exchange")
    fun getCurrency(
        @Query("valcode") currencyCode: String,
        @Query("date") date: String? = null,
        @Query("json") json: String = "true"
    ): Call<List<Currency>>
}

