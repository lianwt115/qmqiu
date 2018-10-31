package com.lwt.qmqiu.activity



import android.graphics.Rect
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
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
import com.opensource.svgaplayer.SVGAImageView
import com.opensource.svgaplayer.SVGADrawable
import com.opensource.svgaplayer.SVGAVideoEntity
import com.opensource.svgaplayer.SVGAParser
import org.jetbrains.annotations.NotNull


class UserInfoActivity : BaseActivity(),BarView.BarOnClickListener, UserInfoContract.View, GiftShowAdapter.GiftClickListen, GiftBuyAdapter.GiftBuyClickListen {

    private lateinit var mUserName:String
    private lateinit var present: UserInfoPresent
    private lateinit var mGiftShowAdapter:GiftShowAdapter
    private lateinit var mGiftBuyAdapter:GiftBuyAdapter
    private  var mGiftInfoList = ArrayList<GiftInfo>()
    private  var mGiftInfoListBuy = ArrayList<GiftInfo>()
    private  var mIsMySelf = false
    private  lateinit var mSVGAParser:SVGAParser
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_userinfo)

        //房间信息
        mUserName=intent.getStringExtra("name")

        userinfo_barview.setBarOnClickListener(this)
        userinfo_barview.changeTitle(getString(R.string.user_info))

        present = UserInfoPresent(this,this)

        present.userFind(mUserName,bindToLifecycle())

        mIsMySelf = SPHelper.getInstance().get("loginName","") as String == mUserName

        userinfo_barview.showMore(mIsMySelf)

        initRecycleView()

        if (mIsMySelf){

            initSvga()

            initButton()

            initRecycleViewBuy()
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

        var gifts= gift.split("*")

        gifts.forEachIndexed { index, s ->

            var giftInfo = GiftInfo(s.toInt())

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
            mGiftBuyAdapter
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

    override fun err(code: Int, errMessage: String?, type: Int) {

        UiUtils.showToast(errMessage!!)

    }

    override fun giftClick(gift: GiftInfo, position: Int) {

        if (mIsMySelf){

            mSVGAParser.parse(gift.savgPath, object : SVGAParser.ParseCompletion {
                override fun onComplete(@NotNull videoItem: SVGAVideoEntity) {
                    val drawable = SVGADrawable(videoItem)
                    svgaPlayer.setImageDrawable(drawable)
                    svgaPlayer.startAnimation()
                }

                override fun onError() {

                    Logger.e("动画异常")
                }
            })

        }



        Logger.e(gift.toString())
    }

    override fun giftBuyClick(cash: Int) {

        gift_buy_cash.text = if (cash<=0)"" else "总花费: $cash 青木球"

    }

}