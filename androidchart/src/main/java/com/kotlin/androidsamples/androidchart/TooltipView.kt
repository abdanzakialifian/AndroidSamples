package com.kotlin.androidsamples.androidchart

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.view.View

class TooltipView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val backgroundPaint = Paint().apply {
        color = Color.WHITE
        style = Paint.Style.FILL
        isAntiAlias = true
    }

    private val strokePaint = Paint().apply {
        color = Color.GRAY
        style = Paint.Style.STROKE
        strokeWidth = 5f
        isAntiAlias = true
    }

    private val textPaint = Paint().apply {
        color = Color.BLACK
        textSize = 50f
        isAntiAlias = true
    }

    private var tooltipText: String = "Tooltip Text"

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        // Set padding and sizes for the rectangle and triangle
        val rectPadding = 20f
        val triangleHeight = 50f
        val triangleWidth = 60f
        val roundedCornerRadius = 20f

        // Coordinates for the rectangle
        val rectLeft = rectPadding
        val rectRight = width - rectPadding
        val rectBottom = height - rectPadding - triangleHeight  // Leave space for triangle

        // Path to draw the connected rectangle and triangle
        val path = Path().apply {
            // Start from top-left corner of the rectangle
            moveTo(rectLeft, rectPadding)

            // Draw top edge
            lineTo(rectRight, rectPadding)

            // Draw right edge
            lineTo(rectRight, rectBottom)

            // Draw bottom-right corner to the beginning of the triangle
            lineTo((width / 2f) + (triangleWidth / 2), rectBottom)

            // Draw the triangle's bottom rounded part using quadTo
            quadTo(
                width / 2f,
                rectBottom + triangleHeight,
                (width / 2f) - (triangleWidth / 2),
                rectBottom
            )

            // Draw bottom-left corner of the rectangle
            lineTo(rectLeft, rectBottom)

            // Complete the left edge and connect to top-left
            lineTo(rectLeft, rectPadding)
            close()
        }

        // Draw the rectangle and triangle (tooltip background)
        canvas.drawPath(path, backgroundPaint)
        canvas.drawPath(path, strokePaint)

        // Draw the rounded rectangle with shadow
        canvas.drawRoundRect(
            rectLeft,
            rectPadding,
            rectRight,
            rectBottom,
            roundedCornerRadius,
            roundedCornerRadius,
            backgroundPaint
        )

        // Draw the tooltip text in the center of the rectangle
        val textWidth = textPaint.measureText(tooltipText)
        val textX = (width - textWidth) / 2
        val textY = (rectBottom + rectPadding) / 2 - (textPaint.descent() + textPaint.ascent()) / 2
        canvas.drawText(tooltipText, textX, textY, textPaint)
    }

    // Method to set tooltip text
    fun setTooltipText(text: String) {
        tooltipText = text
        invalidate()  // Request to redraw the view with updated text
    }
}




