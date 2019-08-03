package com.sa.alarm.home.users

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.sa.alarm.R
import com.sa.alarm.base.BaseFragment
import com.sa.alarm.home.HomeActivity
import com.sa.alarm.home.users.adapter.ItemAdapter
import com.sa.alarm.utils.LogUtils
import kotlinx.android.synthetic.main.fragment_users.*
import kotlinx.android.synthetic.main.fragment_users.pgbLogin
import kotlinx.android.synthetic.main.toolbar_main.view.*

class UsersFragment :BaseFragment() {
    companion object {
        private var usersInstance: UsersFragment? = null
        val TAG : String = "UsersFragment"
        fun getInstance(): UsersFragment {
            if (usersInstance == null) {
                usersInstance = UsersFragment()
            }
            return usersInstance as UsersFragment
        }
    }

    private lateinit var usersViewModel : UsersViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        LogUtils.d(TAG,"onCreateView :")
        return inflater.inflate(R.layout.fragment_users ,container ,false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()

        swrLoadNewUser.setOnRefreshListener {

            swrLoadNewUser.isRefreshing = false
            usersViewModel.refreshAdapter()
        }
        toolbar.ivBack.setOnClickListener {
            (activity as HomeActivity).supportFragmentManager.popBackStackImmediate();
        }

        usersViewModel.getProgresBar().observe(this, Observer { isLoading ->
            if(isLoading) pgbLogin.visibility = View.VISIBLE else pgbLogin.visibility = View.GONE
        })
    }

    private fun init() {

        toolbar.tvTitle.setText(resources.getText(R.string.title_user))

        usersViewModel = ViewModelProviders.of(this).get(UsersViewModel::class.java)

        rvUsers.setLayoutManager(LinearLayoutManager(context , RecyclerView.VERTICAL,false))
        rvUsers.setHasFixedSize(true)

        val adapter = ItemAdapter(context!!)

        usersViewModel.userPagedList.observe(this , Observer {
            adapter.submitList(it)
            usersViewModel.isLoading.value =false
        })

        rvUsers.setAdapter(adapter)
    }
}