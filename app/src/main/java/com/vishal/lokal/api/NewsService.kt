package com.vishal.lokal.api

import android.content.Context
import com.vishal.lokal.model.News
import io.reactivex.Single
import javax.inject.Inject

class NewsService @Inject constructor(private val context: Context, private val api: NewsApi) {

    fun get(query: String? = null): Single<News> {
        return if (query.isNullOrBlank()) headlines() else search(query)
    }

    fun headlines(): Single<News> {
        return api.getArticles(country(), API_KEY)
    }

    fun search(query: String): Single<News> {
        return api.searchArticles(query, API_KEY)
    }

    private fun country(): String {
        context.resources.configuration.locales[0].country
        return context.resources.configuration.locale.country
    }

    companion object {
        private val API_KEY = "6a1d4f2afd3b4379bd04f56e72a8bb6d"
    }

}