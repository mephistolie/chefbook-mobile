package com.cactusknights.chefbook.legacy.adapters

import android.graphics.Typeface
import android.widget.TextView
import androidx.compose.ui.res.stringResource
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.databinding.BindingAdapter
import com.cactusknights.chefbook.R
import com.cactusknights.chefbook.models.MarkdownString
import com.cactusknights.chefbook.models.MarkdownTypes
import com.google.android.material.textfield.TextInputEditText

@BindingAdapter("isSectionEdit")
fun setIngredientEditStyle(view: TextInputEditText, type: MarkdownTypes) {
    if (type == MarkdownTypes.HEADER) {
        view.setTypeface(null, Typeface.BOLD)
        view.hint = view.resources.getString(R.string.section)
    } else {
        view.setTypeface(null, Typeface.NORMAL)
        view.hint = view.resources.getString(R.string.ingredient)
    }
}

@BindingAdapter("isSection")
fun setIngredientStyle(view: TextView, markdown: MarkdownString) {
    view.text = markdown.text
    if (markdown.type == MarkdownTypes.HEADER) {
        view.typeface = ResourcesCompat.getFont(view.context, R.font.comfortaa)
        view.textSize = 26.0F
        view.setTextColor(ContextCompat.getColor(view.context, R.color.navigation_ripple))
    } else {
        view.typeface = ResourcesCompat.getFont(view.context, R.font.montserrat)
        view.textSize = 22.0F
        view.setTextColor(ContextCompat.getColor(view.context, R.color.monochrome_invert))
    }
}