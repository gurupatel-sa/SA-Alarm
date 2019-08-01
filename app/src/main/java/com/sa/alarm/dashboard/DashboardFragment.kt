package com.sa.alarm.dashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.sa.alarm.R
import com.sa.alarm.base.BaseFragment
import com.sa.alarm.utils.LogUtils
import kotlinx.android.synthetic.main.fragment_dashboard.*
import android.graphics.Rect
import androidx.appcompat.widget.PopupMenu



class DashboardFragment :BaseFragment() {
    companion object{
        private var dashboardInstance: DashboardFragment? = null

        fun getInstance(): DashboardFragment {
            if (dashboardInstance == null) {
                dashboardInstance = DashboardFragment()
            }
            return dashboardInstance as DashboardFragment
        }
    }

    private val TAG : String = this.javaClass.getSimpleName()


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        LogUtils.d(TAG,"onCreateView")
        return inflater.inflate(R.layout.fragment_dashboard , container ,false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        appbar.setOnClickListener {
           var pos=  locateView(it)
        }
    }

    fun locateView(v: View?): Rect? {
        val loc_int = IntArray(2)
        if (v == null) return null
        try {
            v.getLocationOnScreen(loc_int)
        } catch (npe: NullPointerException) {
            //Happens when the view doesn't exist on screen anymore.
            return null
        }

        val location = Rect()
        location.left = loc_int[0]
        location.top = loc_int[1]
        location.right = location.left + v.width
        location.bottom = location.top + v.height
        return location
    }

}