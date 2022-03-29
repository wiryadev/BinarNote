package com.wiryadev.binarnote.ui

import android.view.View
import android.widget.EditText
import androidx.core.widget.addTextChangedListener
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputLayout

fun View.showSnackbar(message: String) {
    Snackbar.make(this, message, Snackbar.LENGTH_LONG).show()
}

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