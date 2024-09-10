package com.kotlin.androidsamples.androidchart

import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis.AxisDependency
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.ValueFormatter
import com.kotlin.androidsamples.androidchart.databinding.ActivityAndroidChartBinding
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import kotlin.random.Random

class AndroidChartActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAndroidChartBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAndroidChartBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupChart1()
        setupChart2()
    }

    private fun setupChart1() {
        setDataChart1()

        binding.apply {
            // background color
            chart1.setBackgroundColor(Color.WHITE)

            // no description text
            chart1.description.isEnabled = false

            // enable touch gestures
            chart1.setTouchEnabled(true)

            // enable scaling and dragging
            chart1.setDragEnabled(true)
            chart1.setScaleEnabled(false)
            chart1.setDrawGridBackground(false)

            chart1.animateX(600)

            // get the legend (only possible after setting data)
            chart1.legend.isEnabled = false

            val xAxis = chart1.xAxis
            xAxis.position = XAxis.XAxisPosition.BOTTOM
            xAxis.enableGridDashedLine(10f, 10f, 10f)
            xAxis.setDrawAxisLine(false)
            xAxis.gridColor = ContextCompat.getColor(this@AndroidChartActivity, R.color.light_grey)
            xAxis.textColor = ContextCompat.getColor(this@AndroidChartActivity, R.color.grey)
            xAxis.textSize = 12F

            val labels = ArrayList<Int>()
            labels.add(200)
            labels.add(400)
            labels.add(600)
            labels.add(800)
            labels.add(1000)

            val leftAxis = chart1.axisLeft
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
            leftAxis.gridColor =
                ContextCompat.getColor(this@AndroidChartActivity, R.color.light_grey)
            leftAxis.textColor = ContextCompat.getColor(this@AndroidChartActivity, R.color.grey)
            leftAxis.textSize = 12F

            val rightAxis = chart1.axisRight
            rightAxis.axisMinimum = labels.first().toFloat()
            rightAxis.axisMaximum = labels.last().toFloat()
            rightAxis.isEnabled = false

            val marker = CustomMarkerView(this@AndroidChartActivity)
            marker.chartView = chart1
            chart1.marker = marker
        }
    }

    private fun setupChart2() {
        setDataChart2()
        binding.apply {
            // background color
            chart2.setBackgroundColor(Color.WHITE)

            // no description text
            chart2.description.isEnabled = false

            // enable touch gestures
            chart2.setTouchEnabled(true)

            // enable scaling and dragging
            chart2.setDragEnabled(true)
            chart2.setScaleEnabled(false)
            chart2.setDrawGridBackground(false)

            chart2.animateX(600)

            // get the legend (only possible after setting data)
            chart2.legend.isEnabled = false

            val xAxis = chart2.xAxis
            xAxis.position = XAxis.XAxisPosition.BOTTOM
            xAxis.enableGridDashedLine(10f, 10f, 10f)
            xAxis.setDrawAxisLine(false)
            xAxis.gridColor = ContextCompat.getColor(this@AndroidChartActivity, R.color.light_grey)
            xAxis.textColor = ContextCompat.getColor(this@AndroidChartActivity, R.color.grey)
            xAxis.textSize = 12F

            val labels = ArrayList<Int>()
            labels.add(200)
            labels.add(400)
            labels.add(600)
            labels.add(800)
            labels.add(1000)

            val leftAxis = chart2.axisLeft
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
            leftAxis.gridColor =
                ContextCompat.getColor(this@AndroidChartActivity, R.color.light_grey)
            leftAxis.textColor = ContextCompat.getColor(this@AndroidChartActivity, R.color.grey)
            leftAxis.textSize = 12F

            val rightAxis = chart2.axisRight
            rightAxis.axisMinimum = labels.first().toFloat()
            rightAxis.axisMaximum = labels.last().toFloat()
            rightAxis.isEnabled = false

            val marker = CustomMarkerView(this@AndroidChartActivity)
            marker.chartView = chart2
            chart2.marker = marker

            setDataChart1()
        }
    }

    private fun setDataChart1() {
        val lowerBound = 200
        val upperBound = 1000

        val entryList: ArrayList<Entry> = ArrayList()
        val calendars = ArrayList<Calendar>()

        for (i in 7 downTo 0) {
            val calendar = Calendar.getInstance()
            calendar.add(Calendar.DAY_OF_YEAR, -i)
            calendars.add(calendar)
            val formatter = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
            val date = formatter.format(calendar.time)
            entryList.add(
                Entry(
                    7 - i.toFloat(),
                    Random.nextInt(lowerBound, upperBound).toFloat(),
                    date
                )
            )
        }

        binding.apply {
            chart1.xAxis.axisMinimum = 0F
            chart1.xAxis.axisMaximum = calendars.size.toFloat() - 1
            chart1.xAxis.labelCount = calendars.size - 1
            chart1.xAxis.setDrawLabels(false)
//        chart.xAxis.valueFormatter = object : ValueFormatter() {
//            override fun getFormattedValue(value: Float): String {
//                val index = value.toInt()
//
//                if (index >= 0 && index < calendars.size) {
//                    val futureDate = calendars[index].time
//                    val formatter = SimpleDateFormat("dd/MM", Locale.getDefault())
//                    val date = formatter.format(futureDate)
//                    return date
//                }
//                return ""
//            }
//        }

            val lineDataSet: LineDataSet?

            if (chart1.data != null && chart1.data.dataSetCount > 0) {
                lineDataSet = chart1.data.getDataSetByIndex(0) as? LineDataSet
                lineDataSet?.values = entryList
                chart1.data.notifyDataChanged()
                chart1.notifyDataSetChanged()
            } else {
                // create a dataset and give it a type
                lineDataSet = LineDataSet(entryList, "")
                lineDataSet.axisDependency = AxisDependency.RIGHT
                lineDataSet.color = ContextCompat.getColor(this@AndroidChartActivity, R.color.green)
                lineDataSet.lineWidth = 3f
                lineDataSet.setDrawCircles(false)
                lineDataSet.setDrawValues(false)
                lineDataSet.setDrawFilled(true)
                lineDataSet.mode = LineDataSet.Mode.HORIZONTAL_BEZIER

                val drawable =
                    ContextCompat.getDrawable(this@AndroidChartActivity, R.drawable.fade_green)
                lineDataSet.fillDrawable = drawable

                // create a data object with the data sets
                val data = LineData(lineDataSet)
                data.setValueTextColor(Color.BLACK)
                data.setValueTextSize(9f)

                // set data
                chart1.data = data
            }
        }
    }

    private fun setDataChart2() {
        val lowerBound = 200
        val upperBound = 1000

        val entryList: ArrayList<Entry> = ArrayList()
        val calendars = ArrayList<Calendar>()

        for (i in 7 downTo 0) {
            val calendar = Calendar.getInstance()
            calendar.add(Calendar.DAY_OF_YEAR, -i)
            calendars.add(calendar)
            val formatter = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
            val date = formatter.format(calendar.time)
            entryList.add(
                Entry(
                    7 - i.toFloat(),
                    Random.nextInt(lowerBound, upperBound).toFloat(),
                    date
                )
            )
        }

        binding.apply {
            chart2.xAxis.axisMinimum = 0F
            chart2.xAxis.axisMaximum = calendars.size.toFloat() - 1
            chart2.xAxis.labelCount = calendars.size - 1
            chart2.xAxis.setDrawLabels(false)
//        chart.xAxis.valueFormatter = object : ValueFormatter() {
//            override fun getFormattedValue(value: Float): String {
//                val index = value.toInt()
//
//                if (index >= 0 && index < calendars.size) {
//                    val futureDate = calendars[index].time
//                    val formatter = SimpleDateFormat("dd/MM", Locale.getDefault())
//                    val date = formatter.format(futureDate)
//                    return date
//                }
//                return ""
//            }
//        }

            val lineDataSet: LineDataSet?

            if (chart2.data != null && chart2.data.dataSetCount > 0) {
                lineDataSet = chart2.data.getDataSetByIndex(0) as? LineDataSet
                lineDataSet?.values = entryList
                chart2.data.notifyDataChanged()
                chart2.notifyDataSetChanged()
            } else {
                //set2.setFillFormatter(new MyFillFormatter(900f));
                lineDataSet = LineDataSet(entryList, "")
                lineDataSet.axisDependency = AxisDependency.RIGHT
                lineDataSet.color = ContextCompat.getColor(this@AndroidChartActivity, R.color.red)
                lineDataSet.lineWidth = 3f
                lineDataSet.setDrawCircles(false)
                lineDataSet.setDrawValues(false)
                lineDataSet.setDrawFilled(true)
                lineDataSet.mode = LineDataSet.Mode.HORIZONTAL_BEZIER

                val drawable =
                    ContextCompat.getDrawable(this@AndroidChartActivity, R.drawable.fade_red)
                lineDataSet.fillDrawable = drawable

                // create a data object with the data sets
                val data = LineData(lineDataSet)
                data.setValueTextColor(Color.BLACK)
                data.setValueTextSize(9f)

                // set data
                chart2.data = data
            }
        }
    }
}