package com.lwt.qmqiu.utils


import android.content.Context
import android.content.SharedPreferences
import com.lwt.qmqiu.App



class SPHelper {

    private  var sharedPreferences: SharedPreferences = App.instanceApp().getSharedPreferences("qmqiu",
            Context.MODE_PRIVATE)
    /*
     * 保存手机里面的名字
     */
    private  var editor: SharedPreferences.Editor

    init {

        editor = sharedPreferences.edit()
    }

    companion object {
        @Volatile
        private var mInstance: SPHelper? = null

        fun getInstance(): SPHelper {
            if (mInstance == null) {
                synchronized(SPHelper::class.java) {
                    if (mInstance == null) {
                        mInstance = SPHelper()
                    }
                }
            }
            return mInstance!!
        }
    }

    /**
     * 存储
     */
    fun put(key: String, `object`: Any) {
        if (`object` is String) {
            editor.putString(key, `object`)
        } else if (`object` is Int) {
            editor.putInt(key, `object`)
        } else if (`object` is Boolean) {
            editor.putBoolean(key, `object`)
        } else if (`object` is Float) {
            editor.putFloat(key, `object`)
        } else if (`object` is Long) {
            editor.putLong(key, `object`)
        } else {
            editor.putString(key, `object`.toString())
        }
        editor.commit()
    }

    /**
     * 获取保存的数据
     */
    fun get(key: String, defaultObject: Any): Any? {
        return if (defaultObject is String) {
            sharedPreferences.getString(key, defaultObject)
        } else if (defaultObject is Int) {
            sharedPreferences.getInt(key, defaultObject)
        } else if (defaultObject is Boolean) {
            sharedPreferences.getBoolean(key, defaultObject)
        } else if (defaultObject is Float) {
            sharedPreferences.getFloat(key, defaultObject)
        } else if (defaultObject is Long) {
            sharedPreferences.getLong(key, defaultObject)
        } else {
            sharedPreferences.getString(key, null)
        }
    }

    /**
     * 移除某个key值已经对应的值
     */
    fun remove(key: String) {
        editor.remove(key)
        editor.commit()
    }

    /**
     * 清除所有数据
     */
    fun clear() {
        editor.clear()
        editor.commit()
    }

    /**
     * 查询某个key是否存在
     */
    fun contain(key: String): Boolean? {
        return sharedPreferences.contains(key)
    }

    /**
     * 返回所有的键值对
     */
    fun getAll(): Map<String, *> {
        return sharedPreferences.all
    }



}