package com.kotlin.androidsamples.androidchart

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.graphics.RectF
import android.graphics.Typeface
import android.util.AttributeSet
import android.view.View
import androidx.core.content.ContextCompat
import com.github.mikephil.charting.data.Entry

class TooltipView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private var highlightX: Float? = null

    private var chartWidth: Int? = null

    private var entryBuy: Entry? = null

    private var entrySell: Entry? = null

    private val rectF = RectF()

    private val path = Path()

    private val buyText = "Harga beli/gram"

    private val sellText = "Harga jual/gram"

    fun setCoordinates(highlightX: Float, chartWidth: Int) {
        this.highlightX = highlightX
        this.chartWidth = chartWidth

        post {
            requestLayout()
            invalidate()
        }
    }

    fun setEntry(entryBuy: Entry?, entrySell: Entry?) {
        this.entryBuy = entryBuy
        this.entrySell = entrySell
    }

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.WHITE
        style = Paint.Style.FILL
        setShadowLayer(5f, 0f, 5f, Color.GRAY)
    }

    private val textPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.BLACK
        textSize = 9f.dpToPx
        textAlign = Paint.Align.LEFT
    }

    private val valuePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        textSize = 9f.dpToPx
        textAlign = Paint.Align.RIGHT
        typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
    }

    private val datePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.GRAY
        textSize = 6f.dpToPx
        textAlign = Paint.Align.CENTER
    }

    private val buyPaint =
        Paint(valuePaint).apply { color = ContextCompat.getColor(context, R.color.green) }

    private val sellPaint =
        Paint(valuePaint).apply { color = ContextCompat.getColor(context, R.color.red) }

    private val cornerRadius = 10f.dpToPx
    private val triangleHeight = 10f.dpToPx
    private val triangleWidth = 20f.dpToPx
    private val padding = 4f.dpToPx

    private val Float.dpToPx: Float
        get() = this * resources.displayMetrics.density

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        highlightX?.let { xCoordinate ->
            val buyPriceTextWidth = textPaint.measureText(buyText)
            val buyPriceValueWidth = buyPaint.measureText(entryBuy?.data.toString())
            val sellPriceTextWidth = textPaint.measureText(sellText)
            val sellPriceValueWidth = sellPaint.measureText(entrySell?.data.toString())

            val maxTextWidth = maxOf(
                buyPriceTextWidth + buyPriceValueWidth,
                sellPriceTextWidth + sellPriceValueWidth
            )

            val rectWidth = maxTextWidth + 2 * 20f.dpToPx

            val rectLeft = (width - rectWidth) / 2f
            val rectTop = padding
            val rectRight = rectLeft + rectWidth
            val rectBottom = height - triangleHeight - padding

            rectF.set(rectLeft, rectTop, rectRight, rectBottom)
            canvas.drawRoundRect(rectF, cornerRadius, cornerRadius, paint)

            path.reset()

            val triangleCenterX = when {
                xCoordinate / 2 > width -> width - ((chartWidth ?: 0) - xCoordinate)
                xCoordinate > width / 2f -> width / 2f
                else -> xCoordinate
            }

            val triangleStartX = triangleCenterX - (triangleWidth / 2f)

            path.moveTo(triangleStartX, rectBottom)
            path.lineTo(triangleCenterX, rectBottom + triangleHeight)
            path.lineTo(triangleStartX + triangleWidth, rectBottom)
            path.close()

            canvas.drawPath(path, paint)

            entryBuy?.data?.let { date ->
                canvas.drawText(
                    date.toString(),
                    width / 2f,
                    rectTop + 12f.dpToPx,
                    datePaint
                )
            }

            entryBuy?.y?.let { buyPrice ->
                canvas.drawText(
                    buyText,
                    rectLeft + 8f.dpToPx,
                    rectTop + 8f.dpToPx + 18f.dpToPx,
                    textPaint
                )

                canvas.drawText(
                    buildString {
                        append("IDR")
                        append(" ")
                        append(buyPrice.toInt())
                        append(",000.00")
                    },
                    rectRight - 8f.dpToPx,
                    rectTop + 8f.dpToPx + 18f.dpToPx,
                    buyPaint
                )
            }

            entrySell?.y?.let { sellPrice ->
                canvas.drawText(
                    sellText,
                    rectLeft + 8f.dpToPx,
                    rectTop + 8f.dpToPx + 2 * 16f.dpToPx,
                    textPaint
                )

                canvas.drawText(
                    buildString {
                        append("IDR")
                        append(" ")
                        append(sellPrice.toInt())
                        append(",000.00")
                    },
                    rectRight - 8f.dpToPx,
                    rectTop + 8f.dpToPx + 2 * 16f.dpToPx,
                    sellPaint
                )
            }
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val buyPriceTextWidth = textPaint.measureText(buyText)
        val buyPriceValueWidth = buyPaint.measureText(entryBuy?.data.toString())
        val sellPriceTextWidth = textPaint.measureText(sellText)
        val sellPriceValueWidth = sellPaint.measureText(entrySell?.data.toString())

        val maxTextWidth = maxOf(
            buyPriceTextWidth + buyPriceValueWidth,
            sellPriceTextWidth + sellPriceValueWidth
        )

        val desiredWidth = maxTextWidth + 4 * 20f.dpToPx

        val desiredHeight = 2 * 18f.dpToPx + 2 * 12f.dpToPx + triangleHeight

        val width = resolveSize(desiredWidth.toInt(), widthMeasureSpec)
        val height = resolveSize(desiredHeight.toInt(), heightMeasureSpec)

        setMeasuredDimension(width, height)
    }
}







