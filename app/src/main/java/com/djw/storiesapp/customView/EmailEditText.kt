package com.djw.storiesapp.customView

import android.content.Context
import android.graphics.drawable.Drawable
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.util.AttributeSet
import android.util.Patterns
import androidx.core.content.ContextCompat
import com.djw.storiesapp.R

class EmailEditText: EditTextCustom {

    private lateinit var emailButton: Drawable

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init()
    }

    private fun init() {
        emailButton = ContextCompat.getDrawable(context, R.drawable.ic_baseline_email_24) as Drawable

        setOnTouchListener(this)

        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
                // DO NOTHING
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (!isValidEmail(text.toString())) {
                    error = resources.getString(R.string.invalid_email)
                    setButtonDrawables()
                } else {
                    setButtonDrawables()
                }
            }

            override fun afterTextChanged(s: Editable) {
                // DO NOTHING
            }
        })
    }

    private fun setButtonDrawables(startOfTheText: Drawable = emailButton, topOfTheText: Drawable? = null, endOfTheText: Drawable? = null, bottomOfTheText: Drawable? = null) {
        setCompoundDrawablesWithIntrinsicBounds(startOfTheText, topOfTheText, endOfTheText, bottomOfTheText)
    }

    private fun isValidEmail(email: String): Boolean {
        return !TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

}