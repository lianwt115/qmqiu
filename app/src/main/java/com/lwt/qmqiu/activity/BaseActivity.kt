package com.lwt.qmqiu.activity

import android.content.DialogInterface
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.lwt.qmqiu.R
import com.lwt.qmqiu.widget.NoticeDialog
import com.trello.rxlifecycle2.LifecycleProvider
import com.trello.rxlifecycle2.LifecycleTransformer
import com.trello.rxlifecycle2.RxLifecycle
import com.trello.rxlifecycle2.android.ActivityEvent
import com.trello.rxlifecycle2.android.RxLifecycleAndroid
import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject



open class BaseActivity : AppCompatActivity(),LifecycleProvider<ActivityEvent>{

    val lifecycleSubject = BehaviorSubject.create<ActivityEvent>()

     var mDestroy:Boolean=false
    private var mNoticeDialog: NoticeDialog?=null
    private var mNoticeDialogBuilder: NoticeDialog.Builder?=null
    protected val RC_SIGN_IN = 9001
    override fun <T : Any?> bindUntilEvent(event: ActivityEvent): LifecycleTransformer<T> {

        return RxLifecycle.bindUntilEvent(lifecycleSubject, event)

    }

    override fun lifecycle(): Observable<ActivityEvent> {
        return lifecycleSubject.hide()
    }

    override fun <T : Any?> bindToLifecycle(): LifecycleTransformer<T> {
        return RxLifecycleAndroid.bindActivity(lifecycleSubject)
    }

    init {

    }

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        lifecycleSubject.onNext(ActivityEvent.CREATE)

    }

    override fun onStart() {
        super.onStart()
        lifecycleSubject.onNext(ActivityEvent.START)
    }

    override fun onResume() {
        super.onResume()
        lifecycleSubject.onNext(ActivityEvent.RESUME)

    }

    override fun onPause() {
        super.onPause()
        lifecycleSubject.onNext(ActivityEvent.PAUSE)
    }
    override fun onStop() {
        super.onStop()
        lifecycleSubject.onNext(ActivityEvent.STOP)
    }

    override fun onDestroy() {
        super.onDestroy()
        mDestroy=true
        lifecycleSubject.onNext(ActivityEvent.DESTROY)
        if (mNoticeDialog != null) {
            mNoticeDialog?.dismiss()
        }

    }


    protected fun showProgressDialog(message: String = getString(R.string.loading),cancle:Boolean = false,type:Int = 1, listen: NoticeDialog.Builder.BtClickListen?=null) {

        if (mNoticeDialogBuilder == null)
            mNoticeDialogBuilder= NoticeDialog.Builder(this,cancle)

        if (mNoticeDialog == null)

            mNoticeDialog=mNoticeDialogBuilder!!.create(message)

        else
            mNoticeDialogBuilder!!.initView( message,cancle)



        mNoticeDialogBuilder!!.setListen(listen,type)

        mNoticeDialog!!.show()
    }

    protected fun showProgressDialogSuccess(boolean: Boolean){

        if (!mDestroy && mNoticeDialog != null && mNoticeDialogBuilder != null) {
            mNoticeDialogBuilder!!.btFinish(boolean)
        }
    }


    protected fun dismissProgressDialog() {
        if (!mDestroy && mNoticeDialog != null) {
            mNoticeDialog!!.dismiss()
        }
    }

}