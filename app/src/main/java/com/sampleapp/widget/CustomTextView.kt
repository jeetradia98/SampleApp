package com.sampleapp.widget

import android.content.Context
import android.content.res.TypedArray
import android.graphics.Typeface
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView
import com.sampleapp.SampleApplication
import com.sampleapp.R
import com.sampleapp.utils.common.FontCache

open class CustomTextView : AppCompatTextView {
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

    protected fun init(attrs: AttributeSet?) {
        if (attrs != null) {
            val attributes: TypedArray = context.obtainStyledAttributes(
                attrs,
                R.styleable.CustomTextView
            )
            val fontName: String? = attributes.getString(R.styleable.CustomTextView_fontName)
            if (fontName != null) {
                val typeface: Typeface? = mFontCache.getTypeface(fontName)
                setTypeface(typeface)
            }
            attributes.recycle()
        }
    }
}