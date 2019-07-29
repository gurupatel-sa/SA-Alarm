package com.sa.alarm.users

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.paging.LivePagedListBuilder
import androidx.paging.PageKeyedDataSource
import androidx.paging.PagedList
import com.google.firebase.firestore.Query
import com.sa.alarm.register.model.User

class UsersViewModel : ViewModel() {

    private val TAG: String = this.javaClass.getSimpleName()
    var userPagedList: LiveData<PagedList<User>>
    var liveDataSource: LiveData<PageKeyedDataSource<Query, User>>
    var userDataSourceFactory: UserDataSourceFactory
    private val PAGE_SIZE :Int = 4

    init {
        userDataSourceFactory = UserDataSourceFactory()
        liveDataSource = userDataSourceFactory.getItemLiveDataSource()

        val config = PagedList.Config.Builder()
            .setPageSize(PAGE_SIZE)
            .setEnablePlaceholders(true)
            .build()

        userPagedList = LivePagedListBuilder(userDataSourceFactory, config).build()
    }

    fun refreshAdapter() {
        userDataSourceFactory.getItemLiveDataSource().getValue()?.invalidate();
    }

}