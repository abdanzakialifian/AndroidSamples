package com.kotlin.androidsamples.androidchart

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.Log
import android.view.LayoutInflater
import androidx.core.content.ContextCompat
import com.github.mikephil.charting.components.MarkerView
import com.github.mikephil.charting.utils.MPPointF
import com.kotlin.androidsamples.androidchart.databinding.CustomMarkerViewWithoutPointerBinding

class CustomMarkerViewWithoutPointer(context: Context) :
    MarkerView(context, R.layout.custom_marker_view_without_pointer) {
    // Binds the custom marker view layout using view binding
    private val binding: CustomMarkerViewWithoutPointerBinding

    // Paint object for the outer circle (stroke)
    private val outerCirclePaint = Paint().apply {
        color = ContextCompat.getColor(context, R.color.orange)
        style = Paint.Style.STROKE
        strokeWidth = 10f
        isAntiAlias = true
    }

    // Paint object for the inner circle (fill)
    private val innerCirclePaint = Paint().apply {
        color = Color.WHITE
        style = Paint.Style.FILL
        isAntiAlias = true
    }

    init {
        val inflater = LayoutInflater.from(context)
        binding = CustomMarkerViewWithoutPointerBinding.inflate(inflater, this, true)
    }

    override fun draw(canvas: Canvas, posX: Float, posY: Float) {
        super.draw(canvas, posX, posY)
        // Draw a circle at the marker position
        // Draw outer circle (stroke)
        canvas.drawCircle(
            posX,
            posY,
            14f,
            outerCirclePaint,
        )
        // Draw a circle at the marker position
        // Draw inner circle (fill)
        canvas.drawCircle(
            posX,
            posY,
            14f,
            innerCirclePaint,
        )

        binding.tooltipView.setCoordinates(posX)
    }

    /**
     * Returns the offset position for the marker view, ensuring it is centered on the x-axis
     * and positioned above the selected entry.
     *
     * @return the calculated offset as an [MPPointF].
     */
    override fun getOffset(): MPPointF {
        return MPPointF(-(binding.tooltipView.width  / 2).toFloat(), -binding.tooltipView.height.toFloat())
    }
}