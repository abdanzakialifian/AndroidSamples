package com.kotlin.androidsamples.androidchart

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View

class TooltipView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private var highlightX: Float? = null

    private var chartWidth: Int? = null

    private val rectF = RectF()

    private val path = Path()

    fun setCoordinates(highlightX: Float, chartWidth: Int) {
        this.highlightX = highlightX
        this.chartWidth = chartWidth

        post {
            requestLayout()
            invalidate()
        }
    }

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.WHITE
        style = Paint.Style.FILL
        setShadowLayer(5f, 0f, 5f, Color.GRAY)
    }

    private val cornerRadius = 10f.dpToPx()
    private val triangleHeight = 10f.dpToPx()
    private val triangleWidth = 20f.dpToPx()
    private val padding = 4f.dpToPx()

    private fun Float.dpToPx(): Float = this * resources.displayMetrics.density

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        highlightX?.let { xCoordinate ->
            val rectLeft = padding
            val rectTop = padding
            val rectRight = width - padding
            val rectBottom = height - triangleHeight - padding

            rectF.set(rectLeft, rectTop, rectRight, rectBottom)
            canvas.drawRoundRect(rectF, cornerRadius, cornerRadius, paint)

            path.reset()

            val triangleCenterX = when {
                xCoordinate > width -> width - ((chartWidth ?: 0) - xCoordinate)
                xCoordinate > width / 2f -> width / 2f
                else -> xCoordinate
            }

            val triangleStartX = triangleCenterX - (triangleWidth / 2f)

            path.moveTo(triangleStartX, rectBottom)
            path.lineTo(triangleCenterX, rectBottom + triangleHeight)
            path.lineTo(triangleStartX + triangleWidth, rectBottom)
            path.close()

            canvas.drawPath(path, paint)
        }
    }
}







