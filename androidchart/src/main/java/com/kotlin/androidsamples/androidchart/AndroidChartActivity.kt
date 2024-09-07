package com.kotlin.androidsamples.androidchart

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.YAxis.AxisDependency
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.listener.OnChartValueSelectedListener
import com.github.mikephil.charting.utils.ColorTemplate
import com.kotlin.androidsamples.androidchart.databinding.ActivityAndroidChartBinding
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import kotlin.random.Random

class AndroidChartActivity : DemoBase(), OnSeekBarChangeListener,
    OnChartValueSelectedListener {
    private lateinit var binding: ActivityAndroidChartBinding

    private lateinit var chart: LineChart
    private lateinit var seekBarX: SeekBar
    private lateinit var seekBarY: SeekBar
    private lateinit var tvX: TextView
    private lateinit var tvY: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAndroidChartBinding.inflate(layoutInflater)
        setContentView(binding.root)

        title = "LineChartActivity2"

        tvX = binding.tvXMax
        tvY = binding.tvYMax

        seekBarX = binding.seekBar1
        seekBarX.setOnSeekBarChangeListener(this)

        seekBarY = binding.seekBar2
        seekBarY.setOnSeekBarChangeListener(this)

        chart = binding.chart1
        chart.setOnChartValueSelectedListener(this)

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


        // add data
        seekBarX.progress = 7
        seekBarY.progress = 30

        chart.animateX(700)


        // get the legend (only possible after setting data)
        val l = chart.legend
        l.isEnabled = false

        val xAxis = chart.xAxis
        xAxis.enableGridDashedLine(10f, 10f, 10f)
        xAxis.setDrawAxisLine(false)

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

        val rightAxis = chart.axisRight
        rightAxis.axisMinimum = labels.first().toFloat()
        rightAxis.axisMaximum = labels.last().toFloat()
        rightAxis.isEnabled = false
    }

    private fun setData(count: Int) {
        val lowerBound = 200
        val upperBound = 1000

        val calendars = ArrayList<Calendar>()
        val values2 = ArrayList<Entry>()
        val values3 = ArrayList<Entry>()

        for (i in 0 until count) {
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
            set2.setCircleColor(Color.BLACK)
            set2.lineWidth = 2f
            set2.circleRadius = 3f
            set2.fillAlpha = 65
            set2.fillColor = Color.RED
            set2.setDrawCircleHole(false)
            set2.highLightColor = Color.rgb(244, 117, 117)

            //set2.setFillFormatter(new MyFillFormatter(900f));
            set3 = LineDataSet(values3, "")
            set3.axisDependency = AxisDependency.RIGHT
            set3.color = ContextCompat.getColor(this, R.color.red)
            set3.setCircleColor(Color.BLACK)
            set3.lineWidth = 2f
            set3.circleRadius = 3f
            set3.fillAlpha = 65
            set3.fillColor = ColorTemplate.colorWithAlpha(Color.YELLOW, 200)
            set3.setDrawCircleHole(false)
            set3.highLightColor = Color.rgb(244, 117, 117)

            // create a data object with the data sets
            val data = LineData(set2, set3)
            data.setValueTextColor(Color.BLACK)
            data.setValueTextSize(9f)

            // set data
            chart.data = data
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.line, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.viewGithub -> {
                val i = Intent(Intent.ACTION_VIEW)
                i.setData(Uri.parse("https://github.com/PhilJay/MPAndroidChart/blob/master/MPChartExample/src/com/xxmassdeveloper/mpchartexample/LineChartActivity2.java"))
                startActivity(i)
            }

            R.id.actionToggleValues -> {
                val sets = chart.data
                    .dataSets

                for (iSet in sets) {
                    val set = iSet as LineDataSet
                    set.setDrawValues(!set.isDrawValuesEnabled)
                }

                chart.invalidate()
            }

            R.id.actionToggleHighlight -> {
                if (chart.data != null) {
                    chart.data.isHighlightEnabled = !chart.data.isHighlightEnabled
                    chart.invalidate()
                }
            }

            R.id.actionToggleFilled -> {
                val sets = chart.data
                    .dataSets

                for (iSet in sets) {
                    val set = iSet as LineDataSet
                    if (set.isDrawFilledEnabled) set.setDrawFilled(false)
                    else set.setDrawFilled(true)
                }
                chart.invalidate()
            }

            R.id.actionToggleCircles -> {
                val sets = chart.data
                    .dataSets

                for (iSet in sets) {
                    val set = iSet as LineDataSet
                    if (set.isDrawCirclesEnabled) set.setDrawCircles(false)
                    else set.setDrawCircles(true)
                }
                chart.invalidate()
            }

            R.id.actionToggleCubic -> {
                val sets = chart.data
                    .dataSets

                for (iSet in sets) {
                    val set = iSet as LineDataSet
                    set.mode = if (set.mode == LineDataSet.Mode.CUBIC_BEZIER
                    ) LineDataSet.Mode.LINEAR
                    else LineDataSet.Mode.CUBIC_BEZIER
                }
                chart.invalidate()
            }

            R.id.actionToggleStepped -> {
                val sets = chart.data
                    .dataSets

                for (iSet in sets) {
                    val set = iSet as LineDataSet
                    set.mode = if (set.mode == LineDataSet.Mode.STEPPED
                    ) LineDataSet.Mode.LINEAR
                    else LineDataSet.Mode.STEPPED
                }
                chart.invalidate()
            }

            R.id.actionToggleHorizontalCubic -> {
                val sets = chart.data
                    .dataSets

                for (iSet in sets) {
                    val set = iSet as LineDataSet
                    set.mode = if (set.mode == LineDataSet.Mode.HORIZONTAL_BEZIER
                    ) LineDataSet.Mode.LINEAR
                    else LineDataSet.Mode.HORIZONTAL_BEZIER
                }
                chart.invalidate()
            }

            R.id.actionTogglePinch -> {
                if (chart.isPinchZoomEnabled) chart.setPinchZoom(false)
                else chart.setPinchZoom(true)

                chart.invalidate()
            }

            R.id.actionToggleAutoScaleMinMax -> {
                chart.isAutoScaleMinMaxEnabled = !chart.isAutoScaleMinMaxEnabled
                chart.notifyDataSetChanged()
            }

            R.id.animateX -> {
                chart.animateX(2000)
            }

            R.id.animateY -> {
                chart.animateY(2000)
            }

            R.id.animateXY -> {
                chart.animateXY(2000, 2000)
            }

            R.id.actionSave -> {
                if (ContextCompat.checkSelfPermission(
                        this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                    ) == PackageManager.PERMISSION_GRANTED
                ) {
                    saveToGallery()
                } else {
                    requestStoragePermission(chart)
                }
            }
        }
        return true
    }

    override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
        tvX.text = seekBarX.progress.toString()
        tvY.text = seekBarY.progress.toString()

        setData(seekBarX.progress)

        // redraw
        chart.invalidate()
    }

    override fun saveToGallery() {
        saveToGallery(chart, "LineChartActivity2")
    }

    override fun onValueSelected(e: Entry, h: Highlight) {
        Log.i("Entry selected", e.toString())

        chart.centerViewToAnimated(
            e.x, e.y, chart.data.getDataSetByIndex(h.dataSetIndex)
                .axisDependency, 500
        )
        //chart.zoomAndCenterAnimated(2.5f, 2.5f, e.getX(), e.getY(), chart.getData().getDataSetByIndex(dataSetIndex)
        // .getAxisDependency(), 1000);
        //chart.zoomAndCenterAnimated(1.8f, 1.8f, e.getX(), e.getY(), chart.getData().getDataSetByIndex(dataSetIndex)
        // .getAxisDependency(), 1000);
    }

    override fun onNothingSelected() {
        Log.i("Nothing selected", "Nothing selected.")
    }

    override fun onStartTrackingTouch(seekBar: SeekBar?) {}

    override fun onStopTrackingTouch(seekBar: SeekBar?) {}
}