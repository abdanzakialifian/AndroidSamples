package com.kotlin.androidsamples.androidchart

import android.content.Context
import android.graphics.Canvas
import android.view.LayoutInflater
import com.github.mikephil.charting.components.MarkerView
import com.github.mikephil.charting.utils.MPPointF
import com.kotlin.androidsamples.androidchart.databinding.CustomMarkerViewWithoutPointerBinding

class CustomMarkerViewWithoutPointer(context: Context) :
    MarkerView(context, R.layout.custom_marker_view_without_pointer) {
    // Binds the custom marker view layout using view binding
    private val binding: CustomMarkerViewWithoutPointerBinding

    init {
        val inflater = LayoutInflater.from(context)
        binding = CustomMarkerViewWithoutPointerBinding.inflate(inflater, this, true)
    }

    override fun draw(canvas: Canvas, posX: Float, posY: Float) {
        super.draw(canvas, posX, posY)
        binding.tooltipView.setCoordinates(posX, chartView.width)
    }

    override fun getOffset(): MPPointF {
        return MPPointF(-(binding.tooltipView.width  / 2).toFloat(), -binding.tooltipView.height.toFloat())
    }
}