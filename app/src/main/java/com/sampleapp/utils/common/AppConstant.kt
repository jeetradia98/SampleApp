package com.sampleapp.utils.common

object AppConstant {
    const val IS_DEBUGGABLE = true

    interface SharedPreferences {
        companion object {
            const val USER_MODEL = "userModel"
            const val TOKEN = "token"
        }
    }

    interface DateTime{
        companion object{
            const val DD_MM_YYYY = "dd/MM/yyyy"
            const val MMM_YYYY = "MMM yyyy"
        }
    }
    interface BundleExtra {
        companion object {
        }
    }

    interface Delays {
        companion object {
            const val MIN_TIME_BETWEEN_CLICKS: Long = 200

        }
    }
}