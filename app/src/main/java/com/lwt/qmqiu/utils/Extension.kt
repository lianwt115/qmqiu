package com.lwt.qmqiu.utils

import android.app.Activity
import android.content.Intent
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers


inline fun <reified T : Activity> Activity.newIntent(isShowRed:Boolean=false) {
    val intent = Intent(this, T::class.java)
    intent.putExtra("isshowred",isShowRed)
    startActivity(intent)
}


fun <T> Observable<T>.applySchedulers(): Observable<T> {
    return subscribeOn(Schedulers.io()).unsubscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
}



