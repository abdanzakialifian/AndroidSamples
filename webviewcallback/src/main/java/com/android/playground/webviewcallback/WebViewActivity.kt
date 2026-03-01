package com.android.playground.webviewcallback

import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.android.playground.webviewcallback.databinding.ActivityWebViewBinding

class WebViewActivity : AppCompatActivity() {
    private lateinit var binding: ActivityWebViewBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWebViewBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val webUrl = intent?.getStringExtra(EXTRA_WEB_URL)
        setupWebView(webUrl)
    }

    private fun setupWebView(url: String?) {
        binding.apply {
            webView.loadUrl(url ?: "")
            webView.settings.apply {
                javaScriptEnabled = true
                domStorageEnabled = true
                userAgentString = "Mozilla/5.0 (Android) AppleWebKit/537.36 (KHTML, like Gecko) Chrome Safari"
            }
            webView.webViewClient = object : WebViewClient() {
                override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                    view?.url
                    visibilityView(true)
                }

                override fun onPageFinished(view: WebView?, url: String?) {
                    visibilityView(false)
                }

                /**
                 * Called when the WebView is about to load a new URL.
                 *
                 * This method is triggered in several scenarios:
                 * - When the user clicks on a link inside the WebView content.
                 * - When the page performs a redirect (HTTP redirect or meta refresh).
                 * - When JavaScript explicitly changes the window location (e.g., window.location).
                 *
                 * Use this method to intercept and decide how to handle the request:
                 * - Return `true` if your app handles the URL manually (WebView will not load it).
                 * - Return `false` if you want the WebView to continue loading the URL as usual.
                 *
                 * Common use cases:
                 * - Intercepting custom scheme URLs (e.g., app://, myapp://) to trigger app logic.
                 * - Redirecting specific URLs to external apps (e.g., tel:, mailto:).
                 * - Blocking or filtering unwanted requests before they are loaded.
                 *
                 * @param view The WebView that is initiating the callback.
                 * @param request The WebResourceRequest object containing details about the URL request,
                 * including the URL, HTTP method, and request headers.
                 * @return `true` if the host application handles the URL, `false` if the WebView should load it.
                 */
                override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
                    Log.d("CEK", "SHOULD OVERRIDE URL LOADING : URL ${request?.url}")
                    Log.d("CEK", "SHOULD OVERRIDE URL LOADING : PATH ${request?.url?.path}")
                    Log.d("CEK", "SHOULD OVERRIDE URL LOADING : PATH SEGMENTS ${request?.url?.pathSegments}")
                    Log.d("CEK", "SHOULD OVERRIDE URL LOADING : LAST PATH SEGMENT ${request?.url?.lastPathSegment}")
                    Log.d("CEK", "SHOULD OVERRIDE URL LOADING : REQUEST HEADERS ${request?.requestHeaders}")
                    Log.d("CEK", "SHOULD OVERRIDE URL LOADING : METHOD ${request?.method}")

                    if (request?.url?.toString()?.contains("") == true) {
                        val intent = Intent().apply { putExtra(WebViewCallbackActivity.EXTRA_PAYMENT_CODE, request.url?.lastPathSegment) }
                        setResult(RESULT_OK, intent)
                        finish()
                    }

                    return true
                }
            }
        }
    }

    private fun visibilityView(isLoading: Boolean) {
        binding.progressBar.isVisible = isLoading
        binding.webView.isVisible = !isLoading
    }

    companion object {
        const val EXTRA_WEB_URL = "extra_web_url"
    }
}