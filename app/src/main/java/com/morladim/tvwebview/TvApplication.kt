package com.morladim.tvwebview

import android.app.Application

/**
 *
 * @Author 5k5k
 * @Date 2022/12/25
 */
class TvApplication : Application() {

    companion object {
        var tvApplication: TvApplication? = null

        fun getInstance(): TvApplication? {
            return tvApplication
        }
    }

    override fun onCreate() {
        super.onCreate()
        tvApplication = this
    }


}