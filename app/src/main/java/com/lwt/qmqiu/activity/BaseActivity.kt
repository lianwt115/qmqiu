package com.lwt.qmqiu.activity


import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.Html
import android.text.Spanned
import com.lwt.qmqiu.App
import com.lwt.qmqiu.R
import com.lwt.qmqiu.bean.QMMessage
import com.lwt.qmqiu.bean.WSErr
import com.lwt.qmqiu.network.QMWebsocket
import com.lwt.qmqiu.utils.RxBus
import com.lwt.qmqiu.utils.applySchedulers
import com.lwt.qmqiu.widget.GiftDialog
import com.lwt.qmqiu.widget.NoticeDialog
import com.orhanobut.logger.Logger
import com.trello.rxlifecycle2.LifecycleProvider
import com.trello.rxlifecycle2.LifecycleTransformer
import com.trello.rxlifecycle2.RxLifecycle
import com.trello.rxlifecycle2.android.ActivityEvent
import com.trello.rxlifecycle2.android.RxLifecycleAndroid
import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import io.reactivex.subjects.BehaviorSubject



open class BaseActivity : AppCompatActivity(),LifecycleProvider<ActivityEvent>, QMWebsocket.QMMessageListen {


    override fun qmMessage(it: QMMessage) {


        runOnUiThread {

            Logger.e("通知RX:$it")
            when (it.type) {
                //礼物
                2 -> {
                    Logger.e("通知RX:$it")
                    val infoList = it.message.split("*") //数量-单位-名称-动画名称


                    val info = Html.fromHtml("${it.from}  赠送: <font color='#FF4081'>"+infoList[0]+"</font>\t"+"${infoList[1]}"+"<font color='#FF4081'>\t${infoList[2]}</font>" +"给<font color='#FF4081'> ${it.to}</font>")


                    showGiftDialog(info,infoList[3])

                }

            }


        }


    }

    override fun errorWS(type: Int, message: String) {

                runOnUiThread {

                    when (type) {

                        0,1 -> {
                            showProgressDialog(message,true)
                        }

                        2 -> {

                            dismissProgressDialog()
                        }
                    }


                }

        }


    protected val lifecycleSubject = BehaviorSubject.create<ActivityEvent>()

    protected var mDestroy:Boolean=false
    private var mNoticeDialog: NoticeDialog?=null
    private var mGiftDialog: GiftDialog?=null
    private var mNoticeDialogBuilder: NoticeDialog.Builder?=null
    private var mGiftDialogBuilder: GiftDialog.Builder?=null

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

        App.instanceApp().setListen(this)
    }


    override fun onPause() {
        super.onPause()
        lifecycleSubject.onNext(ActivityEvent.PAUSE)
    }
    override fun onStop() {
        super.onStop()
        lifecycleSubject.onNext(ActivityEvent.STOP)

        App.instanceApp().setListen(null)
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

    protected fun showGiftDialog(info: Spanned, path:String) {

        if (mGiftDialogBuilder == null)
            mGiftDialogBuilder= GiftDialog.Builder(this)

        if (mGiftDialog == null)

            mGiftDialog=mGiftDialogBuilder!!.create()

        mGiftDialogBuilder?.start(info,path)

        mGiftDialog?.show()
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