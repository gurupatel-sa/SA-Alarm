package com.sa.alarm.home.reminder

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.sa.alarm.R
import com.sa.alarm.base.BaseFragment
import com.sa.alarm.utils.LogUtils
import kotlinx.android.synthetic.main.fragment_reminder.*
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.sa.alarm.home.addreminder.AddReminder
import com.sa.alarm.home.HomeActivity
import com.sa.alarm.home.reminder.adapter.MyReminderAdapter
import com.sa.alarm.home.reminder.adapter.OtherReminderAdapter

class ReminderFragment :BaseFragment() {

    private lateinit var reminderViewModel: ReminderViewModel
    private lateinit var myAdapter : MyReminderAdapter
    private lateinit var otherAdapter : OtherReminderAdapter
    companion object{
        private var reminderInstance: ReminderFragment? = null
         val TAG : String = "ReminderFragment"

        fun getInstance(): ReminderFragment {
            if (reminderInstance == null) {
                reminderInstance =
                    ReminderFragment()
            }
            return reminderInstance as ReminderFragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        LogUtils.d(TAG,"onCreateView")
        return inflater.inflate(R.layout.fragment_reminder , container ,false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
        fabAddReminder.setOnClickListener {
            (activity as HomeActivity).replaceFragments(AddReminder.TAG , AddReminder.getInstance())
        }

        appbar.setNavigationOnClickListener(View.OnClickListener {
            //open bottom sheet
            val bottomSheetDialogFragment = BottomSheetFragment().newInstance()
            bottomSheetDialogFragment.show(activity!!.supportFragmentManager, "Bottom Sheet Dialog Fragment")
        })
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        reminderViewModel.newData.observe(activity!! , Observer {dataInserted ->
            if(dataInserted.type == 0)
                reminderViewModel.getMyReminders()
            else
                reminderViewModel.getOtherReminders()
        })

        reminderViewModel.myReminderList.observe(this, Observer {
            LogUtils.d(TAG, "myReminderList  :" + it?.size)
            myAdapter.setData(it)
        })

        reminderViewModel.otherReminderList.observe(this, Observer {
            LogUtils.d(TAG, "otherReminderList  :" + it?.size)
            otherAdapter.setData(it)
        })
    }

    private fun init() {
        reminderViewModel = ViewModelProviders.of(activity!!).get(ReminderViewModel::class.java)

        rvMyReminders.setLayoutManager(LinearLayoutManager(context , RecyclerView.VERTICAL,false))
        rvMyReminders.setHasFixedSize(true)
        myAdapter= MyReminderAdapter(context!!)
        rvMyReminders.adapter = myAdapter

        rvOtherReminders.setLayoutManager(LinearLayoutManager(context , RecyclerView.VERTICAL,false))
        rvOtherReminders.setHasFixedSize(true)
        otherAdapter= OtherReminderAdapter(context!!)
        rvOtherReminders.adapter = otherAdapter
    }
}