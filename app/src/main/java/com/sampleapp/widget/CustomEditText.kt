package com.sampleapp.widget

import android.content.Context
import android.view.KeyEvent
import android.view.View
import java.util.*
import android.content.res.TypedArray
import android.graphics.Typeface
import android.text.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import androidx.annotation.StringRes
import androidx.appcompat.widget.AppCompatEditText
import com.sampleapp.SampleApplication
import com.sampleapp.R
import com.sampleapp.utils.common.FontCache

/**
 * **implemented filter types: **
 *
 *
 * **EMAIL : **
 *
 *  *  must be in lower case
 *  *  must not have character other than defined in allowedEmailChars
 *
 * **PHONE**
 *
 *  *  must not start with zero
 *  *  must allow only 0 to 9 and plus sign
 *  *  must not allow + sign after first char
 *
 * **full name**
 *
 *  *  must be alphabetic.
 *  * may include space
 *
 * **first or last name**
 *
 *  *  must be alphabetic
 *  *  must not include space
 *
 * **user name**
 *
 *  *  can be either alphabets or numbers (alphanumeric)
 *  *  may include space
 *
 * **ADDRESS/STATE/zip code**
 *
 *  * must not have character other than defined in allowedAddressNameChars
 *
 * **PASSWORD**
 *
 *  * no filters
 *
 * **company name**
 *
 *  * must not have character other than defined in allowedCompanyNameChars
 *
 * **account name**
 *
 *  * must not have character other than defined in allowedCompanyNameChars
 *  * must not start with 0-9,dot,slash,comma or dash symbols
 *
 * **bank name**
 *
 *  * must not have character other than defined in allowedCompanyNameChars
 *  * must not start with 0-9,dot,slash,comma or dash symbols
 *
 * **ADDRESS/STATE/zip code**
 *
 *  * must not have character other than defined in NO_SPECIAL_CHARS
 *
 * **only alphabetic, only numbers, only alphanumeric**
 *
 *  * 1:[a-z][A-Z][space],  2:[0-9],  3:[a-z][A-Z][0-9][space]
 *
 * To view doc - use CTRL+Q or F2
 */
class CustomEditText : AppCompatEditText {
    private val mFontCache: FontCache = SampleApplication.getAppComponent().provideFontCache()
    private var inputFilters: MutableList<InputFilter>? = null
    private var filterTextWatcher: TextWatcher? = null
    private var occurrenceTextWatcher: TextWatcher? = null

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
        inputFilters = ArrayList()
        if (attrs != null) {
            //for not allowing first charactor as space by default
            val arrEditView: TypedArray =
                context.obtainStyledAttributes(attrs, R.styleable.CustomEditText)
            val fontName: String? = arrEditView.getString(R.styleable.CustomEditText_fontName)
            if (fontName != null && !isInEditMode) {
                val typeface: Typeface? = mFontCache.getTypeface(fontName)
                setTypeface(typeface)
            }
            val allowFirstSpace: Boolean =
                arrEditView.getBoolean(R.styleable.CustomEditText_allowFirstSpace, false)
            if (!allowFirstSpace) {
                filterFirstChars(' ')
            }
            val filterType: Int = arrEditView.getInteger(R.styleable.CustomEditText_filterType, 0)
            filterInput(filterType)
            arrEditView.recycle()
        }

        //get previous input filters
        val editFilters: Array<InputFilter> = filters
        //add them to array list
        Collections.addAll(inputFilters, *editFilters)
        //set [previous + new] input filters
        val arrInputFilters = arrayOfNulls<InputFilter>((inputFilters as ArrayList<InputFilter>).size)
        filters = (inputFilters as ArrayList<InputFilter>).toArray<InputFilter?>(arrInputFilters)
        changeCourserVisibilityAccordingFocus()
    }

    /**
     * adds appropriate input filter according to given type
     *
     * @param filterType filter type
     */
    private fun filterInput(filterType: Int) {
        when (filterType) {
            EMAIL -> {
                inputFilters!!.add(InputFilter { source: CharSequence, start: Int, end: Int, dest: Spanned?, dStart: Int, dEnd: Int ->
                    source.toString().lowercase(Locale.getDefault())
                })
                addStringFilter(R.string.allowedEmailChars)
            }
            PHONE -> {
                filterFirstChars('0')
                addStringFilter(R.string.allowedPhoneChars)
                filterCharactorOccurrence('+', 1)
            }
            FULL_NAME -> addStringFilter(R.string.allowedPersonNameChars)
            FIRST_LAST_NAME -> {
                doNotAllowAnySpace()
                addStringFilter(R.string.allowedFirstLastNameChars)
            }
            USER_NAME -> addStringFilter(R.string.allowedAlphanumericNameChars)
            ADDRESS, ZIP_CODE, STATE -> addStringFilter(R.string.allowedAddressNameChars)
            COMPANY_NAME -> addStringFilter(R.string.allowedCompanyNameChars)
            PASSWORD -> addStringFilter(R.string.allowPassword)
            ALPHA_NUMERIC -> addStringFilter(R.string.allowedAlphanumericNameChars)
            ONLY_ALPHABETS -> addStringFilter(R.string.allowAlphabets)
            ONLY_NUMBERS -> addStringFilter(R.string.allowNumeric)
            NO_SPECIAL_CHARS -> addStringFilter(R.string.noSpecialChars)
            ACCOUNT_NAME -> {
                addStringFilter(R.string.allowedCompanyNameChars)
                filterFirstChars(
                    '0',
                    '1',
                    '2',
                    '3',
                    '4',
                    '5',
                    '6',
                    '7',
                    '8',
                    '9',
                    '.',
                    '/',
                    '-',
                    ','
                )
            }
            BANK_NAME -> {
                addStringFilter(R.string.allowedCompanyNameChars)
                filterFirstChars(
                    '0',
                    '1',
                    '2',
                    '3',
                    '4',
                    '5',
                    '6',
                    '7',
                    '8',
                    '9',
                    '.',
                    '/',
                    '-',
                    ','
                )
            }
        }
    }

    /**
     * adds input filter that allows ONLY those characters defined in string resource
     *
     * @param allowedCharsRes string resource for allowed characters
     */
    private fun addStringFilter(@StringRes allowedCharsRes: Int) {
        val allowedChars: String = context.getString(allowedCharsRes)
        val allowedCharsFilter =
            InputFilter { source: CharSequence?, start: Int, end: Int, dest: Spanned?, dStart: Int, dEnd: Int ->
                if (source == null) return@InputFilter null
                //checking allowedCharsRes in input and replacing others with empty string
                var containsNotAllowedChars = false
                var i = 0
                while (i < source.length) {
                    if (!allowedChars.contains(source[i].toString())) {
                        containsNotAllowedChars = true
                        break
                    }
                    i++
                }
                if (containsNotAllowedChars) {
                    return@InputFilter ""
                    /* // #IMPORTANT : On Some keyboards; double space turns input to a PERIOD(.) sign. that can be TURNED OFF from settings.
                // SEE HERE : http://teckfront.com/activate-double-space-period-google-key-board-android-lollipop-kitkat-devices/
                // For google keyboard : settings-> language & input -> Google Keyboard -> text Correction -> Double-space period
                // Below is an ugly hack to avoid deleting character on double space when input becomes PERIOD(.) sign.
                return source.equals(".") ?
                        null :
                        "";*/
                }
                null
            }
        inputFilters!!.add(allowedCharsFilter)
    }

    /**
     * disables allowing space in input
     */
    private fun doNotAllowAnySpace() {
        val inputFilter =
            InputFilter { source: CharSequence?, start: Int, end: Int, dest: Spanned?, dStart: Int, dEnd: Int ->
                if (source == null) return@InputFilter ""
                // checking if contains space and replacing it with empty string
                var hasSpace = false
                var i = 0
                while (i < source.length) {
                    if (Character.isSpaceChar(source[i])) {
                        hasSpace = true
                        break
                    }
                    i++
                }
                if (hasSpace) return@InputFilter ""
                null
            }
        inputFilters!!.add(inputFilter)
    }

    /**
     * changes cursor blinking according to focus on view
     */
    private fun changeCourserVisibilityAccordingFocus() {
        //if done key pressed - don't blink cursor
        setOnEditorActionListener(OnEditorActionListener { view: TextView?, actionId: Int, event: KeyEvent? ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                isCursorVisible = false
                return@OnEditorActionListener false
            }
            false
        })

        // if touched on input - blink cursor
        setOnTouchListener(OnTouchListener { view: View?, event: MotionEvent? ->
            isCursorVisible = true
            false
        })

        // if focus on view is changed - change cursor blinking accordingly
        onFocusChangeListener = OnFocusChangeListener { view: View?, hasFocus: Boolean ->
            isCursorVisible = hasFocus
        }
    }

    override fun onKeyPreIme(keyCode: Int, event: KeyEvent): Boolean {
        // if back key press(keyboard down by user) - don't blink cursor
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            isCursorVisible = false
        }
        return super.onKeyPreIme(keyCode, event)
    }

    /**
     * limits charactor occurrence in given input
     *
     * @param charToLimit        charactor to limit for occurrence
     * @param maxFrequencyOfChar maximum frequency of charactor
     */
    private fun filterCharactorOccurrence(charToLimit: Char, maxFrequencyOfChar: Int) {
        occurrenceTextWatcher = getOccurrenceTextWatcher(charToLimit, maxFrequencyOfChar)
        addTextChangedListener(occurrenceTextWatcher)
    }

    private fun getOccurrenceTextWatcher(charToLimit: Char, maxFrequencyOfChar: Int): TextWatcher {
        return object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(
                charSequence: CharSequence,
                start: Int,
                before: Int,
                count: Int
            ) {
                //count charactor frequency in input....if found and is greater than frequency defined - substring input string from its last occurrence
                var charSequence = charSequence
                if (countMatches(charSequence, charToLimit.toString()) > maxFrequencyOfChar) {
                    charSequence = charSequence.toString()
                        .substring(0, charSequence.toString().lastIndexOf(charToLimit))
                    setText(charSequence)
                    setSelection(charSequence.length)
                }
            }

            override fun afterTextChanged(s: Editable) {}
        }
    }

    /**
     * disables given charactor as first charactor in input
     *
     * @param firstChars characters to disable at first
     */
    private fun filterFirstChars(vararg firstChars: Char) {
        val firstCharsList = firstChars.asList()
        filterTextWatcher = getFilterTextWatcher(firstCharsList)
        addTextChangedListener(filterTextWatcher)
    }

    private fun getFilterTextWatcher(firstCharsList: List<Char>): TextWatcher {
        return object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                //if first charactor is to be filtered, substring input string from first index to length of string
                var s = s
                if (s.length > 0 && firstCharsList.contains(s[0])) {
                    s = s.toString().substring(1, s.length)
                    setText(s)
                    setSelection(s.length)
                }
            }

            override fun afterTextChanged(s: Editable) {}
        }
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        //clearing text watchers when detaching from window
        if (filterTextWatcher != null) {
            removeTextChangedListener(filterTextWatcher)
            filterTextWatcher = null
        }
        if (occurrenceTextWatcher != null) {
            removeTextChangedListener(occurrenceTextWatcher)
            occurrenceTextWatcher = null
        }
        inputFilters!!.clear()
    }

    private fun countMatches(str: CharSequence, sub: CharSequence): Int {
        if (TextUtils.isEmpty(str) || TextUtils.isEmpty(sub)) {
            return 0
        }
        var count = 0
        var idx = 0
        while (indexOf(str, sub, idx).also { idx = it } != -1) {
            count++
            idx += sub.length
        }
        return count
    }

    private fun indexOf(cs: CharSequence, searchChar: CharSequence, start: Int): Int {
        return cs.toString().indexOf(searchChar.toString(), start)
    }

    companion object {
        const val EMAIL = 1
        const val PHONE = 2
        const val FULL_NAME = 3
        const val FIRST_LAST_NAME = 4
        const val USER_NAME = 5
        const val ADDRESS = 6
        const val STATE = 7
        const val ZIP_CODE = 8
        const val COMPANY_NAME = 9
        const val PASSWORD = 10
        const val ALPHA_NUMERIC = 11
        const val ONLY_ALPHABETS = 12
        const val ONLY_NUMBERS = 13
        const val NO_SPECIAL_CHARS = 14
        const val ACCOUNT_NAME = 15
        const val BANK_NAME = 16
    }
}