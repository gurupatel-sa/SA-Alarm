package com.sa.alarm.home.users

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.paging.LivePagedListBuilder
import androidx.paging.PageKeyedDataSource
import androidx.paging.PagedList
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.sa.alarm.common.Constants
import com.sa.alarm.auth.register.model.User

class UsersViewModel : ViewModel() {

    private val TAG: String = this.javaClass.getSimpleName()
    var userPagedList: LiveData<PagedList<User>>
    var liveDataSource: LiveData<PageKeyedDataSource<Query, User>>
    var userDataSourceFactory: UserDataSourceFactory
    private val PAGE_SIZE: Int = 4
    var isLoading: MutableLiveData<Boolean> = MutableLiveData()
    var rootRef = FirebaseFirestore.getInstance()
    var uid = FirebaseAuth.getInstance().currentUser?.uid

    init {
        isLoading.value = true
        userDataSourceFactory = UserDataSourceFactory()
        liveDataSource = userDataSourceFactory.getItemLiveDataSource()
        val config = PagedList.Config.Builder()
            .setPageSize(PAGE_SIZE)
            .setEnablePlaceholders(true)
            .build()

        userPagedList = LivePagedListBuilder(userDataSourceFactory, config).build()
    }

    //fetch new data again by pull to refresh
    fun refreshAdapter() {
        userDataSourceFactory.getItemLiveDataSource().getValue()?.invalidate();
    }

    fun getProgresBar(): LiveData<Boolean> {
        return isLoading
    }

    /*
    *@param
    * user_id : other user's uid
    * uid : my id
     */
    fun setFollowData(user_id: String) {
        val follow = hashMapOf(
            Constants.IS_FOLLOWING to true,
            Constants.USER_ID to user_id
        )
        rootRef.collection(Constants.USER_LIST)
            .document(uid!!)
            .collection(Constants.FOLLOW_LIST)
            .document(user_id)
            .set(follow)
            .addOnCompleteListener { task ->

            }
    }
}
