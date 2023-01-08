package ru.apps.rtrofitfacts

import retrofit2.Call
import retrofit2.http.GET
import ru.apps.rtrofitfacts.API.CatJson

interface ApiRequests {
    @GET("/facts/random")
    fun getCatFacts(): Call<CatJson>
}