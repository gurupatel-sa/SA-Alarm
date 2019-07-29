package com.sa.alarm.users

import androidx.paging.PageKeyedDataSource
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot
import com.sa.alarm.common.Constants
import com.sa.alarm.register.model.User
import com.sa.alarm.utils.LogUtils

class UserDataSource : PageKeyedDataSource<Query, User>() {

    private var rootRef : FirebaseFirestore = FirebaseFirestore.getInstance()
    private var nextQuery:Query
    private val TAG : String = this.javaClass.getSimpleName()
    private val PAGE_SIZE :Long = 4
    private val ORDER_BY_FIELD :String ="timestamp"
    private val ORDER :Query.Direction

    init {
        ORDER =Query.Direction.DESCENDING

        nextQuery= rootRef.collection(Constants.USER_LIST)
            .orderBy(ORDER_BY_FIELD ,ORDER)
            .limit(PAGE_SIZE)
    }

    override fun loadInitial(params: LoadInitialParams<Query>, callback: LoadInitialCallback<Query, User>) {
        LogUtils.d(TAG,"loadInitial :")
        nextQuery
            .get()
            .addOnCompleteListener { task: Task<QuerySnapshot> ->
                if(task.isSuccessful && !task.result?.isEmpty!!){
                    val docSnapshot = task.result?.documents
                    if(docSnapshot?.size ==- 1){
                    }
                    val lastVisible = docSnapshot!![docSnapshot.size-1]

                    LogUtils.d(TAG,"retreiveUserList : ${task.result?.size()}" )
                    var userList = ArrayList<User>()
                    for (document in docSnapshot){
                        val user = document.toObject(User::class.java)
                        userList.add(user!!)
                        LogUtils.d(TAG,"retreiveUserList :"+user?.displayName)
                    }

                    nextQuery = rootRef.collection(Constants.USER_LIST)
                        .orderBy(ORDER_BY_FIELD , ORDER)
                        .startAfter(lastVisible)
                        .limit(PAGE_SIZE)

                    callback.onResult(userList ,null , nextQuery)
                }
            }
    }

    override fun loadAfter(params: LoadParams<Query>, callback: LoadCallback<Query, User>) {
        LogUtils.d(TAG,"loadAfter :")
        nextQuery
            .get()
            .addOnCompleteListener { task: Task<QuerySnapshot> ->
                if(task.isSuccessful && !task.result?.isEmpty!!){
                    val docSnapshot = task.result?.documents
                    if(docSnapshot?.size ==- 1){
                    }
                    val lastVisible = docSnapshot!![docSnapshot.size-1]

                    LogUtils.d(TAG,"retreiveUserList : ${task.result?.size()}" )
                    var userList = ArrayList<User>()
                    for (document in docSnapshot){
                        val user = document.toObject(User::class.java)
                        userList.add(user!!)
                        LogUtils.d(TAG,"retreiveUserList :"+user?.displayName)
                    }

                    nextQuery = rootRef.collection(Constants.USER_LIST)
                        .orderBy(ORDER_BY_FIELD , ORDER)
                        .startAfter(lastVisible)
                        .limit(PAGE_SIZE)

                    callback.onResult(userList ,nextQuery)
                }
            }
    }

    override fun loadBefore(params: LoadParams<Query>, callback: LoadCallback<Query, User>) {
        LogUtils.d(TAG,"loadBefore :")
    }
}