package com.vishal.lokal.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.vishal.lokal.databinding.NewscardBinding
import com.vishal.lokal.model.Article

class NewsAdapter : ListAdapter<Article, NewsAdapter.NewsViewHolder>(NewsDiffCallBack()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        return NewsViewHolder(NewscardBinding.inflate(LayoutInflater.from(parent.context),
            parent,
            false))
    }

    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        val newsItem = getItem(position)
        holder.binding.news = NewsViewModel(newsItem)
        Glide.with(holder.itemView).load(newsItem.urlToImage).into(holder.binding.newsimage)
    }

    class NewsViewHolder(val binding: NewscardBinding) : RecyclerView.ViewHolder(binding.root)
}

class NewsViewModel(article: Article) {
    val expanded = ObservableBoolean(false)
    val title = ObservableField(article.title)
    val description = ObservableField(article.description)
    val url = ObservableField(article.url)
    val publishedAt = ObservableField(article.publishedAt)

    fun onClick(_view: View?) {
        expanded.set(!expanded.get())
    }
}

class NewsDiffCallBack : DiffUtil.ItemCallback<Article>() {
    override fun areItemsTheSame(oldItem: Article, newItem: Article): Boolean {
        return oldItem.title == newItem.title
    }

    override fun areContentsTheSame(oldItem: Article, newItem: Article): Boolean {
        return oldItem.title == newItem.title
    }
}