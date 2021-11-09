package com.sampleapp.widget

import android.content.Context
import android.content.res.TypedArray
import android.graphics.Typeface
import android.util.AttributeSet
import androidx.appcompat.widget.SwitchCompat
import com.sampleapp.SampleApplication
import com.sampleapp.R
import com.sampleapp.utils.common.FontCache

/**
 * Created  on 6/8/2017.
 */
class CustomSwitch : SwitchCompat {
    private val mFontCache: FontCache = SampleApplication.getAppComponent().provideFontCache()

    constructor(context: Context?, attrs: AttributeSet?, defStyle: Int) : super(
        context!!,
        attrs,
        defStyle
    ) {
        if (!isInEditMode) init(attrs)
    }

    constructor(context: Context?, attrs: AttributeSet?) : super(context!!, attrs) {
        if (!isInEditMode) init(attrs)
    }

    constructor(context: Context?) : super(context!!) {
        if (!isInEditMode) init(null)
    }

    private fun init(attrs: AttributeSet?) {
        if (attrs != null) {
            val attributes: TypedArray = context.obtainStyledAttributes(
                attrs,
                R.styleable.CustomSwitch
            )
            val fontName: String? = attributes.getString(R.styleable.CustomSwitch_fontName)
            if (fontName != null) {
                val typeface: Typeface? = mFontCache.getTypeface(fontName)
                setTypeface(typeface)
            }
            attributes.recycle()
        }
    }
}