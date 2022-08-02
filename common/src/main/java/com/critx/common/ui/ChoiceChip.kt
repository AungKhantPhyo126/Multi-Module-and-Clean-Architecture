package com.critx.common.ui

import android.content.Context
import android.view.LayoutInflater
import com.critx.common.databinding.ChoiceChipBinding
import com.google.android.material.chip.Chip

fun Context.createChip(label: String): Chip {
    val inflater: LayoutInflater = LayoutInflater.from(this)
    val chip = ChoiceChipBinding.inflate(inflater).root
    chip.text = label
    return chip
}