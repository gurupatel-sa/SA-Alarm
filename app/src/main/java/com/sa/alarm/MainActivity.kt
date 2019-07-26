package com.sa.alarm

import android.content.Intent
import android.os.Bundle
import com.sa.alarm.base.BaseActivity
import com.sa.alarm.splash.DummyActivity
import com.sa.alarm.utils.LogUtils
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseActivity() {

    var TAG : String = this.javaClass.getSimpleName()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

//        registerNetworkReciver()
        LogUtils.d(TAG,"onCreate : init")

        click.setOnClickListener {
            startActivity(Intent(this ,DummyActivity::class.java))
        }

    }
}
