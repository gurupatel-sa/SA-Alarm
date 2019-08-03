package com.sa.alarm.home.profile

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.Glide
import com.sa.alarm.R
import com.sa.alarm.base.BaseFragment
import com.sa.alarm.home.HomeActivity
import com.sa.alarm.utils.LogUtils
import kotlinx.android.synthetic.main.dialog_select_camera.*
import kotlinx.android.synthetic.main.fragment_profile.*
import kotlinx.android.synthetic.main.fragment_profile.etEmail
import kotlinx.android.synthetic.main.toolbar_main.view.ivBack
import kotlinx.android.synthetic.main.toolbar_main.view.tvTitle

class ProfileFragment : BaseFragment() {
    companion object{
        private var profileInstance: ProfileFragment? = null
         val TAG : String = "ProfileFragment"
        var dialog :Dialog? =null

        fun getInstance(): ProfileFragment {
            if (profileInstance == null) {
                profileInstance = ProfileFragment()
            }
            return profileInstance as ProfileFragment
        }
    }
    private lateinit var profileViewModel: ProfileViewmodel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        LogUtils.d(TAG,"onCreateViews")
        return inflater.inflate(R.layout.fragment_profile , container ,false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()

        toolbar.ivBack.setOnClickListener {
            (activity as HomeActivity).supportFragmentManager.popBackStackImmediate();
        }

        profileViewModel.getUser().observe(this , Observer { user ->
            etEmail.setText(user.email)
            etName.setText(user.displayName)
            tvName.setText(user.displayName)

            Glide.with(this)
                .load(user.photoUrl)
                .into(civProfile)
        })

        btnSubmitPofile.setOnClickListener {
            if(isValid()){
                profileViewModel.submitData(etName.text.toString())
            }
        }

        profileViewModel.getProgresBar().observe(this, Observer { isLoading ->
            if(isLoading) pgbProfile.visibility = View.VISIBLE else pgbProfile.visibility = View.GONE
        })

        camera.setOnClickListener{
            dialog = Dialog(context!!)
            dialog?.setContentView(R.layout.dialog_select_camera);

            dialog?.tvSelectGallary?.setOnClickListener {
            }

            dialog?.tvSelectCamera?.setOnClickListener {
            }

            val window = dialog?.getWindow()
            window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)

            dialog?.show()
        }
    }

    private fun isValid(): Boolean {
        if(etName.text.toString().isEmpty()){
            etName.error = resources.getString(R.string.error_email_empty)
            return false
        }
        return true
    }

    private fun init() {
        toolbar.tvTitle.setText(resources.getText(R.string.title_edit_profile))
        profileViewModel = ViewModelProviders.of(this).get(ProfileViewmodel::class.java)
    }
}