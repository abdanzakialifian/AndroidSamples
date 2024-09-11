package com.kotlin.androidsamples.androidchart

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.view.LayoutInflater
import androidx.core.content.ContextCompat
import com.github.mikephil.charting.components.MarkerView
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.utils.MPPointF
import com.kotlin.androidsamples.androidchart.databinding.CustomMarkerViewBinding

class CustomMarkerView(context: Context) : MarkerView(context, R.layout.custom_marker_view) {
    private val binding: CustomMarkerViewBinding

    init {
        // Inflate the layout and initialize binding
        val inflater = LayoutInflater.from(context)
        binding = CustomMarkerViewBinding.inflate(inflater, this, true)
    }

    private val outerCirclePaint = Paint().apply {
        color = ContextCompat.getColor(context, R.color.orange)
        style = Paint.Style.STROKE
        strokeWidth = 10f
        isAntiAlias = true
    }

    private val innerCirclePaint = Paint().apply {
        color = Color.WHITE
        style = Paint.Style.FILL
        isAntiAlias = true
    }

    override fun refreshContent(entry: Entry?, highlight: Highlight?) {
        super.refreshContent(entry, highlight)
        // Customize your marker view here
        binding.tvDate.text = entry?.data as? String
    }

    override fun draw(canvas: Canvas, posX: Float, posY: Float) {
        // Draw a circle at the marker position
        canvas.drawCircle(posX, posY, 14f, outerCirclePaint)
        canvas.drawCircle(posX, posY, 14f, innerCirclePaint)
        val offset = getOffsetForDrawingAtPoint(posX, posY)

        // translate to the correct position and draw
        canvas.translate(posX + offset.x, 0F)
        draw(canvas)
    }

    override fun getOffset(): MPPointF {
        // Adjust the marker view's offset
        return MPPointF(-width / 2f, -height.toFloat())
    }
}