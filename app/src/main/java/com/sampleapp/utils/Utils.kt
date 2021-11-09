package com.sampleapp.utils

import android.content.Context
import android.util.DisplayMetrics
import android.util.Patterns
import com.sampleapp.utils.common.AppConstant
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*
import java.util.regex.Pattern


object Utils {
    fun validEmail(email: String): Boolean {
        val pattern: Pattern = Patterns.EMAIL_ADDRESS
        return pattern.matcher(email).matches()
    }

    fun convertToLocalTime(
        timestamp: Long,
        format: String = AppConstant.DateTime.DD_MM_YYYY
    ): String {
        val utcDate = Date(timestamp * 1000)
        val df: DateFormat = SimpleDateFormat(format, Locale.getDefault())
        df.timeZone = TimeZone.getTimeZone(TimeZone.getDefault().id)
        return df.format(utcDate)
    }

    fun dpToPx(context: Context, dp: Int): Int {
        val displayMetrics: DisplayMetrics = context.getResources().getDisplayMetrics()
        return Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT))
    }
}