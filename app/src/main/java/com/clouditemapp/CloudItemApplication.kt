package com.clouditemapp

import android.app.Application
import com.clouditemapp.data.initializer.DataInitializer
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class CloudItemApplication : Application() {

    @Inject
    lateinit var dataInitializer: DataInitializer

    override fun onCreate() {
        super.onCreate()
        // 初始化示例数据
        dataInitializer.initializeData()
    }
}