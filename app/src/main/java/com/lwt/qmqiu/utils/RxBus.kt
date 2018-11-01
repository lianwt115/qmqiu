package com.lwt.qmqiu.utils

import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import io.reactivex.subjects.PublishSubject

class RxBus {

    private val subject = PublishSubject.create<Any>().toSerialized()
    private val dispoable: Disposable? = null

    companion object {
        @Volatile
        private var mInstance: RxBus? = null

        fun getInstance(): RxBus? {
            if (mInstance == null) {
                synchronized(RxBus::class.java) {
                    if (mInstance == null) {
                        mInstance = RxBus()
                    }
                }
            }
            return mInstance
        }
    }



    /**
     * 发送事件
     * @param object
     */
    fun post(obj: Any) {
        subject.onNext(obj)
    }


    /**
     * @param classType
     * @param <T>
     * @return
    </T> */
    fun <T> toObservale(classType: Class<T>): Observable<T> {
        return subject.ofType(classType)
    }


    /**
     * 订阅
     * @param bean
     * @param consumer
     */
    /*public void subscribe(Class bean, Consumer consumer) {
        dispoable = toObservale(bean).subscribe(consumer);
    }*/

    /**
     * 取消订阅
     */
    /* public void unSubcribe() {
        if (dispoable != null && dispoable.isDisposed()) {
            dispoable.dispose();
        }

    }*/

}