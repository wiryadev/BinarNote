package com.wiryadev.binarnote.ui

import android.animation.ObjectAnimator
import android.view.View
import android.widget.EditText
import androidx.core.widget.addTextChangedListener
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputLayout
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

fun View.showSnackbar(message: String) {
    Snackbar.make(this, message, Snackbar.LENGTH_LONG).show()
}

fun View.animateAlphaToVisible(
    animDuration: Long? = null
): ObjectAnimator = ObjectAnimator
    .ofFloat(this, View.ALPHA, 1f)
    .setDuration(animDuration ?: 150L)

inline fun EditText.addErrorListener(
    name: String,
    crossinline validation: (String) -> Boolean = { true },
) {
    val parentInputLayout = this.parent.parent as TextInputLayout

    this.addTextChangedListener(
        afterTextChanged = {
            val additionalValidation = validation.invoke(it.toString())
            parentInputLayout.error = when {
                text.toString().isBlank() -> {
                    "$name must be filled"
                }
                !additionalValidation -> {
                    "$name must be valid"
                }
                else -> {
                    null
                }
            }
        },
    )
}

val simpleDateFormat = SimpleDateFormat("MM/dd/yyyy", Locale.ROOT)

fun String.formatDisplayDate(): String {
    val dateFormatToBeDisplayed = simpleDateFormat.parse(this) as Date
    return DateFormat.getDateInstance(DateFormat.FULL).format(dateFormatToBeDisplayed)
}