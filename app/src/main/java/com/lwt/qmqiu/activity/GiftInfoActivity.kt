package com.lwt.qmqiu.activity




import android.graphics.Rect
import android.os.Bundle
import android.view.View
import com.google.android.material.tabs.TabLayout
import com.lwt.qmqiu.App
import com.lwt.qmqiu.R
import com.lwt.qmqiu.adapter.ChatAdatpter
import com.lwt.qmqiu.adapter.GiftShowAdapter
import com.lwt.qmqiu.bean.GiftInfo
import com.lwt.qmqiu.fragment.GiftListFragment
import com.lwt.qmqiu.widget.BarView
import com.orhanobut.logger.Logger
import kotlinx.android.synthetic.main.activity_giftinfo.*



class GiftInfoActivity : BaseActivity(),BarView.BarOnClickListener, GiftShowAdapter.GiftClickListen, TabLayout.OnTabSelectedListener{



    private var mTabs = listOf<String>("收入", "支出","兑换").toMutableList()

    private lateinit var mFragments: ArrayList<androidx.fragment.app.Fragment>

    //1收入  二是支出
    private val STRATEGY = arrayOf(1,2,3)//接口路徑路由



    private  var mGiftInfoList = ArrayList<GiftInfo>()
    private lateinit var mGiftShowAdapter:GiftShowAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_giftinfo)

        giftinfo_barview.setBarOnClickListener(this)
        giftinfo_barview.changeTitle("礼物详情")

        initRecycleView()

        var localUser= App.instanceApp().getLocalUser()

        if (localUser!=null)
            initGift(localUser.gift)

        initFragment()
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

        mGiftShowAdapter= GiftShowAdapter(this,mGiftInfoList,this,false)

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

    private fun initGift(gift:String) {

        mGiftInfoList.clear()

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

                mGiftInfoList.add(giftInfo)
        }

        mGiftShowAdapter.notifyDataSetChanged()
    }

    private fun initFragment() {

        var incoomFragment: GiftListFragment = GiftListFragment()
        var incoomBundle = Bundle()
        incoomBundle.putInt("type",STRATEGY[0])
        incoomFragment.arguments = incoomBundle


        var payFragment: GiftListFragment = GiftListFragment()
        var payBundle = Bundle()
        payBundle.putInt("type", STRATEGY[1])
        payFragment.arguments = payBundle

        var exchangeFragment: GiftListFragment = GiftListFragment()
        var exchangeBundle = Bundle()
        exchangeBundle.putInt("type", STRATEGY[2])
        exchangeFragment.arguments = exchangeBundle



        mFragments = ArrayList()
        mFragments.add(incoomFragment as androidx.fragment.app.Fragment)
        mFragments.add(payFragment as androidx.fragment.app.Fragment)
        mFragments.add(exchangeFragment as androidx.fragment.app.Fragment)



        vp_content.adapter = ChatAdatpter(supportFragmentManager, mFragments, mTabs)

        tabs.setupWithViewPager(vp_content)

        tabs.addOnTabSelectedListener(this)

    }



    override fun onTabReselected(tab: TabLayout.Tab?) {

    }

    override fun onTabUnselected(tab: TabLayout.Tab?) {
    }

    override fun onTabSelected(tab: TabLayout.Tab?) {
    }


    override fun giftClick(gift: GiftInfo, position: Int) {

        Logger.e(gift.toString())
    }
    override fun barViewClick(left: Boolean) {

        when (left) {

            true -> {

                finish()
            }

        }

    }

}