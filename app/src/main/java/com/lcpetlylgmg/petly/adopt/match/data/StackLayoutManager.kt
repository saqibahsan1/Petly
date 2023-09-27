package com.lcpetlylgmg.petly.adopt.match.data

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.LayoutParams

class StackLayoutManager(context: Context) : RecyclerView.LayoutManager() {

    override fun generateDefaultLayoutParams(): RecyclerView.LayoutParams {
        return LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
    }

    override fun onLayoutChildren(recycler: RecyclerView.Recycler?, state: RecyclerView.State?) {
        removeAndRecycleAllViews(recycler!!)

        val itemCount = itemCount
        if (itemCount > 0) {
            val stackMargin = 16 // Adjust as needed to control the spacing between stacked items
            val parentWidth = width
            val parentHeight = height

            for (position in 0 until itemCount) {
                val child = recycler.getViewForPosition(position)
                addView(child)

                measureChildWithMargins(child, 0, 0)
                val width = getDecoratedMeasuredWidth(child)
                val height = getDecoratedMeasuredHeight(child)

                val left = (parentWidth - width) / 2
                val top = stackMargin * position // Adjust to control vertical spacing
                val right = left + width
                val bottom = top + height

                layoutDecorated(child, left, top, right, bottom)
            }
        }
    }
}
