package com.lwt.qmqiu.activity



import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Rect
import android.os.Bundle
import android.text.*
import android.view.View
import com.lwt.qmqiu.R
import com.lwt.qmqiu.bean.BaseUser
import com.lwt.qmqiu.utils.SPHelper
import com.lwt.qmqiu.utils.UiUtils
import com.lwt.qmqiu.widget.BarView
import com.bumptech.glide.Glide
import com.lwt.qmqiu.adapter.GiftBuyAdapter
import com.lwt.qmqiu.adapter.GiftShowAdapter
import com.lwt.qmqiu.bean.GiftInfo
import com.lwt.qmqiu.mvp.contract.UserInfoContract
import com.lwt.qmqiu.mvp.present.UserInfoPresent
import com.lwt.qmqiu.network.ApiService
import com.orhanobut.logger.Logger
import kotlinx.android.synthetic.main.activity_userinfo.*
import com.lwt.qmqiu.App
import com.lwt.qmqiu.bean.IMChatRoom
import com.lwt.qmqiu.bean.RefuseLog
import com.lwt.qmqiu.utils.applySchedulers
import com.lwt.qmqiu.widget.NoticeDialog
import io.reactivex.Observable
import java.util.concurrent.TimeUnit


class UserInfoActivity : BaseActivity(),BarView.BarOnClickListener, UserInfoContract.View, GiftShowAdapter.GiftClickListen, GiftBuyAdapter.GiftBuyClickListen, View.OnClickListener, NoticeDialog.Builder.BtClickListen {


    private lateinit var mUserName:String
    //是否兑换
    private  var mExchange:Boolean = false
    private  var mLocalUserName:String =SPHelper.getInstance().get("loginName","") as String
    private lateinit var present: UserInfoPresent
    private lateinit var mGiftShowAdapter:GiftShowAdapter
    private lateinit var mGiftBuyAdapter:GiftBuyAdapter
    private  var mGiftInfoList = ArrayList<GiftInfo>()
    private  var mGiftInfoListBuy = ArrayList<GiftInfo>()
    private  var mIsMySelf = false
    private  var mLocalRefuseOther = false
    private  var mSendGiftIndex = -1
    private  var mSendGiftCount = 0
    private  var mSendGiftList = listOf<String>("天使宝贝","挚爱玫瑰","激情跑车","女王皇冠")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_userinfo)

        //房间信息
        mUserName=intent.getStringExtra("name")

        mExchange=intent.getBooleanExtra("exchange",false)

        userinfo_barview.setBarOnClickListener(this)

        userinfo_barview.changeTitle(if (mExchange)getString(R.string.gift_exchange) else getString(R.string.user_info))

        present = UserInfoPresent(this,this)

        present.userFind(mUserName,bindToLifecycle())

        mIsMySelf = mLocalUserName == mUserName

        userinfo_barview.showMore(mIsMySelf)

        initRecycleView()

        if (mIsMySelf){

            initButton()

            initRecycleViewBuy()
        }else{

            //检测 我有没有阻止他
            present.refuseCheck(mLocalUserName,mUserName,bindToLifecycle())
        }

        gift_buy.text = if (mExchange) "兑换" else "购买礼物"

        gift_buy.background = getDrawable(R.drawable.bg_20dp_13)
        gift_buy.setFinalCornerRadius(20F)
        gift_buy.setOnClickListener(this)


        gift_send.text = "赠送礼物"
        gift_send.background = getDrawable(R.drawable.bg_20dp_13)
        gift_send.setFinalCornerRadius(20F)
        gift_send.setOnClickListener(this)



        gift_send_delete.setOnClickListener(this)

        room_private.setOnClickListener(this)

        message_refuse.setFinalCornerRadius(20F)
        message_refuse.setOnClickListener(this)

    }

    //礼物购买
    override fun onClick(v: View?) {

        when (v?.id) {

            R.id.gift_buy -> {

               if (mExchange){


                   if (mGiftBuyAdapter.getCount() > 0){

                       gift_buy.startAnimation()

                       present.coinExchange(mUserName,mGiftBuyAdapter.getGiftCount(),bindToLifecycle())

                   }else{

                       UiUtils.showToast("请选择礼物数量")

                   }

               }else{

                   if (mGiftBuyAdapter.getCount() > 0){

                       gift_buy.startAnimation()

                       present.giftBuy(mUserName,mGiftBuyAdapter.getCount(),mGiftBuyAdapter.getGiftCount(),mGiftBuyAdapter.getPriceCount(),bindToLifecycle())

                   }else{

                       UiUtils.showToast("请选择礼物数量")

                   }
               }

            }
            R.id.gift_send -> {

                if (mSendGiftIndex == -1 || mSendGiftCount<1){

                    UiUtils.showToast(getString(R.string.sendgift_notice))

                }else{

                    gift_send.startAnimation()

                    present.giftSend(mLocalUserName,mUserName,mSendGiftIndex,mSendGiftCount,bindToLifecycle())

                }


            }
            R.id.gift_send_delete -> {

                if (mSendGiftCount < 1)
                    return
                //1-0
                mSendGiftCount--

                if (mSendGiftCount == 0){

                    gift_send_info.visibility = View.GONE

                    gift_send_delete.visibility = View.GONE

                    gift_send_info.text = ""

                    mSendGiftIndex = -1
                }else{

                    gift_send_info.text =Html.fromHtml("赠送: <font color='#FF4081'>"+mSendGiftList[mSendGiftIndex]+"   </font>"+"数量: "+"<font color='#FF4081'>"+mSendGiftCount+"</font>")

                }


            }
            //开启私人聊天
            R.id.room_private -> {

                if (this.mLocalRefuseOther){

                    UiUtils.showToast("请先解除阻止")

                    return
                }

                showProgressDialog("需 10青木 或 免费",true,2,this)

            }
            //阻止
            R.id.message_refuse -> {

                message_refuse.startAnimation()

                present.refuseUser(mLocalUserName,mUserName,!mLocalRefuseOther,bindToLifecycle())

            }


        }

    }

    //阻止动作
    override fun setRefuseUser(refuseLog: RefuseLog) {

        this.mLocalRefuseOther = refuseLog.status!!

        message_refuse.doneLoadingAnimation(resources.getColor(if (refuseLog.status!!) R.color.text_color_14 else R.color.text_color_9), BitmapFactory.decodeResource(resources,R.mipmap.ic_done))

        Observable.timer(1,TimeUnit.SECONDS).applySchedulers().subscribe({

            message_refuse.revertAnimation {

                when (refuseLog.status) {

                    true -> {

                        message_refuse.text = "解除阻止"
                        message_refuse.background = getDrawable(R.drawable.bg_20dp_21)
                    }

                    false -> {

                        message_refuse.text = "阻止"
                        message_refuse.background = getDrawable(R.drawable.bg_20dp_24)
                    }
                }
            }

        },{
            Logger.e("按钮复原异常")
        })

    }

    //阻止检测
    override fun setRefuseCheck(refuse: Boolean) {

        this.mLocalRefuseOther = refuse

        when (refuse) {

            true-> {

                message_refuse.text = "解除阻止"
                message_refuse.background = getDrawable(R.drawable.bg_20dp_21)
            }
            false-> {

                message_refuse.text = "阻止"
                message_refuse.background = getDrawable(R.drawable.bg_20dp_24)
            }

        }

    }




    override fun btClick(etContent: String): Boolean {

        var user = App.instanceApp().getLocalUser()
        var location = App.instanceApp().getBDLocation()

        if (user != null){

            present.creatIMChatRoom(user.name,location?.latitude ?: 0.000000, location?.longitude
                    ?: 0.000000,3,user.name.plus("ALWTA$mUserName"),bindToLifecycle())

            return true

        }else{

            UiUtils.showToast("请先登录")

            return false
        }

    }


    override fun creatIMChatRoomSuccess(room: IMChatRoom) {

        showProgressDialogSuccess(true)

        Observable.timer(1,TimeUnit.SECONDS).applySchedulers().subscribe({

            //进入聊天室
            val intent = Intent(this, IMActivity::class.java)

            intent.putExtra("imChatRoom",room)

            startActivity(intent)

        },{
            Logger.e("按钮复原异常")
        })

    }

    private fun initRecycleViewBuy() {

        val linearLayoutManager = object : androidx.recyclerview.widget.LinearLayoutManager(this, androidx.recyclerview.widget.LinearLayoutManager.HORIZONTAL,false){
            override fun canScrollVertically(): Boolean {
                return false
            }

            override fun canScrollHorizontally(): Boolean {
                return false
            }
        }
        recycleview_giftbuy.layoutManager=linearLayoutManager

        mGiftBuyAdapter= GiftBuyAdapter(this,mGiftInfoListBuy,this,mExchange)

        recycleview_giftbuy.adapter = mGiftBuyAdapter

        recycleview_giftbuy.addItemDecoration(object : androidx.recyclerview.widget.RecyclerView.ItemDecoration() {
            override fun getItemOffsets(outRect: Rect, view: View, parent: androidx.recyclerview.widget.RecyclerView, state: androidx.recyclerview.widget.RecyclerView.State) {
                outRect.top = 0
                outRect.bottom = 0
                outRect.left = 0
                outRect.right =0
            }
        })

    }

    private fun initButton() {

        bt_me.visibility = View.VISIBLE
        bt_other.visibility = View.GONE

    }

    private fun initGift(gift:String,buy:Boolean=false) {

        if (buy)
            mGiftInfoListBuy.clear()
        else
            mGiftInfoList.clear()


        var gifts= gift.split("*")

        gifts.forEachIndexed { index, s ->

            var giftInfo = GiftInfo(if (buy) 0 else s.toInt())

            when (index) {

                0-> {
                    giftInfo.imgPath = R.mipmap.angel
                    giftInfo.savgPath = "angel.svga"
                    giftInfo.price = 18
                }

                1 -> {
                    giftInfo.imgPath = R.mipmap.rose
                    giftInfo.savgPath = "rose.svga"
                    giftInfo.price = 38

                }
                2 -> {
                    giftInfo.imgPath = R.mipmap.paoche
                    giftInfo.savgPath = "posche.svga"
                    giftInfo.price = 68

                }
                3 -> {
                    giftInfo.imgPath = R.mipmap.kingset
                    giftInfo.savgPath = "kingset.svga"
                    giftInfo.price = 88

                }
            }

            if (buy)

                mGiftInfoListBuy.add(giftInfo)
            else

                mGiftInfoList.add(giftInfo)
        }

        if (buy)
            mGiftBuyAdapter.notifyDataSetChanged()
        else
            mGiftShowAdapter.notifyDataSetChanged()
    }


    override fun barViewClick(left: Boolean) {

        when (left) {

            true -> {

                finish()
            }

            false -> {

                if (mExchange){

                    val intent = Intent(this, CoinInfoActivity::class.java)

                    startActivity(intent)

                }else{

                    UiUtils.showToast("可以编辑")

                }



            }


        }

    }
    private fun initRecycleView() {


        val linearLayoutManager = object : androidx.recyclerview.widget.LinearLayoutManager(this, androidx.recyclerview.widget.LinearLayoutManager.HORIZONTAL,false){
            override fun canScrollVertically(): Boolean {
                return false
            }

            override fun canScrollHorizontally(): Boolean {
                return false
            }
        }
        recycleview_gift.layoutManager=linearLayoutManager

        mGiftShowAdapter= GiftShowAdapter(this,mGiftInfoList,this,!mIsMySelf)

        recycleview_gift.adapter = mGiftShowAdapter

        recycleview_gift.addItemDecoration(object : androidx.recyclerview.widget.RecyclerView.ItemDecoration() {
            override fun getItemOffsets(outRect: Rect, view: View, parent: androidx.recyclerview.widget.RecyclerView, state: androidx.recyclerview.widget.RecyclerView.State) {
                outRect.top = 0
                outRect.bottom = 0
                outRect.left = 14
                outRect.right =14
            }
        })
    }



    override fun setUser(baseUser: BaseUser) {

        //图像
        Glide.with(this).load(ApiService.BASE_URL_Api.plus(baseUser.imgPath)).into(user_img)

        user_name.changeTitleAndContent(baseUser.name,"")

        if (!mExchange){

            user_gender.visibility = View.VISIBLE
            user_age.visibility = View.VISIBLE

            user_gender.changeTitleAndContent("性别",if(baseUser.male)"男" else "女")
            user_age.changeTitleAndContent("年龄",baseUser.age.toString())
        }

        user_basecoin.changeTitleAndContent("青木",baseUser.coinbase.toString())
        user_coin.changeTitleAndContent("青木球",baseUser.coin.toString())

        initGift(baseUser.gift)
        if (mIsMySelf)
            initGift(baseUser.gift,true)
        Logger.e(baseUser.toString())

    }

    //礼物购买成功
    override fun setGiftBuy(baseUser: BaseUser) {

        gift_buy.doneLoadingAnimation(resources.getColor(R.color.bt_bg9), BitmapFactory.decodeResource(resources,R.mipmap.ic_done))

        Observable.timer(1,TimeUnit.SECONDS).applySchedulers().subscribe({

            gift_buy.revertAnimation()

        },{
            Logger.e("按钮复原异常")
        })

        //刷新本地用户信息
        App.instanceApp().updataLocalUser(baseUser,true,true)
        //更新金币和礼物
        user_coin.changeTitleAndContent("青木球",baseUser.coin.toString())
        //更新礼物
        initGift(baseUser.gift)
        if (mIsMySelf)
            initGift(baseUser.gift,true)

        gift_buy_cash.text = ""
    }

    //赠送礼物成功
    override fun setGiftSend(baseUser: BaseUser) {
        //播放动画,设置本地用户信息
        //刷新本地用户信息
        App.instanceApp().updataLocalUser(baseUser,true)
        //刷新当前页信息
        present.userFind(mUserName,bindToLifecycle())

        gift_send.doneLoadingAnimation(resources.getColor(R.color.bt_bg9), BitmapFactory.decodeResource(resources,R.mipmap.ic_done))

        Observable.timer(500,TimeUnit.MILLISECONDS).applySchedulers().subscribe({

            gift_send.revertAnimation()
            //mGiftInfoList[mSendGiftIndex].savgPath
            //mSendGiftIndex
            showSuccessGift(mSendGiftIndex)

        },{
            Logger.e("按钮复原异常")
        })


    }

    private fun showSuccessGift(giftIndex: Int) {


        //数量-单位-名称-动画名称

        val info = Html.fromHtml("$mLocalUserName(我)  赠送: <font color='#FF4081'>"+mSendGiftCount+"</font>\t"+"${giftUnitList[giftIndex]}"+"<font color='#FF4081'>\t ${giftNameList[giftIndex]}</font>" +"\n给<font color='#FF4081'> $mUserName</font>")


        showGiftDialog(info,giftPathList[giftIndex])

    }

    override fun err(code: Int, errMessage: String?, type: Int) {

        when (type) {

            1 -> {

                Logger.e("获取个人信息失败")

            }

            2,7 -> {

                gift_buy!!.doneLoadingAnimation(resources.getColor(R.color.bt_bg9), BitmapFactory.decodeResource(resources,R.mipmap.error))

                Observable.timer(1,TimeUnit.SECONDS).applySchedulers().subscribe({

                    gift_buy.revertAnimation()

                },{
                    Logger.e("按钮复原异常")
                })

                Logger.e("礼物购买失败")

            }
            3 -> {

                gift_send!!.doneLoadingAnimation(resources.getColor(R.color.bt_bg9), BitmapFactory.decodeResource(resources,R.mipmap.error))

                Observable.timer(1,TimeUnit.SECONDS).applySchedulers().subscribe({

                    gift_send.revertAnimation()

                },{
                    Logger.e("按钮复原异常")
                })

                Logger.e("礼物赠送失败")

            }
            4 -> {

                showProgressDialogSuccess(false)

            }

            5 -> {

                message_refuse!!.doneLoadingAnimation(resources.getColor(if (!this.mLocalRefuseOther) R.color.text_color_14 else R.color.text_color_9), BitmapFactory.decodeResource(resources,R.mipmap.error))

                Observable.timer(1,TimeUnit.SECONDS).applySchedulers().subscribe({

                    message_refuse.revertAnimation()
                },{
                    Logger.e("按钮复原异常")
                })

                Logger.e("阻止失败")

            }
        }


        UiUtils.showToast(errMessage!!)

    }

    override fun giftClick(gift: GiftInfo, position: Int) {

        if (!mIsMySelf){

            if (mSendGiftIndex == position)

                mSendGiftCount++

            else
                mSendGiftCount = 1

            gift_send_info.visibility = View.VISIBLE
            gift_send_delete.visibility = View.VISIBLE

            gift_send_info.text =Html.fromHtml("赠送: <font color='#FF4081'>"+mSendGiftList[position]+"   </font>"+"数量: "+"<font color='#FF4081'>"+mSendGiftCount+"</font>")

            mSendGiftIndex = position
        }

        Logger.e(gift.toString())
    }

    override fun giftBuyClick(cash: Int) {


        var showCash = if (mExchange) (cash*0.7).toInt() else cash

        var showNotice = if (mExchange) "可兑换" else "总花费"

        gift_buy_cash.text = if (cash<=0)"" else "$showNotice : $showCash 青木球"

    }


    override fun onDestroy() {
        super.onDestroy()

        gift_buy.dispose()
        gift_send.dispose()
        room_private.dispose()
        message_refuse.dispose()
    }


}