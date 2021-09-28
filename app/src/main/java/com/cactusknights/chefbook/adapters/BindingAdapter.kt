package com.cactusknights.chefbook.adapters

import android.graphics.Typeface
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.databinding.BindingAdapter
import com.cactusknights.chefbook.R
import com.google.android.material.textfield.TextInputEditText

@BindingAdapter("isSectionEdit")
fun setIngredientEditStyle(view: TextInputEditText, isSection: Boolean) {
    if (isSection) {
        view.setTypeface(null, Typeface.BOLD)
    } else {
        view.setTypeface(null, Typeface.NORMAL)
    }
}

@BindingAdapter("isSection")
fun setIngredientStyle(view: TextView, isSection: Boolean) {
    if (isSection) {
        view.typeface = ResourcesCompat.getFont(view.context, R.font.comfortaa)
        view.textSize = 26.0F
        view.setTextColor(ContextCompat.getColor(view.context, R.color.navigation_ripple))
    } else {
        view.typeface = ResourcesCompat.getFont(view.context, R.font.montserrat)
        view.textSize = 22.0F
        view.setTextColor(ContextCompat.getColor(view.context, R.color.monochrome_invert))
    }
}