package com.lwt.qmqiu.activity



import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
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
import com.opensource.svgaplayer.SVGADrawable
import com.opensource.svgaplayer.SVGAVideoEntity
import com.opensource.svgaplayer.SVGAParser
import org.jetbrains.annotations.NotNull
import android.text.style.ForegroundColorSpan
import com.lwt.qmqiu.App
import com.lwt.qmqiu.R.id.*
import com.lwt.qmqiu.R.string.gift
import com.lwt.qmqiu.utils.applySchedulers
import com.opensource.svgaplayer.SVGADynamicEntity
import io.reactivex.Observable
import kotlinx.android.synthetic.main.activity_userinfo.view.*
import java.util.concurrent.TimeUnit


class UserInfoActivity : BaseActivity(),BarView.BarOnClickListener, UserInfoContract.View, GiftShowAdapter.GiftClickListen, GiftBuyAdapter.GiftBuyClickListen, View.OnClickListener {


    private lateinit var mUserName:String
    private  var mLocalUserName:String =SPHelper.getInstance().get("loginName","") as String
    private lateinit var present: UserInfoPresent
    private lateinit var mGiftShowAdapter:GiftShowAdapter
    private lateinit var mGiftBuyAdapter:GiftBuyAdapter
    private  var mGiftInfoList = ArrayList<GiftInfo>()
    private  var mGiftInfoListBuy = ArrayList<GiftInfo>()
    private  var mIsMySelf = false
    private  lateinit var mSVGAParser:SVGAParser
    private  var mSendGiftIndex = -1
    private  var mSendGiftCount = 0
    private  var mSendGiftList = listOf<String>("天使宝贝","挚爱玫瑰","激情跑车","女王皇冠")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_userinfo)

        //房间信息
        mUserName=intent.getStringExtra("name")

        userinfo_barview.setBarOnClickListener(this)
        userinfo_barview.changeTitle(getString(R.string.user_info))

        present = UserInfoPresent(this,this)

        present.userFind(mUserName,bindToLifecycle())

        mIsMySelf = mLocalUserName == mUserName

        userinfo_barview.showMore(mIsMySelf)

        initRecycleView()

        initSvga()

        if (mIsMySelf){

            initButton()

            initRecycleViewBuy()
        }

        gift_buy.setOnClickListener(this)
        gift_send.setOnClickListener(this)
        gift_send_delete.setOnClickListener(this)

    }

    //礼物购买
    override fun onClick(v: View?) {

        when (v?.id) {

            R.id.gift_buy -> {

                if (mGiftBuyAdapter.getCount() > 0){

                    gift_buy.startAnimation()

                    present.giftBuy(mUserName,mGiftBuyAdapter.getCount(),mGiftBuyAdapter.getGiftCount(),mGiftBuyAdapter.getPriceCount(),bindToLifecycle())

                }else{

                    UiUtils.showToast("请选择礼物数量")

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


        }

    }


    private fun initRecycleViewBuy() {

        val linearLayoutManager = object : LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false){
            override fun canScrollVertically(): Boolean {
                return false
            }

            override fun canScrollHorizontally(): Boolean {
                return false
            }
        }
        recycleview_giftbuy.layoutManager=linearLayoutManager

        mGiftBuyAdapter= GiftBuyAdapter(this,mGiftInfoListBuy,this)

        recycleview_giftbuy.adapter = mGiftBuyAdapter

        recycleview_giftbuy.addItemDecoration(object : RecyclerView.ItemDecoration() {
            override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
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

    private fun initSvga() {

        svgaPlayer.clearsAfterStop =true

        svgaPlayer.loops = 1
        mSVGAParser = SVGAParser(this)


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
                    giftInfo.imgPath = R.mipmap.posche
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

                UiUtils.showToast("可以编辑")

            }


        }

    }
    private fun initRecycleView() {


        val linearLayoutManager = object : LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false){
            override fun canScrollVertically(): Boolean {
                return false
            }

            override fun canScrollHorizontally(): Boolean {
                return false
            }
        }
        recycleview_gift.layoutManager=linearLayoutManager

        mGiftShowAdapter= GiftShowAdapter(this,mGiftInfoList,this)

        recycleview_gift.adapter = mGiftShowAdapter

        recycleview_gift.addItemDecoration(object : RecyclerView.ItemDecoration() {
            override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
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
        user_gender.changeTitleAndContent("性别",if(baseUser.male)"男" else "女")
        user_age.changeTitleAndContent("年龄",baseUser.age.toString())
        user_basecoin.changeTitleAndContent("青木",baseUser.coinbase.toString())
        user_coin.changeTitleAndContent("青木球",baseUser.coin.toString())

        initGift(baseUser.gift)
        if (mIsMySelf)
            initGift(baseUser.gift,true)
        Logger.e(baseUser.toString())

    }

    //礼物购买成功
    override fun setGiftBuy(baseUser: BaseUser) {

        gift_buy.doneLoadingAnimation(resources.getColor(R.color.white), BitmapFactory.decodeResource(resources,R.mipmap.ic_done))

        Observable.timer(1,TimeUnit.SECONDS).applySchedulers().subscribe({

            gift_buy.revertAnimation()

        },{
            Logger.e("按钮复原异常")
        })

        //刷新本地用户信息
        App.instanceApp().setLocalUser(baseUser)
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
        App.instanceApp().setLocalUser(baseUser)
        //刷新当前页信息
        present.userFind(mUserName,bindToLifecycle())

        gift_send.doneLoadingAnimation(resources.getColor(R.color.white), BitmapFactory.decodeResource(resources,R.mipmap.ic_done))

        Observable.timer(1,TimeUnit.SECONDS).applySchedulers().subscribe({

            gift_send.revertAnimation()

            //播放动画
            mSVGAParser.parse(mGiftInfoList[mSendGiftIndex].savgPath, object : SVGAParser.ParseCompletion {
                override fun onComplete(@NotNull videoItem: SVGAVideoEntity) {
                    var drawable:SVGADrawable
                    if (mSendGiftIndex == 3)
                        drawable= SVGADrawable(videoItem,requestDynamicItemWithSpannableText())
                    else
                        drawable= SVGADrawable(videoItem)
                    svgaPlayer.setImageDrawable(drawable)
                    svgaPlayer.startAnimation()
                }

                override fun onError() {

                    Logger.e("动画异常")
                }
            })



        },{
            Logger.e("按钮复原异常")
        })


    }
    override fun err(code: Int, errMessage: String?, type: Int) {

        when (type) {

            1 -> {

                Logger.e("获取个人信息失败")

            }

            2 -> {

                gift_buy!!.doneLoadingAnimation(resources.getColor(R.color.white), BitmapFactory.decodeResource(resources,R.mipmap.error))

                Observable.timer(1,TimeUnit.SECONDS).applySchedulers().subscribe({

                    gift_buy.revertAnimation()

                },{
                    Logger.e("按钮复原异常")
                })

                Logger.e("礼物购买失败")

            }
            3 -> {

                gift_send!!.doneLoadingAnimation(resources.getColor(R.color.white), BitmapFactory.decodeResource(resources,R.mipmap.error))

                Observable.timer(1,TimeUnit.SECONDS).applySchedulers().subscribe({

                    gift_send.revertAnimation()

                },{
                    Logger.e("按钮复原异常")
                })

                Logger.e("礼物赠送失败")

            }
        }


        UiUtils.showToast(errMessage!!)

    }

    override fun giftClick(gift: GiftInfo, position: Int) {

        if (mIsMySelf){

            mSVGAParser.parse(gift.savgPath, object : SVGAParser.ParseCompletion {
                override fun onComplete(@NotNull videoItem: SVGAVideoEntity) {
                    var drawable:SVGADrawable
                    if (position == 3)
                        drawable= SVGADrawable(videoItem,requestDynamicItemWithSpannableText())
                    else
                        drawable= SVGADrawable(videoItem)
                    svgaPlayer.setImageDrawable(drawable)
                    svgaPlayer.startAnimation()
                }

                override fun onError() {

                    Logger.e("动画异常")
                }
            })

        }else{

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

        gift_buy_cash.text = if (cash<=0)"" else "总花费: $cash 青木球"

    }


    private fun requestDynamicItemWithSpannableText(): SVGADynamicEntity {
        val dynamicEntity = SVGADynamicEntity()
        val spannableStringBuilder = SpannableStringBuilder(mLocalUserName.plus("送了 $mSendGiftCount ${mSendGiftList[mSendGiftIndex]} 给 $mUserName"))
        spannableStringBuilder.setSpan(ForegroundColorSpan(Color.YELLOW), 0, mLocalUserName.length, Spannable.SPAN_INCLUSIVE_INCLUSIVE)
        val textPaint = TextPaint()
        textPaint.color = Color.WHITE
        textPaint.textSize = 28f
        dynamicEntity.setDynamicText(StaticLayout(
                spannableStringBuilder,
                0,
                spannableStringBuilder.length,
                textPaint,
                0,
                Layout.Alignment.ALIGN_CENTER,
                1.0f,
                0.0f,
                false
        ), "banner")
        dynamicEntity.setDynamicDrawer({ canvas, frameIndex ->
            val aPaint = Paint()
            aPaint.color = Color.WHITE
            canvas.drawCircle(50f, 54f, (frameIndex % 5).toFloat(), aPaint)
            false
        }, "banner")
        return dynamicEntity
    }

    override fun onDestroy() {
        super.onDestroy()

        gift_buy.dispose()
    }


}