package com.lwt.qmqiu.activity

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.transition.Explode
import android.transition.Transition
import android.transition.TransitionInflater
import android.view.View
import android.view.ViewAnimationUtils
import android.view.animation.AccelerateInterpolator
import com.lwt.qmqiu.R
import com.lwt.qmqiu.R.id.cv_add
import com.lwt.qmqiu.R.id.fab
import com.lwt.qmqiu.bean.BaseUser
import com.lwt.qmqiu.mvp.contract.UserLoginContract
import com.lwt.qmqiu.mvp.present.UserLoginPresent
import com.lwt.qmqiu.utils.UiUtils
import com.orhanobut.logger.Logger
import kotlinx.android.synthetic.main.activity_register.*
import java.util.regex.Matcher
import java.util.regex.Pattern

class RegisterActivity:BaseActivity(), View.OnClickListener, UserLoginContract.View {



    private lateinit var present:UserLoginPresent

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            ShowEnterAnimation()
        }

        fab.setOnClickListener(this)
        bt_go.setOnClickListener(this)


        /*val explode = Explode()
        explode.duration = 500
        window.exitTransition = explode
        window.enterTransition = explode*/

        et_username.addTextChangedListener(object :TextWatcher{

            override fun afterTextChanged(s: Editable?) {

            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

                var content =  et_username.text.toString()

                var regEx = "[^a-zA-Z0-9]"  //只能输入字母或数字

                var p = Pattern.compile(regEx)

                var m = p.matcher(content)

                var str = m.replaceAll("").trim()    //删掉不是字母或数字的字符
                if(content != str){
                    UiUtils.showToast("请输入字母或数字")
                    et_username.setText(str)  //设置EditText的字符
                    et_username.setSelection(str.length) //因为删除了字符，要重写设置新的光标所在位置
                }


            }

        }

        )


        present = UserLoginPresent(this,this)

    }

    override fun onClick(v: View?) {

        when (v?.id) {

            R.id.fab -> {

               animateRevealClose()
            }

            R.id.bt_go -> {

                //判断非空

                if (et_username.text.toString().length < 6 || et_username.text.toString().length >12) {

                    UiUtils.showToast("请输入6-12位用户名")
                    return

                }else{

                    var regEx = "[a-zA-Z]"  //只能输入字母或数字

                    var p = Pattern.compile(regEx)

                    var m = p.matcher(et_username.text.toString().substring(0,1))

                    if (!m.matches()) {
                        UiUtils.showToast("用户名请以字母开头")
                        return
                    }

                }

                if (et_password.text.toString().length <0 || et_repeatpassword.text.toString().length <0) {

                    UiUtils.showToast("密码不能为空")

                    return

                }

                if (et_password.text.toString().length != et_repeatpassword.text.toString().length ) {

                    UiUtils.showToast("两次密码不一致")

                    return

                }

                present.userRegist(et_username.text.toString(),et_password.text.toString(),bindToLifecycle())

                bt_go.startAnimation()


            }


        }

    }

    override fun registSuccess(baseUser: BaseUser) {

        bt_go.doneLoadingAnimation(resources.getColor(R.color.white), BitmapFactory.decodeResource(resources,R.mipmap.ic_done))


        Logger.e("注册成功:$baseUser")

        UiUtils.showToast("注册成功")
    }

    override fun err(code: Int, errMessage: String?) {

        bt_go.revertAnimation()

        when (code) {

            202 -> {
                Logger.e("用户名已存在,请重新注册")
            }

            else -> {

            }
        }

        UiUtils.showToast(errMessage!!)

    }



    private fun ShowEnterAnimation() {
        val transition = TransitionInflater.from(this).inflateTransition(R.transition.fabtransition)
        window.sharedElementEnterTransition = transition

        transition.addListener(object : Transition.TransitionListener {
            override fun onTransitionStart(transition: Transition) {
                cv_add.visibility = View.GONE
            }

            override fun onTransitionEnd(transition: Transition) {
                transition.removeListener(this)
                animateRevealShow()
            }

            override fun onTransitionCancel(transition: Transition) {

            }

            override fun onTransitionPause(transition: Transition) {

            }

            override fun onTransitionResume(transition: Transition) {

            }


        })
    }

    fun animateRevealShow() {
        val mAnimator = ViewAnimationUtils.createCircularReveal(cv_add, cv_add.width / 2, 0, (fab.getWidth() / 2).toFloat(), cv_add.getHeight().toFloat())
        mAnimator.duration = 500
        mAnimator.interpolator = AccelerateInterpolator()
        mAnimator.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                super.onAnimationEnd(animation)
            }

            override fun onAnimationStart(animation: Animator) {
                cv_add.visibility = View.VISIBLE
                super.onAnimationStart(animation)
            }
        })
        mAnimator.start()
    }

    fun animateRevealClose() {
        val mAnimator = ViewAnimationUtils.createCircularReveal(cv_add, cv_add.width / 2, 0, cv_add.getHeight().toFloat(), (fab.getWidth() / 2).toFloat())
        mAnimator.duration = 500
        mAnimator.interpolator = AccelerateInterpolator()
        mAnimator.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                cv_add.visibility = View.INVISIBLE
                super.onAnimationEnd(animation)
                fab.setImageResource(R.drawable.plus)
                super@RegisterActivity.onBackPressed()
            }

            override fun onAnimationStart(animation: Animator) {
                super.onAnimationStart(animation)
            }
        })
        mAnimator.start()
    }

    override fun onBackPressed() {
        animateRevealClose()
    }


    override fun onDestroy() {
        super.onDestroy()
        bt_go.dispose()
    }



}