package com.kotlin.androidsamples.androidchart

import android.content.Context
import android.graphics.Canvas
import android.view.LayoutInflater
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.MarkerView
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.utils.MPPointD
import com.github.mikephil.charting.utils.MPPointF
import com.kotlin.androidsamples.androidchart.databinding.CustomMarkerViewWithoutPointerBinding
import java.math.BigDecimal
import java.math.RoundingMode

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
        val lineDataSetBuy = getLineDataSet(0)
        val lineDataSetSell = getLineDataSet(1)

        val pointBuy = getPoint(lineDataSetBuy, posX, posY)
        val pointSell = getPoint(lineDataSetSell, posX, posY)

        val entryBuy = getEntry(lineDataSetBuy, pointBuy)
        val entrySell = getEntry(lineDataSetSell, pointSell)

        binding.tooltipView.setCoordinates(posX, chartView.width)
        binding.tooltipView.setEntry(entryBuy, entrySell)
    }

    override fun getOffset(): MPPointF = MPPointF(-(width / 2).toFloat(), -height.toFloat())

    private fun getLineDataSet(index: Int): LineDataSet? {
        return chartView.data.getDataSetByIndex(index) as? LineDataSet
    }

    private fun getPoint(lineDataSet: LineDataSet?, posX: Float, posY: Float): MPPointD? {
        val lineChart = chartView as? LineChart
        val axisDependency = lineDataSet?.axisDependency
        return lineChart?.getTransformer(axisDependency)?.getValuesByTouchPoint(posX, posY)
    }

    private fun getEntry(lineDataSet: LineDataSet?, point: MPPointD?): Entry? {
        return lineDataSet?.values?.find { entry ->
            val entryX = BigDecimal(entry.x.toDouble()).setScale(1, RoundingMode.HALF_UP).toDouble()
            val pointX = BigDecimal(point?.x ?: 0.0).setScale(1, RoundingMode.HALF_UP).toDouble()
            entryX == pointX
        }
    }
}