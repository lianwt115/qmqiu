package com.lwt.qmqiu.dao



interface BaseDaoUtils<T> {

    fun insertOrReplace(t:T)

    fun getAll(normal:Boolean = true): MutableList<T>?
    fun findByIdOne(id:String): T?
    fun findById(id:String): MutableList<T>?
    fun deleteById(id:String)
    fun getCount():Long


}