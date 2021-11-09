package com.sampleapp.model.base

class CustomError(var errorCode: Int, var error: String) : RuntimeException()