package com.lcpetlylgmg.petly.common.service.ui

import android.annotation.SuppressLint
import android.app.Activity
import android.graphics.Bitmap
import android.os.Bundle
import android.view.View
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import com.lcpetlylgmg.petly.common.service.data.WebViewParams
import com.lcpetlylgmg.petly.databinding.WebviewActivityBinding
import com.lcpetlylgmg.petly.organization.post.data.Post
import com.lcpetlylgmg.petly.utils.GlobalKeys

class WebViewActivity : Activity() {

    private lateinit var binding : WebviewActivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = WebviewActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val params = intent.getParcelableExtra<WebViewParams>(GlobalKeys.WEB_DATA)
        binding.productName.text = params?.name.toString()
        loadWebData(params?.link.toString())
        binding.buttonBack.setOnClickListener { finish() }
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun loadWebData(urlWeb: String){
        binding.webview.settings.javaScriptEnabled = true
        binding.webview.clearHistory()
        binding.webview.clearCache(false)
        binding.webview.loadUrl(urlWeb)
        binding.webview.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
                val url = request?.url.toString()
                view?.loadUrl(url)
                return super.shouldOverrideUrlLoading(view, request)
            }

            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                binding.laoder.visibility = View.VISIBLE
                super.onPageStarted(view, url, favicon)
            }

            override fun onPageFinished(view: WebView?, url: String?) {
                binding.laoder.visibility = View.GONE
                super.onPageFinished(view, url)
            }

            override fun onReceivedError(view: WebView, request: WebResourceRequest, error: WebResourceError) {
                super.onReceivedError(view, request, error)
            }
        }
    }
}