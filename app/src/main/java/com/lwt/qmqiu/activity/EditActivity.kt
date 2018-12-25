package com.lwt.qmqiu.activity




import android.graphics.BitmapFactory
import android.graphics.Rect
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.lwt.qmqiu.App
import com.lwt.qmqiu.R
import com.lwt.qmqiu.adapter.ImgSelectListAdapter
import com.lwt.qmqiu.bean.BaseUser
import com.lwt.qmqiu.mvp.contract.EditContract
import com.lwt.qmqiu.mvp.present.EditPresent
import com.lwt.qmqiu.network.ApiService
import com.lwt.qmqiu.utils.UiUtils
import com.lwt.qmqiu.utils.applySchedulers
import com.lwt.qmqiu.widget.BarView
import com.orhanobut.logger.Logger
import io.reactivex.Observable
import kotlinx.android.synthetic.main.activity_edit.*

import java.util.concurrent.TimeUnit


class EditActivity : BaseActivity(),BarView.BarOnClickListener, ImgSelectListAdapter.ImgSelectClick, View.OnClickListener, EditContract.View {

    override fun setUser(user: BaseUser) {
        //更新用户信息
       App.instanceApp().updataLocalUser(user,false,false,true)

        updata_user.doneLoadingAnimation(resources.getColor(R.color.bt_bg9), BitmapFactory.decodeResource(resources,R.mipmap.ic_done))

        Observable.timer(500, TimeUnit.MILLISECONDS).applySchedulers().subscribe({

            updata_user.revertAnimation()
            //mGiftInfoList[mSendGiftIndex].savgPath
            //mSendGiftIndex
            updataShow(user)

        },{
            Logger.e(it.localizedMessage)
        })
    }

    override fun err(code: Int, errMessage: String?, type: Int) {

        updata_user!!.doneLoadingAnimation(resources.getColor(R.color.bt_bg9), BitmapFactory.decodeResource(resources,R.mipmap.error))

        Observable.timer(1,TimeUnit.SECONDS).applySchedulers().subscribe({

            updata_user.revertAnimation()

        },{
            Logger.e(it.localizedMessage)
        })

       UiUtils.showToast(errMessage?:getString(R.string.sys_err))
    }

    //服务器图片目录
    private var imgArray = listOf<String>(
            "qmqiuimg/ab.jpg","qmqiuimg/aj.jpg","qmqiuimg/bghqg.jpg","qmqiuimg/bmyw.jpg",
            "qmqiuimg/cjl.jpg","qmqiuimg/ck.jpg","qmqiuimg/clxzlmc.jpg","qmqiuimg/dbf.jpg",
            "qmqiuimg/dcq.jpg","qmqiuimg/deb.jpg","qmqiuimg/dfbb.jpg","qmqiuimg/dq.jpg",
            "qmqiuimg/dxhys.jpg","qmqiuimg/dy.jpg","qmqiuimg/dyq.jpg","qmqiuimg/emzzr.jpg",
            "qmqiuimg/gj.jpg","qmqiuimg/gsz.jpg","qmqiuimg/gx.jpg","qmqiuimg/hd.jpg",
            "qmqiuimg/hr.jpg","qmqiuimg/hsyls.jpg","qmqiuimg/hz.jpg","qmqiuimg/jl.jpg",
            "qmqiuimg/jlfw.jpg","qmqiuimg/jmsw.jpg","qmqiuimg/jmz.jpg","qmqiuimg/lfh.jpg",
            "qmqiuimg/lhc.jpg","qmqiuimg/lnhys.jpg","qmqiuimg/lqs.jpg","qmqiuimg/lwt.jpg",
            "qmqiuimg/lzw.jpg","qmqiuimg/mcf.jpg","qmqiuimg/mg.jpg","qmqiuimg/mlc.jpg",
            "qmqiuimg/mrb.jpg","qmqiuimg/mrf.jpg","qmqiuimg/mwq.jpg","qmqiuimg/mwq1.jpg",
            "qmqiuimg/nddzx.jpg","qmqiuimg/oyk.jpg","qmqiuimg/qf.jpg","qmqiuimg/qq.jpg",
            "qmqiuimg/qqr.jpg","qmqiuimg/rwx.jpg","qmqiuimg/ryy.jpg","qmqiuimg/se.jpg",
            "qmqiuimg/sg.jpg","qmqiuimg/shlxz.jpg","qmqiuimg/sqs.jpg","qmqiuimg/tbg.jpg",
            "qmqiuimg/tstl.jpg","qmqiuimg/wxb.jpg","qmqiuimg/wyy.jpg","qmqiuimg/wyz.jpg",
            "qmqiuimg/xdoyf.jpg","qmqiuimg/xln.jpg","qmqiuimg/xln1.jpg","qmqiuimg/xys.jpg",
            "qmqiuimg/xz.jpg","qmqiuimg/yd.jpg","qmqiuimg/yen.jpg","qmqiuimg/yg.jpg",
            "qmqiuimg/yg1.jpg","qmqiuimg/yk.jpg","qmqiuimg/yl.jpg","qmqiuimg/yls.jpg",
            "qmqiuimg/yzh.jpg","qmqiuimg/zl.jpg","qmqiuimg/zm.jpg","qmqiuimg/zs.jpg",
            "qmqiuimg/zstwcy.jpg","qmqiuimg/zzr.jpg"
    )

    private lateinit var mAdapter:ImgSelectListAdapter
    private lateinit var mImgpath:String
    private  var mAge:Int=0
    private lateinit var mShowName:String
    private  var mMale:Boolean=false
    private lateinit var present: EditPresent


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_edit)

        edit_barview.setBarOnClickListener(this)
        edit_barview.changeTitle(getString(R.string.edit_userinfo))

        var userLocal = App.instanceApp().getLocalUser()

        if (userLocal!=null){

            updataShow(userLocal)

            cb_male.setOnCheckedChangeListener { _, isChecked ->

                cb_female.isChecked = !isChecked
            }

            cb_female.setOnCheckedChangeListener { _, isChecked ->

                cb_male.isChecked = !isChecked
            }

        }

        initRecycleView()

        updata_user.text = getString(R.string.edit)
        updata_user.background = getDrawable(R.drawable.bg_20dp_13)
        updata_user.setFinalCornerRadius(20F)
        updata_user.setOnClickListener(this)

        present = EditPresent(this,this)
    }


    private fun updataShow(userLocal: BaseUser) {
        //图像
        Glide.with(this).load(ApiService.BASE_URL_Api.plus(userLocal.imgPath)).into(user_img)
        et_showName.setText("")
        et_showName.hint = userLocal.showName
        et_age.setText("")
        et_age.hint = userLocal.age.toString()

        cb_male.isChecked = userLocal.male

        cb_female.isChecked = !userLocal.male

        mImgpath = userLocal.imgPath!!

        mAge = userLocal.age

        mMale = userLocal.male

        mShowName = userLocal.showName
    }

    private fun initRecycleView() {

        val linearLayoutManager = object : androidx.recyclerview.widget.LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL,false){

            override fun canScrollHorizontally(): Boolean {
                return true
            }
        }

        img_recyclerView.layoutManager=linearLayoutManager

        mAdapter= ImgSelectListAdapter(this,imgArray,this)

        img_recyclerView.adapter=mAdapter

        img_recyclerView.addItemDecoration(object : androidx.recyclerview.widget.RecyclerView.ItemDecoration() {
            override fun getItemOffsets(outRect: Rect, view: View, parent: androidx.recyclerview.widget.RecyclerView, state: androidx.recyclerview.widget.RecyclerView.State) {
                outRect.top = 2
                outRect.bottom = 1
                outRect.left = 2
                outRect.right = 0
            }
        })
    }


    override fun imgSelect(imgPath: String) {

        mImgpath = imgPath

        Glide.with(this).load(ApiService.BASE_URL_Api.plus(imgPath)).into(user_img)

    }

    override fun onClick(v: View?) {

        when (v?.id) {

            R.id.updata_user -> {

                if (!TextUtils.isEmpty(et_showName.text))
                    mShowName = et_showName.text.toString()

                if (!TextUtils.isEmpty(et_age.text))
                    mAge = et_age.text.toString().toInt()

                mMale = cb_male.isChecked

                var userLocal = App.instanceApp().getLocalUser()

                if (userLocal!=null){

                    present.updataUser(userLocal.name,mShowName,mAge,mMale,mImgpath,bindToLifecycle())

                    updata_user.startAnimation()
                }


            }


        }

    }

    override fun barViewClick(left: Boolean) {

        when (left) {

            true -> {

                finish()
            }

        }

    }

}