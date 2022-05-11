package com.vishal.lokal.api


import com.vishal.lokal.model.News
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsApi {
    @GET("top-headlines")
    fun getArticles(
        @Query("country") country: String,
        @Query("category") category: String,
        @Query("apiKey") key: String,
    ): Single<News>

    @GET("everything")
    fun searchArticles(@Query("q") query: String, @Query("apiKey") key: String): Single<News>
}