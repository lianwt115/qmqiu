package com.lwt.qmqiu.activity

import android.Manifest
import android.app.ActivityOptions
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import com.baidu.location.BDLocation
import com.lwt.qmqiu.R
import com.lwt.qmqiu.utils.applySchedulers
import com.orhanobut.logger.Logger
import com.tbruyelle.rxpermissions2.RxPermissions
import io.reactivex.Observable
import kotlinx.android.synthetic.main.activity_main.*
import java.util.concurrent.TimeUnit
import android.text.TextUtils
import android.view.KeyEvent
import com.lwt.qmqiu.fragment.NoteFragment
import com.lwt.qmqiu.App
import com.lwt.qmqiu.BuildConfig
import com.lwt.qmqiu.R.mipmap.location
import com.lwt.qmqiu.bean.BaseUser
import com.lwt.qmqiu.fragment.FindFragment
import com.lwt.qmqiu.fragment.MineFragment
import com.lwt.qmqiu.map.MapLocationUtils
import com.lwt.qmqiu.mvp.contract.UserLoginContract
import com.lwt.qmqiu.mvp.present.UserLoginPresent
import com.lwt.qmqiu.utils.SPHelper
import com.lwt.qmqiu.utils.UiUtils
import com.tencent.bugly.beta.Beta



class MainActivity : BaseActivity(), View.OnClickListener, UserLoginContract.View {


    private var mExitTime: Long = 0
    private var findFragment: FindFragment? = null
    private var noteFragment: NoteFragment? = null
    private var mineFragment: MineFragment? = null
    private lateinit var present:UserLoginPresent
    private  var loginClick = false
    private  var autoLoginCount = 0
    private  var autoLoginCountMax = 3

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        checkAndRequirePermissions()

        setRadioButton()

        initFragment(savedInstanceState)

        //需要初始化
        Beta.init(applicationContext, BuildConfig.DEBUG)

        //startService(Intent(this, LocalService::class.java))
        //startService(Intent(this, RomoteService::class.java))

        present = UserLoginPresent(this,this)


        //登录
        gotoLogin()

    }

    private fun gotoLogin() {
        if (!App.instanceApp().isLogin() && autoLoginCount <= autoLoginCountMax)

            autoLogin(true)
        else
            bottomShow()
    }

    private fun setRadioButton() {
        rb_find.isChecked = true
        rb_find.setTextColor(resources.getColor(R.color.colorAccent))
        find_parente.setOnClickListener(this)
        note_parente.setOnClickListener(this)
        mine_parent.setOnClickListener(this)
    }

    private fun initFragment(savedInstanceState: Bundle?) {
        if (savedInstanceState != null) {
            //异常情况
            val mFragments: List<androidx.fragment.app.Fragment> = supportFragmentManager.fragments
            for (item in mFragments) {
                if (item is FindFragment) {
                    findFragment = item
                }else if (item is NoteFragment){
                    noteFragment=item
                }else if (item is MineFragment){
                    mineFragment=item
                }

            }
        } else {
            findFragment = FindFragment()
            noteFragment = NoteFragment()
            mineFragment = MineFragment()

            val fragmentTrans = supportFragmentManager.beginTransaction()

            fragmentTrans.add(R.id.fl_content, findFragment!!)
            fragmentTrans.add(R.id.fl_content, noteFragment!!)
            fragmentTrans.add(R.id.fl_content, mineFragment!!)

            fragmentTrans.commit()
        }


        supportFragmentManager.beginTransaction().show(findFragment!!)
                .hide(noteFragment!!)
                .hide(mineFragment!!)
                .commit()

    }



    override fun onClick(v: View?) {

        clearState()

        when (v?.id) {
            R.id.find_parente -> {

                rb_find.isChecked = true
                rb_find.setTextColor(resources.getColor(R.color.colorAccent))

                supportFragmentManager.beginTransaction().show(findFragment!!)
                        .hide(noteFragment!!)
                        .hide(mineFragment!!)
                        .commit()

            }

            R.id.note_parente -> {


                rb_note.isChecked = true
                rb_note.setTextColor(resources.getColor(R.color.colorAccent))

                supportFragmentManager.beginTransaction().show(noteFragment!!)
                        .hide(findFragment!!)
                        .hide(mineFragment!!)
                        .commit()
            }

            R.id.mine_parent -> {


                rb_mine.isChecked = true
                rb_mine.setTextColor(resources.getColor(R.color.colorAccent))
                supportFragmentManager.beginTransaction().show(mineFragment!!)
                        .hide(findFragment!!)
                        .hide(noteFragment!!)
                        .commit()
            }

            R.id.fab  -> {
                loginClick = true
                window.exitTransition = null
                window.enterTransition = null

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    val options = ActivityOptions.makeSceneTransitionAnimation(this, fab, fab.transitionName)
                    startActivity(Intent(this, RegisterActivity::class.java), options.toBundle())
                } else {
                    startActivity(Intent(this, RegisterActivity::class.java))
                }
            }
        }


    }

    private fun clearState() {
        rb_mine.isChecked=false
        rb_note.isChecked=false
        rb_find.isChecked=false
        rb_note.setTextColor(resources.getColor(R.color.main_bottom_text))
        rb_mine.setTextColor(resources.getColor(R.color.main_bottom_text))
        rb_find.setTextColor(resources.getColor(R.color.main_bottom_text))
    }



    //权限检查
    fun checkAndRequirePermissions(){

        //判断系统版本是否是6.0以上

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){

            var per: RxPermissions?= RxPermissions(this)
            per?.request( Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.CAMERA,Manifest.permission.RECORD_AUDIO,Manifest.permission.READ_EXTERNAL_STORAGE)
                    ?.subscribe {
                        if (it) {

                           // Logger.e("权限全部同意")

                        } else {
                            //至少一个没有同意
                            showProgressDialog("权限被拒绝,稍后将退出")

                            Observable.timer(3, TimeUnit.SECONDS).applySchedulers().subscribe({

                                finish()

                            }, {

                                Logger.e(it.message)

                            })
                        }
                    }
        }else{

            Logger.e("系统版本低于6.0,无需动态申请权限")

        }

    }

    override fun onDestroy() {

        super.onDestroy()

    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK) {

            if (System.currentTimeMillis().minus(mExitTime) <= 3000 ) {

                App.instanceApp().closeWs()
                autoLogin(false)

                finish()
            } else {
                mExitTime = System.currentTimeMillis()

                UiUtils.showToast(getString(R.string.exit_notice))
            }
            return true
        }
        return super.onKeyDown(keyCode, event)
    }


    override fun onPause() {
        super.onPause()

    }

    override fun onResume() {
        super.onResume()

        bottomShow()

        if (loginClick){
            loginClick = false
            rb_find.isChecked = true
            rb_find.setTextColor(resources.getColor(R.color.colorAccent))
        }

        MapLocationUtils.getInstance().findMe()

    }

    private fun bottomShow() {


        //本地有无记录
        if (checkUserInfo()) {

            fab.hide()
            rg_root.visibility = View.VISIBLE

        }

        if (autoLoginCount > autoLoginCountMax) {

            fab.show()

            rg_root.visibility = View.INVISIBLE

        }


        if (App.instanceApp().isLogin()){

            fab.hide()

            rg_root.visibility = View.VISIBLE

        }

        fab.setOnClickListener(this)

    }

    override fun onStop() {
        super.onStop()

        MapLocationUtils.getInstance().exit()

    }


    override fun successRegistOrLogin(baseUser: BaseUser, regist: Boolean) {

        MapLocationUtils.getInstance().setListen(null)
        //Logger.e("自动登录成功")
        App.instanceApp().setLocalUser(baseUser)
    }

    override fun err(code: Int, errMessage: String?) {

        //登录
        gotoLogin()
        Logger.e("自动登录失败:code:$code ** errMessage:$errMessage")

    }

    private fun autoLogin(login:Boolean) {
        //是否已经登录

        if (!checkUserInfo())
            return

        val name = SPHelper.getInstance().get("loginName","") as String
        val password = SPHelper.getInstance().get("loginPassword","") as String


        when (login) {
            //登录
            true -> {

                    autoLoginCount++

                    if (App.instanceApp().getLocalUser() == null)

                        present.userLogin(name,password,true,"",0.00,0.00,bindToLifecycle())


            }
            //登出
            false -> {

                val location = App.instanceApp().getBDLocation()!!

                if (App.instanceApp().getLocalUser() != null)

                    present.userLoginOut(name,password,true,location.addrStr,location.latitude,location.longitude,bindToLifecycle())

            }
        }


    }

    private fun checkUserInfo():Boolean {

        val name = SPHelper.getInstance().get("loginName","") as String
        val password = SPHelper.getInstance().get("loginPassword","") as String

        return !TextUtils.isEmpty(name) && !TextUtils.isEmpty(password)

    }


}
