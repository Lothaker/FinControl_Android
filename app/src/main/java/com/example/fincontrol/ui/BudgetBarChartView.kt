package com.example.fincontrol.ui

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import androidx.core.content.ContextCompat
import com.example.fincontrol.R
import com.example.fincontrol.model.CategoryExpenseSummary
import com.example.fincontrol.util.CurrencyUtils
import kotlin.math.max

class BudgetBarChartView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : View(context, attrs) {

    private val labelPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.parseColor("#2F3650")
        textSize = 36f
    }
    private val valuePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.parseColor("#4A4A4A")
        textSize = 28f
    }
    private val barPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.parseColor("#5A7FC4")
    }
    private val barBgPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.parseColor("#D9E1F2")
    }

    private var items: List<CategoryExpenseSummary> = emptyList()

    fun submitData(data: List<CategoryExpenseSummary>) {
        items = data.take(5)
        invalidate()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val desiredHeight = paddingTop + paddingBottom + max(items.size, 1) * 140
        val resolvedWidth = MeasureSpec.getSize(widthMeasureSpec)
        val resolvedHeight = resolveSize(desiredHeight, heightMeasureSpec)
        setMeasuredDimension(resolvedWidth, resolvedHeight)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        if (items.isEmpty()) {
            labelPaint.textSize = 34f
            canvas.drawText("Sem despesas neste mês", paddingLeft.toFloat(), (height / 2).toFloat(), labelPaint)
            return
        }

        val maxValue = items.maxOf { max(it.totalCents, 1L) }.toFloat()
        var top = paddingTop.toFloat() + 10f
        val left = paddingLeft.toFloat()
        val barLeft = left + 10f
        val barRight = width - paddingRight.toFloat() - 10f

        items.forEach { item ->
            canvas.drawText(item.categoryName, left, top + 30f, labelPaint)
            val valueText = CurrencyUtils.formatFromCents(item.totalCents)
            canvas.drawText(valueText, left, top + 68f, valuePaint)

            val barTop = top + 84f
            val barBottom = barTop + 28f
            canvas.drawRoundRect(barLeft, barTop, barRight, barBottom, 16f, 16f, barBgPaint)
            val progressRight = barLeft + ((item.totalCents / maxValue) * (barRight - barLeft))
            canvas.drawRoundRect(barLeft, barTop, progressRight, barBottom, 16f, 16f, barPaint)
            top += 130f
        }
    }
}
