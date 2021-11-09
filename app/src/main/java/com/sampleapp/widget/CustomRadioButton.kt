package com.sampleapp.widget

import android.content.Context
import android.content.res.TypedArray
import android.graphics.Typeface
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatRadioButton
import com.sampleapp.SampleApplication
import com.sampleapp.R
import com.sampleapp.utils.common.FontCache

/**
 * Created on 5/7/2016.
 */
class CustomRadioButton : AppCompatRadioButton {
    private val mFontCache: FontCache = SampleApplication.getAppComponent().provideFontCache()

    constructor(context: Context?) : super(context) {
        if (!isInEditMode) init(null)
    }

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
        if (!isInEditMode) init(attrs)
    }

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        if (!isInEditMode) init(attrs)
    }

    private fun init(attributeSet: AttributeSet?) {
        if (attributeSet == null) return
        val attributes: TypedArray =
            context.obtainStyledAttributes(attributeSet, R.styleable.CustomRadioButton)
        val fontName: String? = attributes.getString(R.styleable.CustomRadioButton_fontName)
        if (fontName != null && !isInEditMode) {
            val typeface: Typeface? = mFontCache.getTypeface(fontName)
            setTypeface(typeface)
        }
        attributes.recycle()
    }
}