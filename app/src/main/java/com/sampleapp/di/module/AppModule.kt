package com.sampleapp.di.module

import android.app.Application
import com.sampleapp.SampleApplication
import com.sampleapp.activity.BaseActivity
import com.sampleapp.api.ApiInterface
import com.sampleapp.api.ApiManager
import com.sampleapp.utils.NetworkUtils
import com.sampleapp.utils.PrefUtils
import com.sampleapp.utils.common.FontCache
import com.sampleapp.utils.permission.PermissionUtil
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

/**
 *
 *
 */
@Module
class AppModule(application: SampleApplication) {
    private val mApplication: SampleApplication = application

    @Provides
    @Singleton
    fun providesApplication(): Application {
        return mApplication
    }

    // Dagger will only look for methods annotated with @Provides
    @Provides
    @Singleton
    fun  // Application reference must come from AppModule.class
            providesSharedPreferences(): PrefUtils {
        return PrefUtils()
    }

    @Provides
    @Singleton
    fun provideBaseAppActivity(baseActivity: BaseActivity): BaseActivity {
        return baseActivity
    }

    @Provides
    @Singleton
    fun provideApiManager(apiInterface: ApiInterface): ApiManager {
        return ApiManager(apiInterface)
    }

    /*@Provides
    @Singleton
    fun provideFileUtils(): FileUtils {
        return FileUtils()
    }*/

    @Provides
    @Singleton
    fun provideNetworkUtils(): NetworkUtils {
        return NetworkUtils(mApplication.getApplicationContext())
    }

    @Provides
    @Singleton
    fun providePermissionUtil(): PermissionUtil {
        return PermissionUtil()
    }

    @Provides
    @Singleton
    fun provideFontCache(): FontCache {
        return FontCache(mApplication.getApplicationContext())
    }

}