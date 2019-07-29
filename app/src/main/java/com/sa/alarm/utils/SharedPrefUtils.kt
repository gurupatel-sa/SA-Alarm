package com.sa.alarm.utils

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.content.Context.MODE_PRIVATE

class SharedPrefUtils {
    companion object{
        val PREF_APP = "pref_app"
        private lateinit var pref :SharedPreferences

        fun getInstance(context: Context, key: String): SharedPreferences {
            pref = context.getSharedPreferences(PREF_APP ,0)
            return pref
        }

        fun putInt(key: String, value:Int){
            pref.edit().putInt(key,value)?.apply()
        }

        fun getInt(key:String) : Int{
            return pref.getInt(key ,-1)
        }

        fun putString(key: String, value:String){
            pref.edit().putString(key,value)?.apply()
        }

        fun getString(key:String) : String?{
            return pref.getString(key ,null)
        }

        fun putBoolean(key: String, value:Boolean){
            pref.edit().putBoolean(key,value)?.apply()
        }

        fun getBoolean(key:String) : Boolean{
            return pref.getBoolean(key ,false)
        }

        fun remove(key :String){
            pref.edit().remove(key).apply()
        }
    }
}