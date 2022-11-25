package com.critx.common.ui

import android.content.Context
import android.widget.Toast
import com.google.android.material.textfield.TextInputEditText

fun validatePrice(textBox: TextInputEditText, context: Context): Boolean {
    return if (textBox.text?.length!! < 6) {
        Toast.makeText(
            context,
            textBox.text.toString() + "need to be highter than 1000000",
            Toast.LENGTH_LONG
        ).show()
        false
    } else {
        true
    }
}