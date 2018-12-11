package com.lwt.qmqiu.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.lwt.qmqiu.App
import com.lwt.qmqiu.R
import com.lwt.qmqiu.bean.GiftInfo
import com.lwt.qmqiu.utils.UiUtils


class GiftBuyAdapter(context: Context, list: List<GiftInfo>, listen: GiftBuyClickListen, mExchange: Boolean) : androidx.recyclerview.widget.RecyclerView.Adapter<GiftBuyAdapter.ListViewHolder>() {


    private var mContext: Context = context
    private var mTotalList: List<GiftInfo> = list
    private var mInflater: LayoutInflater =  LayoutInflater.from(mContext)
    private var mGiftBuyClickListen: GiftBuyClickListen? = listen
    private var index = -1

    private var mExchange = mExchange

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {

        val obj= mTotalList[position]


        Glide.with(mContext).load(obj.imgPath).into(holder.giftbuy_img)


        holder.giftbuy_count.text=obj.count.toString()

        holder.giftbuy_price.text="青木球:${obj?.price}"

        //选择背景
        holder.giftbuy_img.background = mContext.getDrawable(if (obj.select) R.drawable.bg_acc_rectangle_0 else R.drawable.bg_line_rectangle_lastpage_noradius)

        holder.giftbuy_img.setOnClickListener {

            //只显示当前选择

            if (index!=-1)

                 mTotalList[index].select =false

            obj.select = true

            index = position

            notifyDataSetChanged()


            if (mExchange) {

                //检查是否超出兑换
                if (checkEnoughEx(position,obj.count)) {

                    obj.count += 1

                    holder.giftbuy_count.text=obj.count.toString()

                    sendCash()

                }else{

                    UiUtils.showToast(mContext.getString(R.string.ex_notenough))

                }

            }else{

                //检查是否超出购买力
                if (checkEnough(obj.price)) {

                    obj.count += 1

                    holder.giftbuy_count.text=obj.count.toString()

                    sendCash()

                }else{

                    UiUtils.showToast(mContext.getString(R.string.coin_notenough))

                }

            }


        }

        holder.giftbuy_delete.setOnClickListener {

            obj.count = obj.count-1

            if (obj.count<0)
                obj.count=0

            holder.giftbuy_count.text=obj.count.toString()

            sendCash()
        }


    }

    private fun checkEnoughEx(index: Int,count:Int):Boolean {


        var user =App.instanceApp().getLocalUser()

        if (user!=null){

            var giftLocal = user.gift.split("*")

            if (index<giftLocal.size)

                return  count < giftLocal[index].toInt()

        }

        return false
    }
    private fun checkEnough(price: Int):Boolean {

        var user =App.instanceApp().getLocalUser()

        if (user!=null){

            return  getCount() <= (user.coin-price)

        }

        return false
    }

    fun getCount():Int{

        var count = 0

        mTotalList.forEach { giftInfo ->

            count += giftInfo.price * giftInfo.count

        }

        return  count
    }

     fun getGiftCount():String{

        return mTotalList[0].count.toString().plus("*${mTotalList[1].count}*${mTotalList[2].count}*${mTotalList[3].count}")

    }
     fun getPriceCount():String{

        return mTotalList[0].price.toString().plus("*${mTotalList[1].price}*${mTotalList[2].price}*${mTotalList[3].price}")

    }


    private fun sendCash() {

        if (mGiftBuyClickListen != null) {

            mGiftBuyClickListen?.giftBuyClick(getCount())

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        var itemView=mInflater.inflate(R.layout.item_giftbuy, parent, false)

        return ListViewHolder(itemView!!, mContext)
    }

    override fun getItemCount(): Int {

        return mTotalList.size
    }

    class ListViewHolder(itemView: View, context: Context) : androidx.recyclerview.widget.RecyclerView.ViewHolder(itemView) {

        var giftbuy_img: ImageView = itemView.findViewById(R.id.giftbuy_img) as ImageView
        var giftbuy_delete: ImageView = itemView.findViewById(R.id.giftbuy_delete) as ImageView
        var giftbuy_count: TextView = itemView.findViewById(R.id.giftbuy_count) as TextView
        var giftbuy_price: TextView = itemView.findViewById(R.id.giftbuy_price) as TextView

    }


    interface GiftBuyClickListen{
        fun giftBuyClick(cash: Int)
    }

}