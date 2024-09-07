package com.kotlin.androidsamples.androidchart

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.kotlin.androidsamples.androidchart.databinding.ActivityAndroidChartBinding

class AndroidChartActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAndroidChartBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAndroidChartBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}