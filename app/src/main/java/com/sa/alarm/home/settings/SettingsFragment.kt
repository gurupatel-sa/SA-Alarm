package com.sa.alarm.home.settings

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.firebase.auth.FirebaseAuth
import com.sa.alarm.R
import com.sa.alarm.auth.AuthActivity
import com.sa.alarm.base.BaseFragment
import com.sa.alarm.common.Constants
import com.sa.alarm.home.HomeActivity
import com.sa.alarm.utils.LogUtils
import com.sa.alarm.utils.SharedPrefUtils
import kotlinx.android.synthetic.main.fragment_settings.*
import kotlinx.android.synthetic.main.toolbar_main.view.*

class SettingsFragment :BaseFragment() {
    companion object {
        private var profileInstance: SettingsFragment? = null
        val TAG: String = "SettingsFragment"

        fun getInstance(): SettingsFragment {
            if (profileInstance == null) {
                profileInstance = SettingsFragment()
            }
            return profileInstance as SettingsFragment
        }
    }
    private lateinit var settingsViewModel: SettingsViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        LogUtils.d(TAG, "onCreateViews")
        return inflater.inflate(R.layout.fragment_settings, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()

        toolbar.ivBack.setOnClickListener {
            (activity as HomeActivity).supportFragmentManager.popBackStackImmediate();
        }

        settingsViewModel.availabilityStatus.observe(this , Observer {status ->
             swAvailability.isChecked =status
        })
        settingsViewModel.notficationStatus.observe(this , Observer {status ->
            swNotification.isChecked =status
        })

        settingsViewModel.isResetMailSent.observe(this , Observer { staus ->
            if(staus){
                Toast.makeText(activity!! ,resources.getString(R.string.password_reset_successful) ,Toast.LENGTH_SHORT).show()
            }
            else{
                Toast.makeText(activity!! ,resources.getString(R.string.password_reset_faliure) ,Toast.LENGTH_SHORT).show()
            }
        })

        swAvailability?.setOnCheckedChangeListener { _, isChecked ->
            if(isChecked != settingsViewModel.availabilityStatus.value!!)
                settingsViewModel.updateAvailability(isChecked)
        }

        swNotification?.setOnCheckedChangeListener { _, isChecked ->
            if(isChecked != settingsViewModel.notficationStatus.value!!)
                settingsViewModel.updateNotification(isChecked)
        }


        tvLogout.setOnClickListener {
            SharedPrefUtils.remove(Constants.IS_LOGGED_IN)
            SharedPrefUtils.remove(Constants.USER_ID)

            FirebaseAuth.getInstance().signOut()
            startActivity(Intent(activity!! , AuthActivity::class.java))
            activity!!.finish()
        }

        tvChangePassword.setOnClickListener {
            settingsViewModel.changePassword()
        }
    }

    private fun init() {
        toolbar.tvTitle.setText(resources.getText(R.string.title_settings))
        settingsViewModel = ViewModelProviders.of(this).get(SettingsViewModel::class.java)
    }
}