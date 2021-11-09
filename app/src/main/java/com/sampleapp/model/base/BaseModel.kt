package com.sampleapp.model.base

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

//import com.google.auto.value.AutoValue;
//import com.google.gson.Gson;
//import com.google.gson.TypeAdapter;
//@AutoValue
open class BaseModel {
    @SerializedName("status")
    @Expose
    var status = 0

    @SerializedName("message")
    @Expose
    var message: String? = null
}