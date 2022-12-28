package com.morladim.tvwebview

import android.os.Bundle
import android.os.Handler
import android.view.KeyEvent
import android.view.View
import android.view.Window
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.FrameLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity(), FavoriteAdapter.ItemListener {

    private var webView: WebView? = null
    private var contentView: FrameLayout? = null
    private val favoriteList: ArrayList<Favorite> = ArrayList()
    private var recyclerView: RecyclerView? = null

    private var menuMode = false
    private val defaultIcon = "https://www.baidu.com/favicon.ico"

    private var currentChannel = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.activity_main)
        handler = Handler(mainLooper)
        initView()
        initWebView()
        initFavoriteList()
//        webView.loadUrl("https://youku.com/")
//        webView.loadUrl("https://v.qq.com/")
//        webView.loadUrl("https://www.iqiyi.com/")
        favoriteList[0].let {
            webView?.loadUrl(it.url)
        }
    }

    private fun initView() {
        webView = findViewById(R.id.webView)
        contentView = findViewById(R.id.videoContainer)
        recyclerView = findViewById(R.id.recyclerView)
        recyclerView!!.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        val mAdapter = FavoriteAdapter(favoriteList, this)
        recyclerView!!.adapter = mAdapter
        recyclerView!!.visibility = View.VISIBLE
    }

    private fun initWebView() {
        webView?.isClickable = true
        val webSetting = webView?.settings
        webSetting?.javaScriptEnabled = true
        webSetting?.setSupportZoom(false)
        webSetting?.builtInZoomControls = true
        webSetting?.loadWithOverviewMode = true
        webSetting?.useWideViewPort = true
        webSetting?.domStorageEnabled = true
        webSetting?.mediaPlaybackRequiresUserGesture = false

        webSetting?.javaScriptCanOpenWindowsAutomatically = true
        webSetting?.allowFileAccess = true
//        webSetting.builtInZoomControls = true
//        webSetting.setSupportMultipleWindows(true)
        webSetting?.cacheMode = WebSettings.LOAD_NO_CACHE

        webSetting?.userAgentString =
            "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/105.0.0.0 Safari/537.36"
        webView?.webViewClient = object : WebViewClient() {

            override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
                return if (url.startsWith("http")) {
                    view.loadUrl(url)
                    true
                } else {
                    false
                }
            }
        }

        webView?.webChromeClient = object : WebChromeClient() {

            var mCallBack: CustomViewCallback? = null

            override fun onShowCustomView(view: View?, callback: CustomViewCallback?) {
                super.onShowCustomView(view, callback)
                mCallBack = callback
                contentView?.removeAllViews()
                contentView?.addView(view)
                contentView?.visibility = View.VISIBLE
            }

            override fun onHideCustomView() {
                super.onHideCustomView()
                mCallBack?.onCustomViewHidden();
                contentView?.removeAllViews()
                contentView?.visibility = View.GONE
            }
        }
    }

    private fun initFavoriteList() {
        favoriteList.add(
            Favorite(
                "https://img.alicdn.com/tfs/TB1WeJ9Xrj1gK0jSZFuXXcrHpXa-195-195.png",
                "https://www.youtube.com/watch?v=AWR-M0Q-RTY",
                "youtube"
            )
        )
        favoriteList.add(
            Favorite(
                "https://img.alicdn.com/tfs/TB1WeJ9Xrj1gK0jSZFuXXcrHpXa-195-195.png",
                "https://youku.com/",
                "youku"
            )
        )
        favoriteList.add(
            Favorite(
                "https://www.bilibili.com/favicon.ico?v=1",
                "https://www.bilibili.com/",
                "bilibili"
            )
        )
        favoriteList.add(
            Favorite(
                "https://v.qq.com/favicon.ico",
                "https://v.qq.com/",
                "tengxun"
            )
        )
        favoriteList.add(
            Favorite(
                "https://www.iqiyipic.com/pcwimg/128-128-logo.png",
                "https://www.iqiyi.com/",
                "aiqiyi"
            )
        )
        favoriteList.add(
            Favorite(
                defaultIcon,
                "https://www.baidu.com/",
                "baidu"
            )
        )
    }

    private fun loadNextChannel() {
        currentChannel = (currentChannel + 1) % favoriteList.size
        webView?.stopLoading()
        favoriteList[currentChannel].let {
            webView?.loadUrl(it.url)
        }
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        Toast.makeText(this, keyCode.toString(), Toast.LENGTH_LONG).show()
        if (keyCode == 82) {
            if (menuMode) {
                loadNextChannel()
            } else {
                turnOnMenu()
            }

            handler?.removeCallbacksAndMessages(null)
            handler?.postDelayed({
                turnOffMenu()
            }, 10_000)
        }
        return super.onKeyDown(keyCode, event)
    }

    private var handler: Handler? = null

    private fun turnOnMenu() {
        menuMode = true
        recyclerView?.visibility = View.VISIBLE
    }

    private fun turnOffMenu() {
        menuMode = false
        recyclerView?.visibility = View.GONE
    }

    var backTime = 0L
    override fun onBackPressed() {
        if (menuMode) {
            turnOffMenu()
            return
        }
        if (webView != null && webView!!.canGoBack()) {
            webView!!.stopLoading()
            webView!!.goBack()
        } else {
            if (System.currentTimeMillis() - backTime < 2000) {
                super.onBackPressed()
            } else {
                backTime = System.currentTimeMillis()
                Toast.makeText(this, "再按一次就关了", Toast.LENGTH_SHORT).show()
            }
        }
    }


    override fun onItemClick(item: Favorite) {
        webView?.loadUrl(item.url)
    }
}