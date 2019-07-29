package com.sa.alarm.splash

import android.os.Bundle
import com.sa.alarm.R
import com.sa.alarm.base.BaseActivity
import com.sa.alarm.utils.LogUtils

class DummyActivity : BaseActivity() {
    private val TAG : String = this.javaClass.getSimpleName()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dummy)

        LogUtils.d(TAG,"onCreate :")
    }
}
