package com.vishal.lokal.di

import com.vishal.lokal.MainActivity
import com.weather.di.ContextModule
import com.weather.di.NetModule
import dagger.Component
import javax.inject.Singleton


@Singleton
@Component(modules = [ContextModule::class, NetModule::class])
interface AppComponent {
    fun inject(ma: MainActivity)

}