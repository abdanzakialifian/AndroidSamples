package com.kotlin.androidsamples.androidchart

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.MarkerView
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis.AxisDependency
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.model.GradientColor
import com.github.mikephil.charting.utils.MPPointF
import com.kotlin.androidsamples.androidchart.databinding.ActivityAndroidChartBinding
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import kotlin.random.Random

class AndroidChartActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAndroidChartBinding

    private lateinit var chart: LineChart

    private var values2: ArrayList<Entry> = ArrayList()

    private var values3: ArrayList<Entry> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAndroidChartBinding.inflate(layoutInflater)
        setContentView(binding.root)

        chart = binding.chart1

        // background color
        chart.setBackgroundColor(Color.WHITE)

        // no description text
        chart.description.isEnabled = false

        // enable touch gestures
        chart.setTouchEnabled(true)

        chart.setDragDecelerationFrictionCoef(0.9f)

        // enable scaling and dragging
        chart.setDragEnabled(true)
        chart.setScaleEnabled(true)
        chart.setDrawGridBackground(false)

        // if disabled, scaling can be done on x- and y-axis separately
        chart.setPinchZoom(true)

        chart.animateX(600)

        setData()

        // get the legend (only possible after setting data)
        chart.legend.isEnabled = false

        val xAxis = chart.xAxis
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.enableGridDashedLine(10f, 10f, 10f)
        xAxis.setDrawAxisLine(false)
        xAxis.gridColor = ContextCompat.getColor(this, R.color.light_grey)
        xAxis.textColor = ContextCompat.getColor(this, R.color.grey)
        xAxis.textSize = 12F

        val labels = ArrayList<Int>()
        labels.add(200)
        labels.add(400)
        labels.add(600)
        labels.add(800)
        labels.add(1000)

        val leftAxis = chart.axisLeft
        leftAxis.enableGridDashedLine(10F, 10F, 10F)
        leftAxis.setDrawAxisLine(false)
        leftAxis.axisMinimum = 0F
        leftAxis.axisMaximum = labels.size.toFloat() - 1F
        leftAxis.labelCount = labels.size - 1
        leftAxis.valueFormatter = object : ValueFormatter() {
            override fun getFormattedValue(value: Float): String {
                val index = value.toInt()
                if (index >= 0 && index < labels.size) {
                    return buildString {
                        append(labels[index])
                        append("k")
                    }
                }
                return ""
            }
        }
        chart
        leftAxis.gridColor = ContextCompat.getColor(this, R.color.light_grey)
        leftAxis.textColor = ContextCompat.getColor(this, R.color.grey)
        leftAxis.textSize = 12F

        val rightAxis = chart.axisRight
        rightAxis.axisMinimum = labels.first().toFloat()
        rightAxis.axisMaximum = labels.last().toFloat()
        rightAxis.isEnabled = false

        val sets = chart.data.dataSets
        for (iSet in sets) {
            val set = iSet as LineDataSet
            set.mode = LineDataSet.Mode.HORIZONTAL_BEZIER
        }

        val marker = CustomMarkerView(this)
        chart.marker = marker
    }

    private fun setData() {
        val lowerBound = 200
        val upperBound = 1000

        val calendars = ArrayList<Calendar>()

        for (i in 0 until 7) {
            val calendar = Calendar.getInstance()
            calendar.add(Calendar.DAY_OF_MONTH, i)
            calendars.add(calendar)
            values2.add(Entry(i.toFloat(), Random.nextInt(lowerBound, upperBound).toFloat()))
            values3.add(Entry(i.toFloat(), Random.nextInt(lowerBound, upperBound).toFloat()))
        }

        chart.xAxis.axisMinimum = 0F
        chart.xAxis.axisMaximum = calendars.size.toFloat() - 1
        chart.xAxis.labelCount = calendars.size - 1
        chart.xAxis.valueFormatter = object : ValueFormatter() {
            override fun getFormattedValue(value: Float): String {
                val index = value.toInt()

                if (index >= 0 && index < calendars.size) {
                    val futureDate = calendars[index].time
                    val formatter = SimpleDateFormat("dd/MM", Locale.getDefault())
                    val date = formatter.format(futureDate)
                    return date
                }
                return ""
            }
        }

        val set2: LineDataSet?
        val set3: LineDataSet?

        if (chart.data != null && chart.data.dataSetCount > 0) {
            set2 = chart.data.getDataSetByIndex(0) as? LineDataSet
            set3 = chart.data.getDataSetByIndex(1) as? LineDataSet
            set2?.values = values2
            set3?.values = values3
            chart.data.notifyDataChanged()
            chart.notifyDataSetChanged()
        } else {
            // create a dataset and give it a type
            set2 = LineDataSet(values2, "")
            set2.axisDependency = AxisDependency.RIGHT
            set2.color = ContextCompat.getColor(this, R.color.green)
            set2.lineWidth = 3f
            set2.setDrawCircles(false)
            set2.setDrawValues(false)
            set2.setDrawFilled(true)
            set2.fillColor = ContextCompat.getColor(this, R.color.green)
            set2.fillAlpha = 5

            //set2.setFillFormatter(new MyFillFormatter(900f));
            set3 = LineDataSet(values3, "")
            set3.axisDependency = AxisDependency.RIGHT
            set3.color = ContextCompat.getColor(this, R.color.red)
            set3.lineWidth = 3f
            set3.setDrawCircles(false)
            set3.setDrawValues(false)
            set3.setDrawFilled(true)
            set3.fillColor = ContextCompat.getColor(this, R.color.red)
            set3.fillAlpha = 5

            // create a data object with the data sets
            val data = LineData(set2, set3)
            data.setValueTextColor(Color.BLACK)
            data.setValueTextSize(9f)

            // set data
            chart.data = data
        }
    }

    private fun setupPointerChart(lineDataSet: LineDataSet) {
        lineDataSet.setCircleColor(ContextCompat.getColor(this, R.color.orange))
        lineDataSet.circleRadius = 6f
        lineDataSet.circleHoleRadius = 4f
    }
}

class CustomMarkerView(context: Context) : MarkerView(context, R.layout.custom_marker_view) {

    private val outerCirclePaint = Paint().apply {
        color = ContextCompat.getColor(context, R.color.orange)
        style = Paint.Style.STROKE
        strokeWidth = 20f
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
//        textView.text = "Value: ${entry?.y}"
    }

    override fun draw(canvas: Canvas, posX: Float, posY: Float) {
        super.draw(canvas, posX, posY)
        // Draw a circle at the marker position
        canvas.drawCircle(posX, posY, 20f, outerCirclePaint)
        canvas.drawCircle(posX, posY, 20f, innerCirclePaint)
    }

    override fun getOffset(): MPPointF {
        // Adjust the marker view's offset
        return MPPointF(-width / 2f, -height.toFloat())
    }
}