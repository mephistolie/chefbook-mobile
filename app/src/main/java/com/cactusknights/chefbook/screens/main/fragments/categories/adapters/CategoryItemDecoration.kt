package com.cactusknights.chefbook.screens.main.fragments.categories.adapters

import android.graphics.Rect;
import android.view.View;
import androidx.recyclerview.widget.RecyclerView

class CategoryItemDecoration(private val spanCount: Int) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        if (parent.getChildLayoutPosition(view) + 1 % spanCount != 0) {
            outRect.right = 8
        }
        outRect.bottom = 12
    }
}