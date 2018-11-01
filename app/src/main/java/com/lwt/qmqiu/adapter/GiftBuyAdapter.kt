package com.lwt.qmqiu.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
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


class GiftBuyAdapter(context: Context, list: List<GiftInfo>,listen:GiftBuyClickListen) : RecyclerView.Adapter<GiftBuyAdapter.ListViewHolder>() {


    var context: Context? = null
    var mTotalList: List<GiftInfo>? = null
    var inflater: LayoutInflater? = null
    var listen: GiftBuyClickListen? = null
    init {
        this.context = context
        this.mTotalList = list
        this.inflater = LayoutInflater.from(context)
        this.listen = listen
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {

        val obj=mTotalList?.get(position)


        Glide.with(context!!).load(obj?.imgPath).into(holder.giftbuy_img)


        holder.giftbuy_count.text=obj?.count.toString()

        holder.giftbuy_price.text="青木球:${obj?.price}"


        holder.giftbuy_img.setOnClickListener {

            //检查是否超出购买力
            if (checkEnough(obj!!.price)) {

                obj?.count = obj?.count!!+1

                holder.giftbuy_count.text=obj?.count.toString()

                sendCash()

            }else{

                UiUtils.showToast(context!!.getString(R.string.coin_notenough))

            }


        }

        holder.giftbuy_delete.setOnClickListener {

            obj?.count = obj?.count!!-1

            if (obj.count<0)
                obj.count=0

            holder.giftbuy_count.text=obj?.count.toString()

            sendCash()
        }


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

        mTotalList?.forEach { giftInfo ->

            count += giftInfo.price * giftInfo.count

        }

        return  count
    }

     fun getGiftCount():String{

        return mTotalList!![0].count.toString().plus("*${mTotalList!![1].count}*${mTotalList!![2].count}*${mTotalList!![3].count}")

    }
     fun getPriceCount():String{

        return mTotalList!![0].price.toString().plus("*${mTotalList!![1].price}*${mTotalList!![2].price}*${mTotalList!![3].price}")

    }


    private fun sendCash() {

        if (listen != null) {

            listen?.giftBuyClick(getCount())

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        var itemView=inflater?.inflate(R.layout.item_giftbuy, parent, false)

        return ListViewHolder(itemView!!, context!!)
    }

    override fun getItemCount(): Int {

        return mTotalList?.size ?: 0
    }

    class ListViewHolder(itemView: View, context: Context) : RecyclerView.ViewHolder(itemView) {

        var giftbuy_img: ImageView = itemView.findViewById(R.id.giftbuy_img) as ImageView
        var giftbuy_delete: ImageView = itemView.findViewById(R.id.giftbuy_delete) as ImageView
        var giftbuy_count: TextView = itemView.findViewById(R.id.giftbuy_count) as TextView
        var giftbuy_price: TextView = itemView.findViewById(R.id.giftbuy_price) as TextView

    }

   /* fun getCount(): List<GiftInfo>? {

        return mTotalList

    }*/

    interface GiftBuyClickListen{
        fun giftBuyClick(cash: Int)
    }

}