package com.kotlin.androidsamples.dynamicapplauncher

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.kotlin.androidsamples.dynamicapplauncher.databinding.ActivityDynamicAppLauncherBinding

class DynamicAppLauncherActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDynamicAppLauncherBinding

    private val dynamicAppLauncherBroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            val fetchState = intent?.getStringExtra(FETCH_STATE)
            binding.apply {
                when (fetchState) {
                    LOADING -> {
                        progressBar.visibility = View.VISIBLE
                        tvFetchState.visibility = View.GONE
                    }

                    SUCCESS -> {
                        progressBar.visibility = View.GONE
                        tvFetchState.visibility = View.VISIBLE
                        tvFetchState.text = String.format("SUCCESS FETCH REMOTE CONFIG")
                    }

                    FAILED -> {
                        progressBar.visibility = View.GONE
                        tvFetchState.visibility = View.VISIBLE
                        tvFetchState.text = String.format("FAILED FETCH REMOTE CONFIG")
                    }
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDynamicAppLauncherBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initBroadcastReceiver()
        initService()
    }

    private fun initBroadcastReceiver() {
        LocalBroadcastManager.getInstance(this)
            .registerReceiver(dynamicAppLauncherBroadcastReceiver, IntentFilter(ACTION_FETCH_STATE))
    }

    private fun initService() {
        startService(Intent(this, DynamicAppLauncherService::class.java))
    }

    override fun onDestroy() {
        LocalBroadcastManager.getInstance(this)
            .unregisterReceiver(dynamicAppLauncherBroadcastReceiver)
        super.onDestroy()
    }

    companion object {
        const val ACTION_FETCH_STATE = "action_fetch_state"
        const val FETCH_STATE = "fetch_state"
        const val LOADING = "loading"
        const val SUCCESS = "success"
        const val FAILED = "failed"
    }
}