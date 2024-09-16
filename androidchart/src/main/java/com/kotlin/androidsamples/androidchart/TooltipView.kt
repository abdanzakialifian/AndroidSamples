package com.kotlin.androidsamples.androidchart

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.graphics.RectF
import android.util.AttributeSet
import android.util.Log
import android.view.View

class TooltipView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private var xCoordinate: Float? = null // Renamed to xCoordinate for clarity

    // Function to set x-coordinate and refresh the view
    fun setCoordinates(x: Float) {
        this.xCoordinate = x
        post {
            invalidate()
        }
    }

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.WHITE
        style = Paint.Style.FILL // No stroke, only fill
        setShadowLayer(5f, 0f, 5f, Color.GRAY) // Adding shadow
    }

    private val paint2= Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.WHITE
        style = Paint.Style.FILL // No stroke, only fill
        setShadowLayer(5f, 0f, 5f, Color.GRAY) // Adding shadow
    }

    private val cornerRadius = 10f.dpToPx() // Radius for rounded corners
    private val triangleHeight = 10f.dpToPx() // Height of the triangle
    private val triangleWidth = 20f.dpToPx() // Width of the triangle
    private val padding = 4f.dpToPx() // Padding for inner content

    // Method to convert dp to px
    private fun Float.dpToPx(): Float = this * resources.displayMetrics.density

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        if (xCoordinate != null) {
            // Define rectangle area above the triangle
            val rectLeft = padding
            val rectTop = padding
            val rectRight = width - padding
            val rectBottom = height - triangleHeight - padding

            // Draw the rounded rectangle
            val rectF = RectF(rectLeft, rectTop, rectRight, rectBottom)
            canvas.drawRoundRect(rectF, cornerRadius, cornerRadius, paint)

            // Draw the triangle
            val path = Path()

            val triangleCenterX = when {
                xCoordinate!! < triangleWidth / 2f -> triangleWidth / 2f
                xCoordinate!! > width -> (xCoordinate!! - width)
                xCoordinate!! > width / 2f -> width / 2f
                else -> xCoordinate
            }
            Log.d("CEK", "X COORDINATE : $xCoordinate")
            Log.d("CEK", "WIDTH : $width")
            Log.d("CEK", "WIDTH 1 : ${width / 2f}")
            Log.d("CEK", "WIDTH 2 : ${triangleWidth / 2f}")
            Log.d("CEK", "WIDTH 3 : ${width - (triangleWidth / 2f)}")
            Log.d("CEK", "TRIANGLE CENTER X : $triangleCenterX")
            Log.d("CEK", "TRIANGLE WIDTH : $triangleWidth")

            val triangleStartX = triangleCenterX!! - (triangleWidth / 2f)

            // Drawing the triangle under the rounded rectangle
            path.moveTo(triangleStartX, rectBottom) // Left side of triangle
            path.lineTo(triangleCenterX, rectBottom + triangleHeight) // Bottom point of the triangle
            path.lineTo(triangleStartX + triangleWidth, rectBottom) // Right side of triangle
            path.close()

            canvas.drawPath(path, paint2)
        }
    }
}







