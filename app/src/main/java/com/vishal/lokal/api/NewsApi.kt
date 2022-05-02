package com.vishal.lokal.api


import com.vishal.lokal.model.News
import io.reactivex.Single
import retrofit2.http.GET

interface NewsApi {
    @GET("top-headlines?country=in&apiKey=6a1d4f2afd3b4379bd04f56e72a8bb6d")
    fun getQuotes(): Single<News>
}