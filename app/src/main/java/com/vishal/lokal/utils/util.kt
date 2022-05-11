package com.vishal.lokal.utils

import android.app.Activity
import android.util.Log
import android.view.View
import androidx.databinding.BindingAdapter
import com.vishal.lokal.App
import com.vishal.lokal.di.AppComponent
import com.vishal.lokal.model.Article
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import java.text.SimpleDateFormat
import java.util.*


fun Disposable.addTo(compositeDisposable: CompositeDisposable) {
    compositeDisposable.add(this)
}

fun Activity.injector(): AppComponent {
    return (application as App).component
}

fun objectToString(article: Article): String {
    return article.publishedAt

}

fun convertToLocalTime(timeString: String): String {
    val format = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'")
    return (format.parse(timeString) ?: Date()).toLocaleString()
}

@BindingAdapter("visibility")
fun setVisibility(view: View, visible: Boolean?) {
    view.visibility = when (visible) {
        true -> View.VISIBLE
        false -> View.GONE
        null -> View.GONE
    }
}

class Logger(val name: Any) {
    fun debug(message: Any) {
        Log.d(name.toString(), message.toString())
    }

    fun error(message: Any, error: Throwable? = null) {
        Log.e(name.toString(), message.toString(), error)
    }
}