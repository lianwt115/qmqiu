package com.lwt.qmqiu.activity


import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.text.Html
import android.text.Spanned
import android.view.KeyEvent
import com.lwt.qmqiu.App
import com.lwt.qmqiu.R
import com.lwt.qmqiu.bean.QMMessage
import com.lwt.qmqiu.network.QMWebsocket
import com.lwt.qmqiu.widget.GiftDialog
import com.lwt.qmqiu.widget.NoticeDialog
import com.orhanobut.logger.Logger
import com.trello.rxlifecycle2.LifecycleProvider
import com.trello.rxlifecycle2.LifecycleTransformer
import com.trello.rxlifecycle2.RxLifecycle
import com.trello.rxlifecycle2.android.ActivityEvent
import com.trello.rxlifecycle2.android.RxLifecycleAndroid
import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject
import android.view.WindowManager
import android.os.Build
import com.lwt.qmqiu.utils.SPHelper


open class BaseActivity : AppCompatActivity(),LifecycleProvider<ActivityEvent>, QMWebsocket.QMMessageListen {


    protected val giftUnitList = listOf("个","朵","台","顶")
    protected val giftNameList = listOf("天使宝贝","挚爱玫瑰","激情跑车","女王皇冠")
    protected val giftPathList = listOf("angel.svga","rose.svga","posche.svga","kingset.svga")
    private  var mLocalUserName:String = SPHelper.getInstance().get("loginName","") as String


    override fun qmMessage(qmMessage: QMMessage) {


        runOnUiThread {

            Logger.e("通知RX:$qmMessage")
            when (qmMessage.type) {
                //礼物
                2 -> {

                    //这个是全局通知收到的  只有可能是接受者
                    val infoList = qmMessage.message.split("*") //数量-单位-名称-动画名称


                    val info = Html.fromHtml("${qmMessage.from}  赠送: <font color='#FF4081'>"+infoList[0]+"</font>\t"+"${infoList[1]}"+"<font color='#FF4081'>\t ${infoList[2]}</font>" +"\n给<font color='#FF4081'> ${if(mLocalUserName == qmMessage.to)"${qmMessage.to}(我)" else qmMessage.to}</font>")


                    showGiftDialog(info,infoList[3])

                }

                //视频呼叫
                6 ->{

                    //TODO 如果当前用户忙
                    if (this@BaseActivity is FaceVideoActivity){


                        showProgressDialog("已拒绝${qmMessage.from}的视频通话")

                        return@runOnUiThread
                    }


                    var data = App.instanceApp().getShowMessage(qmMessage.message)

                    Logger.e("收到视频邀请:data")

                    val intent = Intent(this, FaceVideoActivity::class.java)

                    intent.putExtra("videoChannel",data)
                    intent.putExtra("active",true)
                    intent.putExtra("message",qmMessage)

                    startActivity(intent)

                }
                //视频退出
                7 ->{

                   if (this@BaseActivity is FaceVideoActivity){

                       showProgressDialog("用户已离开")
                       this@BaseActivity.exit()

                   }


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
        App.instanceApp().setCurrentActivity(this)
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


    protected fun changeStatusColor(color: Int){
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
                window.statusBarColor = resources.getColor(color)

            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        App.instanceApp().setCurrentActivity(null)
        mDestroy=true

        lifecycleSubject.onNext(ActivityEvent.DESTROY)
        if (mNoticeDialog != null) {
            mNoticeDialog?.dismiss()
        }

    }


    protected fun showProgressDialog(message: String = getString(R.string.loading),cancle:Boolean = true,type:Int = 1, listen: NoticeDialog.Builder.BtClickListen?=null) {

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

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK) {

            super.onBackPressed()

        }
        return super.onKeyDown(keyCode, event)
    }

}