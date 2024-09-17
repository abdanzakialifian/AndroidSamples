package com.kotlin.androidsamples.androidchart

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis.AxisDependency
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.utils.MPPointD
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
        setupChart3()
        setupChart4()
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

            val marker = CustomMarkerViewWithPointer(this@AndroidChartActivity)
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

            val marker = CustomMarkerViewWithPointer(this@AndroidChartActivity)
            marker.chartView = chart2
            chart2.marker = marker
        }
    }

    private fun setupChart3() {
        setDataChart3()

        binding.apply {
            // background color
            chart3.setBackgroundColor(Color.WHITE)

            // no description text
            chart3.description.isEnabled = false

            // enable touch gestures
            chart3.setTouchEnabled(true)

            // enable scaling and dragging
            chart3.setDragEnabled(true)
            chart3.setScaleEnabled(false)
            chart3.setDrawGridBackground(false)

            chart3.animateX(600)

            // get the legend (only possible after setting data)
            chart3.legend.isEnabled = false

            val xAxis = chart3.xAxis
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

            val leftAxis = chart3.axisLeft
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

            val rightAxis = chart3.axisRight
            rightAxis.axisMinimum = labels.first().toFloat()
            rightAxis.axisMaximum = labels.last().toFloat()
            rightAxis.isEnabled = false

            val marker = CustomMarkerViewWithPointer(this@AndroidChartActivity)
            marker.chartView = chart3
            chart3.marker = marker
        }
    }

    private fun setupChart4() {
        setDataChart4()

        binding.apply {
            // background color
            chart4.setBackgroundColor(Color.WHITE)

            // no description text
            chart4.description.isEnabled = false

            // enable touch gestures
            chart4.setTouchEnabled(true)

            // enable scaling and dragging
            chart4.setDragEnabled(true)
            chart4.setScaleEnabled(false)
            chart4.setDrawGridBackground(false)

            chart4.animateX(600)

            // get the legend (only possible after setting data)
            chart4.legend.isEnabled = false

            chart4.extraTopOffset = 70F
            chart4.extraRightOffset = 30f

            val xAxis = chart4.xAxis
            xAxis.position = XAxis.XAxisPosition.BOTTOM
            xAxis.enableGridDashedLine(10f, 10f, 10f)
            xAxis.setDrawAxisLine(false)
            xAxis.gridColor = ContextCompat.getColor(this@AndroidChartActivity, R.color.grey)
            xAxis.textColor = ContextCompat.getColor(this@AndroidChartActivity, R.color.grey)
            xAxis.textSize = 12F

            val labels = ArrayList<Int>()
            labels.add(200)
            labels.add(400)
            labels.add(600)
            labels.add(800)
            labels.add(1000)

            val leftAxis = chart4.axisLeft
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
                ContextCompat.getColor(this@AndroidChartActivity, R.color.grey)
            leftAxis.textColor = ContextCompat.getColor(this@AndroidChartActivity, R.color.grey)
            leftAxis.textSize = 12F

            val rightAxis = chart4.axisRight
            rightAxis.axisMinimum = labels.first().toFloat()
            rightAxis.axisMaximum = labels.last().toFloat()
            rightAxis.isEnabled = false

            val marker = CustomMarkerViewWithoutPointer(this@AndroidChartActivity)
            marker.chartView = chart4
            chart4.marker = marker
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

    private fun setDataChart3() {
        val lowerBound = 200
        val upperBound = 1000

        val entryList: ArrayList<Entry> = ArrayList()
        val entryList2: ArrayList<Entry> = ArrayList()
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
            entryList2.add(
                Entry(
                    7 - i.toFloat(),
                    Random.nextInt(lowerBound, upperBound).toFloat(),
                    date
                )
            )
        }

        binding.apply {
            chart3.xAxis.axisMinimum = 0F
            chart3.xAxis.axisMaximum = calendars.size.toFloat() - 1
            chart3.xAxis.labelCount = calendars.size - 1
            chart3.xAxis.setDrawLabels(false)
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
            val lineDataSet2: LineDataSet?

            if (chart3.data != null && chart3.data.dataSetCount > 0) {
                lineDataSet = chart3.data.getDataSetByIndex(0) as? LineDataSet
                lineDataSet2 = chart3.data.getDataSetByIndex(1) as? LineDataSet
                lineDataSet?.values = entryList
                lineDataSet2?.values = entryList2
                chart3.data.notifyDataChanged()
                chart3.notifyDataSetChanged()
            } else {
                lineDataSet = LineDataSet(entryList, "")
                lineDataSet.axisDependency = AxisDependency.RIGHT
                lineDataSet.color = ContextCompat.getColor(this@AndroidChartActivity, R.color.green)
                lineDataSet.lineWidth = 3f
                lineDataSet.setDrawCircles(false)
                lineDataSet.setDrawValues(false)
                lineDataSet.setDrawFilled(false)
                lineDataSet.mode = LineDataSet.Mode.HORIZONTAL_BEZIER

                //set2.setFillFormatter(new MyFillFormatter(900f));
                lineDataSet2 = LineDataSet(entryList2, "")
                lineDataSet2.axisDependency = AxisDependency.RIGHT
                lineDataSet2.color = ContextCompat.getColor(this@AndroidChartActivity, R.color.red)
                lineDataSet2.lineWidth = 3f
                lineDataSet2.setDrawCircles(false)
                lineDataSet2.setDrawValues(false)
                lineDataSet2.setDrawFilled(false)
                lineDataSet2.mode = LineDataSet.Mode.HORIZONTAL_BEZIER

                // create a data object with the data sets
                val data = LineData(lineDataSet, lineDataSet2)
                data.setValueTextColor(Color.BLACK)
                data.setValueTextSize(9f)

                // set data
                chart3.data = data
            }
        }
    }

    private fun setDataChart4() {
        val lowerBound = 200
        val upperBound = 1000

        val entryList: ArrayList<Entry> = ArrayList()
        val entryList2: ArrayList<Entry> = ArrayList()
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
            entryList2.add(
                Entry(
                    7 - i.toFloat(),
                    Random.nextInt(lowerBound, upperBound).toFloat(),
                    date
                )
            )
        }

        binding.apply {
            chart4.xAxis.axisMinimum = 0F
            chart4.xAxis.axisMaximum = calendars.size.toFloat() - 1
            chart4.xAxis.labelCount = calendars.size - 1
            chart4.xAxis.setDrawLabels(false)
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
            val lineDataSet2: LineDataSet?

            if (chart4.data != null && chart4.data.dataSetCount > 0) {
                lineDataSet = chart4.data.getDataSetByIndex(0) as? LineDataSet
                lineDataSet2 = chart4.data.getDataSetByIndex(1) as? LineDataSet
                lineDataSet?.values = entryList
                lineDataSet2?.values = entryList2
                chart4.data.notifyDataChanged()
                chart4.notifyDataSetChanged()
            } else {
                lineDataSet = LineDataSet(entryList, "")
                lineDataSet.axisDependency = AxisDependency.RIGHT
                lineDataSet.color = ContextCompat.getColor(this@AndroidChartActivity, R.color.green)
                lineDataSet.lineWidth = 3f
                lineDataSet.setDrawCircles(false)
                lineDataSet.setDrawValues(false)
                lineDataSet.setDrawFilled(true)
                lineDataSet.fillColor = ContextCompat.getColor(this@AndroidChartActivity, R.color.green)
                lineDataSet.fillAlpha = 10
                lineDataSet.mode = LineDataSet.Mode.HORIZONTAL_BEZIER

                //set2.setFillFormatter(new MyFillFormatter(900f));
                lineDataSet2 = LineDataSet(entryList2, "")
                lineDataSet2.axisDependency = AxisDependency.RIGHT
                lineDataSet2.color = ContextCompat.getColor(this@AndroidChartActivity, R.color.red)
                lineDataSet2.lineWidth = 3f
                lineDataSet2.setDrawCircles(false)
                lineDataSet2.setDrawValues(false)
                lineDataSet2.setDrawFilled(true)
                lineDataSet2.fillColor = ContextCompat.getColor(this@AndroidChartActivity, R.color.red)
                lineDataSet2.fillAlpha = 10
                lineDataSet2.mode = LineDataSet.Mode.HORIZONTAL_BEZIER

                // create a data object with the data sets
                val data = LineData(lineDataSet, lineDataSet2)
                data.setValueTextColor(Color.BLACK)
                data.setValueTextSize(9f)

                // set data
                chart4.data = data
            }
        }
    }
}