package com.lwt.qmqiu.fragment

import android.app.Activity
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.lwt.qmqiu.R
import com.lwt.qmqiu.widget.NoticeDialog
import com.trello.rxlifecycle2.LifecycleProvider
import com.trello.rxlifecycle2.LifecycleTransformer
import com.trello.rxlifecycle2.RxLifecycle
import com.trello.rxlifecycle2.android.FragmentEvent
import com.trello.rxlifecycle2.android.RxLifecycleAndroid
import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject



abstract class BaseFragment : Fragment(),LifecycleProvider<FragmentEvent> {


    var mDestroy:Boolean=false
    private var mNoticeDialog: NoticeDialog?=null
    private var mNoticeDialogBuilder: NoticeDialog.Builder?=null
    val lifecycleSubject = BehaviorSubject.create<FragmentEvent>()

    override fun lifecycle(): Observable<FragmentEvent> {

        return lifecycleSubject.hide()
    }

    override fun <T : Any?> bindUntilEvent(event: FragmentEvent): LifecycleTransformer<T> {
        return RxLifecycle.bindUntilEvent(lifecycleSubject, event)
    }

    override fun <T : Any?> bindToLifecycle(): LifecycleTransformer<T> {
        return RxLifecycleAndroid.bindFragment(lifecycleSubject)
    }


    var isFirst : Boolean = false
    var rootView :View? = null
    var isFragmentVisiable :Boolean = false
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        lifecycleSubject.onNext(FragmentEvent.CREATE_VIEW)
        if(rootView==null){
            rootView = inflater?.inflate(getLayoutResources(),container,false)
        }
        return  rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()

    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        lifecycleSubject.onNext(FragmentEvent.ATTACH)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lifecycleSubject.onNext(FragmentEvent.CREATE)
    }

    override fun onStart() {
        super.onStart()
        lifecycleSubject.onNext(FragmentEvent.START)
    }

    override fun onPause() {
        super.onPause()
        lifecycleSubject.onNext(FragmentEvent.PAUSE)
    }

    override fun onStop() {
        super.onStop()
        lifecycleSubject.onNext(FragmentEvent.STOP)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        mDestroy=true
        lifecycleSubject.onNext(FragmentEvent.DESTROY_VIEW)
        if (mNoticeDialog != null) {
            mNoticeDialog?.dismiss()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        lifecycleSubject.onNext(FragmentEvent.DESTROY)
    }

    override fun onDetach() {
        super.onDetach()
        lifecycleSubject.onNext(FragmentEvent.DETACH)
    }

    override fun onResume() {
        super.onResume()
        lifecycleSubject.onNext(FragmentEvent.RESUME)
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        if (isVisibleToUser) {
            isFragmentVisiable = true
        }
        if (rootView == null) {
            return
        }
        //可见，并且没有加载过
        if (!isFirst&&isFragmentVisiable) {
            onFragmentVisiableChange(true);
            return
        }
        //由可见——>不可见 已经加载过
        if (isFragmentVisiable) {
            onFragmentVisiableChange(false)
            isFragmentVisiable = false
        }
    }
    open protected fun onFragmentVisiableChange(b: Boolean) {

    }

    abstract fun getLayoutResources(): Int

    abstract fun initView()
    fun backgroundAlpha(activity: Activity, bgAlpha: Float) {
        val lp = activity.window.attributes
        lp.alpha = bgAlpha
        activity.window.attributes = lp
    }

    protected fun showProgressDialog(message: String = getString(R.string.loading),cancle:Boolean = true,type:Int = 1, listen: NoticeDialog.Builder.BtClickListen? = null) {

        if (mNoticeDialogBuilder == null)
            mNoticeDialogBuilder= NoticeDialog.Builder(activity!!,cancle)

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