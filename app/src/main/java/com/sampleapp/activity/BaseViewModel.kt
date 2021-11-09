package com.sampleapp.activity

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.sampleapp.SampleApplication
import com.sampleapp.api.ApiManager

open class BaseViewModel(application: Application) : AndroidViewModel(application) {

    var loading: MutableLiveData<Boolean> = MutableLiveData()
    var throwable: MutableLiveData<Throwable> = MutableLiveData()
    var tempClick: MutableLiveData<Boolean> = MutableLiveData()

    var apiManager: ApiManager = SampleApplication.getAppComponent().provideApiManager()
}