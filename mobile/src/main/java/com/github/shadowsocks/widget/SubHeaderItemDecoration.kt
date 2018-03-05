package com.github.shadowsocks.widget

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.Typeface
import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.util.DisplayMetrics
import android.util.TypedValue
import android.view.View
import com.perqin.shadowsocksq.R

/**
 * Author: perqin
 * Date  : 3/5/18
 *
 * TODO: Support customized SubHeader height
 */
class SubHeaderItemDecoration(val context: Context, private val provider: SubHeaderProvider) : RecyclerView.ItemDecoration() {
    private val textPaint = Paint().apply {
        color = ContextCompat.getColor(context, R.color.material_primary_500)
        textSize = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 14F, context.resources.displayMetrics)
        typeface = Typeface.DEFAULT_BOLD
    }
    private val textHeight = textPaint.fontMetrics.let { it.bottom - it.top }
    private val textBaselineOffset = textPaint.fontMetrics.bottom

    override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State?) {
        c.save()
        var left: Int
        var right: Int
        if (parent.clipToPadding) {
            left = parent.paddingLeft
            right = parent.width -  parent.paddingRight
            c.clipRect(left, parent.paddingTop, right, parent.height - parent.paddingBottom)
        } else {
            left = 0
            right = parent.width
        }
        // Add left padding
        left += dipToDimensionPixelSize(16, context.resources.displayMetrics)
        val height = dipToDimensionPixelSize(48, context.resources.displayMetrics)
        val bound = Rect()
        IntRange(0, parent.childCount - 1).forEach {
            val child = parent.getChildAt(it)
            provider.getSubHeader(parent.getChildAdapterPosition(child))?.let { title ->
                parent.getDecoratedBoundsWithMargins(child, bound)
                val top = bound.top
                val bottom = top + height
                c.drawText(title, left.toFloat(), bottom - (height - textHeight) / 2 - textBaselineOffset, textPaint)
            }
        }
        c.restore()
    }

    override fun getItemOffsets(outRect: Rect, view: View?, parent: RecyclerView, state: RecyclerView.State?) {
        if (provider.getSubHeader(parent.getChildAdapterPosition(view)) != null) {
            outRect.set(0, dipToDimensionPixelSize(48, context.resources.displayMetrics), 0, 0)
        } else {
            outRect.set(0, 0, 0, 0)
        }
    }

    private fun dipToDimensionPixelSize(dip: Int, metrics: DisplayMetrics): Int {
        val f = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dip.toFloat(), metrics)
        return (if (f >= 0) f + 0.5f else f - 0.5f).toInt()
    }

    interface SubHeaderProvider {
        /**
         * Get the sub header for the item at position.
         * @param position The adapter position.
         * @return the sub header title if this item should show a sub header ("" for empty title), <code>null</code> otherwise.
         */
        fun getSubHeader(position: Int): String?
    }
}
