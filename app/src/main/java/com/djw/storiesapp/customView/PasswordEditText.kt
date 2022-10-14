package com.djw.storiesapp.customView

import android.content.Context
import android.graphics.drawable.Drawable
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import androidx.core.content.ContextCompat
import com.djw.storiesapp.R

class PasswordEditText : EditTextCustom {

    private lateinit var passwordButtonImage: Drawable

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
        passwordButtonImage = ContextCompat.getDrawable(context, R.drawable.ic_baseline_lock_24) as Drawable

        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
                // DO NOTHING
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (text?.length in 1..5) {
                    error = resources.getString(R.string.invalid_password, 6)
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

    private fun setButtonDrawables(
        startOfTheText: Drawable = passwordButtonImage,
        topOfTheText: Drawable? = null,
        endOfTheText: Drawable? = null,
        bottomOfTheText: Drawable? = null
    ){
        setCompoundDrawablesWithIntrinsicBounds(
            startOfTheText,
            topOfTheText,
            endOfTheText,
            bottomOfTheText
        )
    }
}