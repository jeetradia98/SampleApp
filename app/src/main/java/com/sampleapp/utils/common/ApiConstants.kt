package com.sampleapp.utils.common

object ApiConstants {
    var BASE_URL = "https://www.google.com/"

    /**
     * api endpoint listed hereF
     */
    interface EndPoints {
        companion object {
            const val USER_LOGIN = "user/login"
            const val USER_LOGOUT = "user/logout"
            const val USER_REGISTER = "user/register"
            const val USER_FORGOT_PASSWORD = "user/forgot-password"
            const val USER_CHANGE_PASSWORD = "user/change-password"
            const val USER_EDIT_PROFILE = "user/profile/update"
            const val USER_PROFILE = "user/profile"
        }
    }

    /**
     * api parameter listed here
     */
    interface params {
        companion object {
            const val AUTHORIZATION = "Authorization"
            const val EMAIL = "email"
            const val PASSWORD = "password"

            const val FULL_NAME = "name"
            const val NICK_NAME = "nick_name"
            const val PHONE = "phone"
            const val HIDE_NAME = "is_hide_name"

            const val NEW_PASSWORD = "new_password"
            const val NEW_C_PASSWORD = "new_confirm_password"
            const val OLD_PASSWORD = "old_password"
            const val TOKEN = "token"
            const val TYPE = "type"
        }
    }

    interface ResponseCode {
        companion object {
            const val RESPONSE_SUCCESS = 1
            const val RESPONSE_FAIL = 0
            const val NOT_FOUND = 404
            const val CONFLICT = 400
            const val HTTP_SUCCESS = 200
            const val HTTP_INTERNAL_SERVER_ERROR = 500
            const val ACCESS_TOKEN_EXPIRE = 104
            const val HTTP_REQUEST_TIMEOUT = 408
        }
    }
}