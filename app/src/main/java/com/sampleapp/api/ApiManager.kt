package com.sampleapp.api

import android.util.Log
import com.sampleapp.SampleApplication
import com.sampleapp.model.base.BaseModel
import com.sampleapp.model.base.CustomError
import com.sampleapp.utils.NetworkUtils
import com.sampleapp.utils.common.ApiConstants
import com.sampleapp.utils.common.AppConstant
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.annotations.NonNull
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.ObservableTransformer
import io.reactivex.rxjava3.functions.Function
import io.reactivex.rxjava3.functions.Supplier
import io.reactivex.rxjava3.schedulers.Schedulers
import java.net.ConnectException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ApiManager @Inject constructor(private val apiInterface: ApiInterface) {
    private val mNetworkUtils: NetworkUtils =
        SampleApplication.getAppComponent().provideNetworkUtils()

    private fun <T> call(modelObservable: Observable<T>): @NonNull Observable<T> {
        return modelObservable
            .startWith(Observable.defer(Supplier defer@{
                //before calling each api, network connection is checked.
                if (!mNetworkUtils.isConnected) {
                    //if network is not available, it will return error observable with ConnectException.
                    return@defer Observable.error(ConnectException("Device is not connected to network"))
                } else {
                    //if it is available, it will return empty observable. Empty observable just emits onCompleted() immediately
                    return@defer Observable.empty()
                }
            }))
            .flatMap<T>(Function flatMap@{ response: T ->
                if (response is BaseModel) {
                    val baseResponse: BaseModel = response as BaseModel
                    if (baseResponse.status != ApiConstants.ResponseCode.RESPONSE_SUCCESS) {
                        //                            showError(addOfficeResponse.getMessage());
                        val customApiError =
                            baseResponse.message?.let { CustomError(baseResponse.status, it) }
                        return@flatMap Observable.error(customApiError)
                    }
                    return@flatMap Observable.just(response)
                }
                Observable.just(response)
            })
            .doOnNext { response: T ->
                //logging response on success
                //you can change to to something else
                //for example, if all your apis returns error codes in success, then you can throw custom exception here
                if (AppConstant.IS_DEBUGGABLE) {
                    Log.e("---", "Response :\n$response")
                }
            }
            .doOnError { throwable: Throwable ->
                //printing stack trace on error
                if (AppConstant.IS_DEBUGGABLE) {
                    throwable.printStackTrace()
                }
            }
    }

    private fun <T> applySchedulers(): ObservableTransformer<T, T> {
        return ObservableTransformer<T, T> { observable: Observable<T> ->
            observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
        }
    }

    fun login(email: String?, password: String?): Observable<BaseModel> {
        return call(apiInterface.login(email, password)).compose(applySchedulers())
    }
}