package com.kotlin.androidsamples

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.google.android.play.core.splitinstall.SplitInstallManagerFactory
import com.google.android.play.core.splitinstall.SplitInstallRequest
import com.google.android.play.core.splitinstall.SplitInstallStateUpdatedListener
import com.google.android.play.core.splitinstall.model.SplitInstallSessionStatus
import com.kotlin.androidsamples.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private var activityModuleName = ""

    private var mySessionId = 0

    private var moduleActivityBackStack = ""

    private val splitInstallManager by lazy { SplitInstallManagerFactory.create(this) }

    private val splitInstallManagerListener = SplitInstallStateUpdatedListener { state ->
        if (state.sessionId() == mySessionId) {
            binding.progressBar.isVisible = state.status() == SplitInstallSessionStatus.DOWNLOADING

            when (state.status()) {
                SplitInstallSessionStatus.UNKNOWN -> {
                    Log.d(this::class.java.simpleName, "UNKNOWN")
                }

                SplitInstallSessionStatus.PENDING -> {
                    Log.d(this::class.java.simpleName, "PENDING")
                }

                SplitInstallSessionStatus.REQUIRES_USER_CONFIRMATION -> {
                    Log.d(this::class.java.simpleName, "REQUIRES_USER_CONFIRMATION")
                }

                SplitInstallSessionStatus.DOWNLOADING -> {
                    Log.d(this::class.java.simpleName, "DOWNLOADING")
                }

                SplitInstallSessionStatus.DOWNLOADED -> {
                    Log.d(this::class.java.simpleName, "DOWNLOADED")
                }

                SplitInstallSessionStatus.INSTALLING -> {
                    Log.d(this::class.java.simpleName, "INSTALLING")
                }

                SplitInstallSessionStatus.INSTALLED -> {
                    Log.d(this::class.java.simpleName, "INSTALLED")
                    navigateToDynamicFeatureModule()
                }

                SplitInstallSessionStatus.FAILED -> {
                    Log.d(this::class.java.simpleName, "FAILED")
                }

                SplitInstallSessionStatus.CANCELING -> {
                    Log.d(this::class.java.simpleName, "CANCELING")
                }

                SplitInstallSessionStatus.CANCELED -> {
                    Log.d(this::class.java.simpleName, "CANCELED")
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.apply {
            btnDynamicAppIcon.setOnClickListener {
                activityModuleName = DYNAMIC_APP_LAUNCHER_ACTIVITY_MODULE
                moduleActivityBackStack = resources.getString(R.string.module_dynamic_app_launcher)
                navigateToDynamicFeatureModule()
            }
            btnMockResponseRetrofit.setOnClickListener {
                activityModuleName = MOCK_RESPONSE_RETROFIT_ACTIVITY_MODULE
                moduleActivityBackStack = resources.getString(R.string.module_mock_response_retrofit)
                navigateToDynamicFeatureModule()
            }
            btnAndroidChart.setOnClickListener {
                activityModuleName = ANDROID_CHART_ACTIVITY_MODULE
                moduleActivityBackStack = resources.getString(R.string.module_android_chart)
                navigateToDynamicFeatureModule()
            }
        }
    }

    private fun navigateToDynamicFeatureModule() {
        try {
            if (splitInstallManager.installedModules.contains(moduleActivityBackStack)) {
                val intent =
                    Intent().setClassName(this@MainActivity, activityModuleName)
                startActivity(intent)
            } else {
                initSplitInstallManager()
            }
        } catch (e: Exception) {
            Log.d(this::class.java.simpleName, "ERROR : ${e.message}")
        }
    }

    private fun initSplitInstallManager() {
        val request = SplitInstallRequest.newBuilder()
            // request by invoking the following method for each
            // module you want to install.
            .addModule(moduleActivityBackStack)
            .build()

        splitInstallManager.registerListener(splitInstallManagerListener)

        splitInstallManager
            // Submits the request to install the module through the
            // asynchronous startInstall() task. Your app needs to be
            // in the foreground to submit the request.
            .startInstall(request)
            // You should also be able to gracefully handle
            // request state changes and errors. To learn more, go to
            // the section about how to Monitor the request state.
            .addOnSuccessListener { sessionId ->
                mySessionId = sessionId
            }
            .addOnFailureListener { exception ->
                Log.d(this::class.java.simpleName, "EXCEPTION $exception")
            }
    }

    override fun onDestroy() {
        splitInstallManager.unregisterListener(splitInstallManagerListener)
        super.onDestroy()
    }

    companion object {
        private const val DYNAMIC_APP_LAUNCHER_ACTIVITY_MODULE =
            "com.kotlin.androidsamples.dynamicapplauncher.DynamicAppLauncherActivity"
        private const val MOCK_RESPONSE_RETROFIT_ACTIVITY_MODULE =
            "com.kotlin.androidsamples.mockresponseretrofit.presentation.MockResponseRetrofitActivity"
        private const val ANDROID_CHART_ACTIVITY_MODULE =
            "com.kotlin.androidsamples.androidchart.AndroidChartActivity"
    }
}