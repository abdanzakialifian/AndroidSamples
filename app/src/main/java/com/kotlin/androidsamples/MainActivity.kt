package com.kotlin.androidsamples

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.google.android.play.core.splitinstall.SplitInstallManagerFactory
import com.google.android.play.core.splitinstall.SplitInstallRequest
import com.google.android.play.core.splitinstall.SplitInstallStateUpdatedListener
import com.google.android.play.core.splitinstall.model.SplitInstallSessionStatus
import com.kotlin.androidsamples.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private var mySessionId = 0

    private val moduleActivityBackStack by lazy { resources.getString(R.string.module_dynamic_app_launcher) }

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
                    Toast.makeText(this, "INSTALLED MODULES", Toast.LENGTH_SHORT).show()

                    val packageActivityName =
                        "com.kotlin.androidsamples.features.activitybackstack.FirstActivity"
                    val intent = Intent().setClassName(this, packageActivityName)
                    startActivity(intent)
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
            btnActivityBackstack.setOnClickListener {
                if (splitInstallManager.installedModules.contains(moduleActivityBackStack)) {
                    Toast.makeText(this@MainActivity, "ALREADY MODULE", Toast.LENGTH_SHORT).show()

                    val packageActivityName =
                        "com.kotlin.androidsamples.dynamicapplauncher.DynamicAppLauncherActivity"
                    val intent = Intent().setClassName(this@MainActivity, packageActivityName)
                    startActivity(intent)
                } else {
                    initSplitInstallManager()
                }
            }

            btnDynamicAppIcon.setOnClickListener {
                startService(Intent(this@MainActivity, AppIconChangeService::class.java))
            }
        }
    }

    private fun initSplitInstallManager() {
        val request = SplitInstallRequest.newBuilder()
            // request by invoking the following method for each
            // module you want to install.
            .addModule(moduleActivityBackStack)
            .build()

        Toast.makeText(this, "START INSTALL MODULE", Toast.LENGTH_SHORT).show()

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
                Toast.makeText(this, "EXCEPTION : $exception", Toast.LENGTH_SHORT).show()
                Log.d(this::class.java.simpleName, "EXCEPTION $exception")
            }
    }



    override fun onDestroy() {
        splitInstallManager.unregisterListener(splitInstallManagerListener)
        super.onDestroy()
    }
}