package com.kotlin.androidsamples.mockresponseretrofit

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.kotlin.androidsamples.mockresponseretrofit.databinding.ActivityMockResponseRetrofitBinding

class MockResponseRetrofitActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMockResponseRetrofitBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMockResponseRetrofitBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}