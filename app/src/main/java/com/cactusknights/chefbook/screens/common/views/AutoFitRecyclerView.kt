package com.cactusknights.chefbook.screens.common.views

import android.R
import android.content.Context
import android.content.res.Configuration
import android.content.res.TypedArray
import android.util.AttributeSet
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cactusknights.chefbook.screens.main.fragments.categories.adapters.CategoryItemDecoration


class AutoFitRecyclerView : RecyclerView {
    private var manager: GridLayoutManager? = null
    private var columnWidth = -1 //default value

    constructor(context: Context) : super(context) {
        init(context, null)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init(context, attrs)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyle: Int) : super(
        context,
        attrs,
        defStyle
    ) {
        init(context, attrs)
    }

    private fun init(context: Context, attrs: AttributeSet?) {
        if (attrs != null) {
            val attrsArray = intArrayOf(R.attr.columnWidth)
            val array: TypedArray = context.obtainStyledAttributes(attrs, attrsArray)
            columnWidth = array.getDimensionPixelSize(0, -1)
            array.recycle()
        }
        manager = GridLayoutManager(getContext(), 1)
        layoutManager = manager
    }

    override fun onMeasure(widthSpec: Int, heightSpec: Int) {
        super.onMeasure(widthSpec, heightSpec)
        if (columnWidth > 0) {
            val spanCount = 1.coerceAtLeast(measuredWidth / columnWidth)
            manager!!.spanCount = spanCount
            invalidateItemDecorations()
            addItemDecoration(CategoryItemDecoration(spanCount))
        }
    }
}