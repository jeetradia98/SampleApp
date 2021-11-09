package com.sampleapp

import android.app.Application
import androidx.multidex.MultiDex
import com.sampleapp.di.component.AppComponent
import com.sampleapp.di.component.DaggerAppComponent
import com.sampleapp.di.module.AppModule
import com.sampleapp.di.module.NetworkModule
import com.sampleapp.di.module.TwitterModule

class SampleApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        MultiDex.install(this)
        component = DaggerAppComponent.builder()
            .twitterModule(TwitterModule())
            .appModule(AppModule(this))
            .networkModule(NetworkModule())
            .build()
        /*if (AppConstant.IS_DEBUGGABLE) {
            Timber.plant(DebugTree())
        } else {
            Timber.plant(CrashReportingTree())
        }*/
    }

   companion object {
       private lateinit var component: AppComponent

       fun getAppComponent(): AppComponent {
           return component
       }
   }
}