package com.kotlin.androidsamples.androidchart

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.view.LayoutInflater
import androidx.core.content.ContextCompat
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.MarkerView
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.utils.MPPointD
import com.github.mikephil.charting.utils.MPPointF
import com.kotlin.androidsamples.androidchart.databinding.CustomMarkerViewWithPointerBinding

/**
 * CustomMarkerViewWithPointer is a subclass of [MarkerView] used to display a custom tooltip with two circles
 * (an outer stroke and inner fill) at specific points on a line chart.
 * This marker view binds data to a custom layout and draws circles at the location of the selected entries
 * for two line data sets.
 *
 * @constructor
 * @param context the context in which the view is running.
 */
class CustomMarkerViewWithPointer(context: Context) : MarkerView(context, R.layout.custom_marker_view_with_pointer) {
    // Binds the custom marker view layout using view binding
    private val binding: CustomMarkerViewWithPointerBinding

    init {
        val inflater = LayoutInflater.from(context)
        binding = CustomMarkerViewWithPointerBinding.inflate(inflater, this, true)
    }

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

    /**
     * Updates the content of the marker view with data from the entry.
     *
     * @param entry the chart entry to display in the marker.
     * @param highlight the highlight object that contains the information of the highlighted value.
     */
    override fun refreshContent(entry: Entry?, highlight: Highlight?) {
        super.refreshContent(entry, highlight)
        binding.tvDate.text = entry?.data as? String
    }

    /**
     * Draws the marker view at a specific position on the canvas.
     * It retrieves the two line data sets and draws a circle at the data points
     * corresponding to the touched position.
     *
     * @param canvas the canvas to draw the marker on.
     * @param posX the x position where the marker will be drawn.
     * @param posY the y position where the marker will be drawn.
     */
    override fun draw(canvas: Canvas, posX: Float, posY: Float) {
        val lineDataSet1 = getLineDataSet(0)
        val lineDataSet2 = getLineDataSet(1)

        val point1 = getPoint(lineDataSet1, posX, posY)
        val point2 = getPoint(lineDataSet2, posX, posY)

        val entry1 = getEntry(lineDataSet1, point1)
        val entry2 = getEntry(lineDataSet2, point2)

        // Draw circles at the respective entry points for both datasets
        drawCircle(entry1, lineDataSet1, canvas)
        drawCircle(entry2, lineDataSet2, canvas)

        // Offset for marker drawing at the correct location
        val offset = getOffsetForDrawingAtPoint(posX, posY)

        // Translate canvas to the correct position and draw
        canvas.translate(posX + offset.x, 0F)
        draw(canvas)
    }

    /**
     * Returns the offset position for the marker view, ensuring it is centered on the x-axis
     * and positioned above the selected entry.
     *
     * @return the calculated offset as an [MPPointF].
     */
    override fun getOffset(): MPPointF {
        // Adjust the marker view's offset
        return MPPointF(-width / 2f, -height.toFloat())
    }

    /**
     * Retrieves the line data set at the specified index.
     *
     * @param index the index of the data set in the chart data.
     * @return the line data set at the specified index, or null if it doesn't exist.
     */
    private fun getLineDataSet(index: Int): LineDataSet? {
        return chartView.data.getDataSetByIndex(index) as? LineDataSet
    }

    /**
     * Converts the touch position (posX, posY) on the chart to the corresponding data point.
     *
     * @param lineDataSet the line data set to get the data from.
     * @param posX the x position of the touch event.
     * @param posY the y position of the touch event.
     * @return the calculated data point as an [MPPointD].
     */
    private fun getPoint(lineDataSet: LineDataSet?, posX: Float, posY: Float): MPPointD? {
        val lineChart = chartView as? LineChart
        val axisDependency = lineDataSet?.axisDependency
        return lineChart?.getTransformer(axisDependency)?.getValuesByTouchPoint(posX, posY)
    }

    /**
     * Retrieves the chart entry that corresponds to the calculated data point.
     *
     * @param lineDataSet the line data set to search within.
     * @param point the calculated data point (x, y values).
     * @return the corresponding chart entry, or null if none is found.
     */
    private fun getEntry(lineDataSet: LineDataSet?, point: MPPointD?): Entry? {
        return lineDataSet?.values?.find { entry -> entry.x == point?.x?.toFloat() }
    }

    /**
     * Draws a circle at the position of the specified entry.
     *
     * @param entry the chart entry at which to draw the circle.
     * @param lineDataSet the line data set containing the entry.
     * @param canvas the canvas to draw the circle on.
     */
    private fun drawCircle(entry: Entry?, lineDataSet: LineDataSet?, canvas: Canvas) {
        if (entry != null) {
            val lineChart = chartView as? LineChart
            val axisDependency = lineDataSet?.axisDependency
            val point =
                lineChart?.getTransformer(axisDependency)?.getPixelForValues(entry.x, entry.y)
            // Draw a circle at the marker position
            // Draw outer circle (stroke)
            canvas.drawCircle(
                point?.x?.toFloat() ?: 0F,
                point?.y?.toFloat() ?: 0F,
                14f,
                outerCirclePaint,
            )
            // Draw a circle at the marker position
            // Draw inner circle (fill)
            canvas.drawCircle(
                point?.x?.toFloat() ?: 0F,
                point?.y?.toFloat() ?: 0F,
                14f,
                innerCirclePaint,
            )
        }
    }
}