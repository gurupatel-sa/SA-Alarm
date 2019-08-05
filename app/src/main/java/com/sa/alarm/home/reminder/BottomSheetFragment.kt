package com.sa.alarm.home.reminder

import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import android.app.Dialog
import android.content.Intent
import com.google.android.material.bottomsheet.BottomSheetBehavior
import android.os.Bundle
import android.view.View
import com.google.firebase.auth.FirebaseAuth
import com.sa.alarm.R
import com.sa.alarm.auth.AuthActivity
import com.sa.alarm.common.Constants
import com.sa.alarm.home.HomeActivity
import com.sa.alarm.home.profile.ProfileFragment
import com.sa.alarm.home.settings.SettingsFragment
import com.sa.alarm.home.users.UsersFragment
import com.sa.alarm.utils.SharedPrefUtils
import kotlinx.android.synthetic.main.fragment_bottom_sheet.*

class BottomSheetFragment : BottomSheetDialogFragment(){
     fun newInstance(): BottomSheetFragment {

        val args = Bundle()

        val fragment = BottomSheetFragment()
        fragment.setArguments(args)
        return fragment
    }

    //Bottom Sheet Callback
    private val mBottomSheetBehaviorCallback = object : BottomSheetBehavior.BottomSheetCallback() {
        override fun onSlide(p0: View, p1: Float) {

        }

        override fun onStateChanged(p0: View, newState: Int) {
            if (newState == BottomSheetBehavior.STATE_HIDDEN) {
                dismiss();
            }
        }
    }

    override fun setupDialog(dialog: Dialog, style: Int) {
        val contentView = View.inflate(context, R.layout.fragment_bottom_sheet, null)
        dialog.setContentView(contentView)

        dialog.tvUserList.setOnClickListener {
            dialog.dismiss()
            (activity as HomeActivity).replaceFragments(UsersFragment.TAG , UsersFragment.getInstance())
        }

        dialog.tvProfile.setOnClickListener {
            dialog.dismiss()
            (activity as HomeActivity).replaceFragments(ProfileFragment.TAG , ProfileFragment.getInstance())
        }

        dialog.tvSettings.setOnClickListener {
            dialog.dismiss()
            (activity as HomeActivity).replaceFragments(SettingsFragment.TAG , SettingsFragment.getInstance())



        }
    }
}