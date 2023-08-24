package com.ziss.storyapp.presentation.components

import android.content.Context
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.annotation.DrawableRes
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import com.ziss.storyapp.R

open class PasswordEditText : AppCompatEditText, View.OnTouchListener {
    private lateinit var visibilityButtonImage: Drawable
    private var isPasswordVisible = false

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context, attrs, defStyleAttr
    ) {
        init()
    }

    private fun init() {
        inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
        visibilityButtonImage =
            ContextCompat.getDrawable(context, R.drawable.ic_visibility_20) as Drawable

        setOnTouchListener(this)

        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s.toString().isNotEmpty()) {
                    showVisibilityButton()

                    if (count < 8) {
                        error
                    }
                } else {
                    hideVisibilityButton()
                }
            }

            override fun afterTextChanged(s: Editable?) {}
        })
    }


    override fun onTouch(v: View?, event: MotionEvent): Boolean {
        if (compoundDrawables[2] != null) {
            val clearButtonStart: Float
            val clearButtonEnd: Float

            if (event.action == MotionEvent.ACTION_UP) {
                if (layoutDirection == View.LAYOUT_DIRECTION_RTL) {
                    clearButtonEnd = (visibilityButtonImage.intrinsicWidth + paddingStart).toFloat()
                    when {
                        event.x < clearButtonEnd -> {
                            togglePasswordVisibility()
                            return true
                        }
                    }
                } else {
                    clearButtonStart =
                        (width - paddingEnd - visibilityButtonImage.intrinsicWidth).toFloat()
                    when {
                        event.x > clearButtonStart -> {
                            togglePasswordVisibility()
                            return true
                        }
                    }
                }
            }
            return false
        }
        return false
    }

    private fun setButtonDrawables(
        startOfTheText: Drawable? = compoundDrawables[0],
        topOfTheText: Drawable? = null,
        endOfTheText: Drawable? = null,
        bottomOfTheText: Drawable? = null,
    ) {
        setCompoundDrawablesWithIntrinsicBounds(
            startOfTheText, topOfTheText, endOfTheText, bottomOfTheText
        )
    }

    private fun showVisibilityButton() {
        setButtonDrawables(endOfTheText = visibilityButtonImage)
    }

    private fun hideVisibilityButton() {
        setButtonDrawables()
    }

    private fun setEdTypeFace(){
        val typeFace = Typeface.createFromAsset(context.assets, "poppins.ttf")
        setTypeface(typeFace)
    }

    private fun togglePasswordVisibility() {
        if (isPasswordVisible) {
            inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
            isPasswordVisible = false
            updateToggleIcon(R.drawable.ic_visibility_20)
        } else {
            inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
            isPasswordVisible = true
            updateToggleIcon(R.drawable.ic_visibility_off_20)
        }
        setSelection(text!!.length)
    }

    private fun updateToggleIcon(@DrawableRes drawableRes: Int) {
        visibilityButtonImage = ContextCompat.getDrawable(context, drawableRes) as Drawable
        visibilityButtonImage = DrawableCompat.wrap(visibilityButtonImage)
        DrawableCompat.setTint(visibilityButtonImage, currentHintTextColor)
        setButtonDrawables(endOfTheText = visibilityButtonImage)
    }
}