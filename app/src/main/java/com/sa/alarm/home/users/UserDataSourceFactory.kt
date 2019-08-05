package com.sa.alarm.home.users

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import androidx.paging.PageKeyedDataSource
import com.google.firebase.firestore.Query
import com.sa.alarm.auth.register.model.User
import com.sa.alarm.utils.LogUtils

class UserDataSourceFactory : DataSource.Factory<Query, User>() {

    private val TAG : String = this.javaClass.getSimpleName()
    private val itemLiveDataSource : MutableLiveData<PageKeyedDataSource<Query,User>> = MutableLiveData()

    override fun create(): DataSource<Query, User> {
        LogUtils.d(TAG,"create :")

        val itemDataSource = UserDataSource()
        itemLiveDataSource.postValue(itemDataSource)
        return itemDataSource
    }


    fun getItemLiveDataSource(): MutableLiveData<PageKeyedDataSource<Query, User>> {
        return itemLiveDataSource
    }
}