package com.kotlin.androidsamples.webviewcallback

import android.content.Intent
import android.os.Bundle
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.kotlin.androidsamples.webviewcallback.databinding.ActivityWebViewCallbackBinding

class WebViewCallbackActivity : AppCompatActivity() {
    private lateinit var binding: ActivityWebViewCallbackBinding

    val webViewLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult(), ::webViewResultLauncher)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWebViewCallbackBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnGoToWebView.setOnClickListener {
            val intent = Intent(this, WebViewActivity::class.java).apply {
                putExtra(
                    WebViewActivity.EXTRA_WEB_URL,
                    ""
                )
            }
            webViewLauncher.launch(intent)
        }
    }

    private fun webViewResultLauncher(result: ActivityResult) {
        if (result.resultCode != RESULT_OK) {
            return
        }
        val paymentCode = result.data?.getStringExtra(EXTRA_PAYMENT_CODE)
        binding.tvResult.text = "Payment Code : $paymentCode"
    }

    companion object {
        const val EXTRA_PAYMENT_CODE = "extra_payment_code"
    }
}