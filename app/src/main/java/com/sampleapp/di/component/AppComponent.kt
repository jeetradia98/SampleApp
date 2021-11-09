package com.sampleapp.di.component

import android.app.Application
import com.sampleapp.activity.BaseActivity
import com.sampleapp.api.ApiManager
import com.sampleapp.api.RetrofitInterceptor
import com.sampleapp.di.module.AppModule
import com.sampleapp.di.module.NetworkModule
import com.sampleapp.di.module.TwitterModule
import com.sampleapp.model.base.BaseModel
import com.sampleapp.utils.NetworkUtils
import com.sampleapp.utils.PrefUtils
import com.sampleapp.utils.common.FontCache
import com.sampleapp.utils.permission.PermissionUtil
import dagger.Component
import javax.inject.Singleton

/**
 *
 *
 */
@Singleton
@Component(modules = [AppModule::class, NetworkModule::class, TwitterModule::class])
interface AppComponent {
    fun inject(activity: BaseActivity)

    fun inject(baseModel: BaseModel);
    fun inject(interceptor: RetrofitInterceptor)
    fun provideApiManager(): ApiManager
    fun providePrefUtil(): PrefUtils
    fun provideApplication(): Application

    //    fun provideFileUtils(): FileUtils?
    fun provideNetworkUtils(): NetworkUtils
    fun providePermissionUtil(): PermissionUtil
    fun provideFontCache(): FontCache
}