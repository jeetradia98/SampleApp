package com.sampleapp.di.module

import android.content.Context
import android.view.inputmethod.InputMethodManager
import com.sampleapp.api.ApiInterface
import com.sampleapp.di.scopes.ViewScope
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import javax.inject.Singleton

/**
 *
 *
 */
@Module
class TwitterModule {
    @Provides
    @Singleton
    fun provideRetrofit(retrofit: Retrofit): ApiInterface {
        return retrofit.create(ApiInterface::class.java)
    }

    @ViewScope
    @Provides
    fun provideInputMethodManager(context: Context): InputMethodManager {
        return context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    }
}