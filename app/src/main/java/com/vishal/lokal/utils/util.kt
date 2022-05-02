package com.vishal.lokal.utils

import android.app.Activity
import android.view.View
import androidx.databinding.BindingAdapter
import com.vishal.lokal.App
import com.vishal.lokal.di.AppComponent
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

fun Long.toHour(): String {
    return Date(this).hours.toString()
}

fun convertLongToTime(time: Long): String {
    val date = Date(time)
    val format = SimpleDateFormat("dd/MMM/yy HH:mm:ss")
    return format.format(date)
}

@BindingAdapter("visibility")
fun setVisibility(view: View, visible: Boolean?) {
    view.visibility = when (visible) {
        true -> View.VISIBLE
        false -> View.GONE
        null -> View.GONE
    }
}