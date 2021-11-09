package com.sampleapp.api

import com.sampleapp.model.base.BaseModel
import com.sampleapp.utils.common.ApiConstants
import io.reactivex.rxjava3.core.Observable
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

/**
 * list of api goes here
 */
interface ApiInterface {
    @FormUrlEncoded
    @POST(ApiConstants.EndPoints.USER_LOGIN)
    fun login(
        @Field(ApiConstants.params.EMAIL) email: String?,
        @Field(ApiConstants.params.PASSWORD) password: String?
    ): Observable<BaseModel>

}
