package com.djw.storiesapp.customView

import android.content.Context
import android.graphics.drawable.Drawable
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import androidx.core.content.ContextCompat
import com.djw.storiesapp.R

class NameEditText: EditTextCustom {

    private lateinit var nameButtonImage: Drawable

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
        nameButtonImage = ContextCompat.getDrawable(context, R.drawable.ic_baseline_person_24) as Drawable
        setOnTouchListener(this)

        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
                // DO NOTHING
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                setButtonDrawables()
            }

            override fun afterTextChanged(s: Editable) {
                // DO NOTHING
            }
        })
    }

    private fun setButtonDrawables(
        startOfTheText: Drawable = nameButtonImage,
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