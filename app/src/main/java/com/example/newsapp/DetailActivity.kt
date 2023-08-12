package com.example.newsapp

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class DetailActivity : AppCompatActivity() {

    lateinit var webView:WebView
    lateinit var progressBar: ProgressBar
    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_activty)


        webView=findViewById(R.id.webView)
        progressBar=findViewById(R.id.progressBar)
        val url =intent.getStringExtra("URL")
        if(url!=null){
            webView.settings.userAgentString = "Mozilla/5.0 (Linux; Android 10; Build/ABCD123) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/92.0.4515.131 Mobile Safari/537.36"
            webView.settings.javaScriptEnabled=true
            webView.webViewClient = object: WebViewClient(){
                override fun onPageFinished(view: WebView?, url: String?) {
                    super.onPageFinished(view, url)
                    progressBar.visibility=View.GONE
                    webView.visibility=View.VISIBLE
                }
            }
            webView.loadUrl(url)
        }else{
            Toast.makeText(this@DetailActivity,"no page found",Toast.LENGTH_SHORT).show()
        }

    }
}