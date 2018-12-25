package com.lwt.qmqiu.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import com.bumptech.glide.Glide
import com.lwt.qmqiu.R
import com.lwt.qmqiu.bean.GiftInfo


class GiftShowAdapter(context: Context, list: List<GiftInfo>, listen: GiftClickListen?,change:Boolean=false) : androidx.recyclerview.widget.RecyclerView.Adapter<GiftShowAdapter.ListViewHolder>() {


    private var mContext: Context = context
    private var mTotalList: List<GiftInfo> = list
    private var mInflater: LayoutInflater = LayoutInflater.from(mContext)
    private var mGiftClickListen: GiftClickListen? = listen
    private var mChange: Boolean = change
    private var index = -1


    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {

        val obj= mTotalList[position]


        Glide.with(mContext).load(obj.imgPath).into(holder.gift_img)


        holder.gift_count.text=obj.count.toString()

        //选择背景
        holder.gift_img.background = mContext.getDrawable(if (obj.select) R.drawable.bg_acc_rectangle_0 else R.drawable.bg_line_rectangle_lastpage_noradius)

        holder.gift_root.setOnClickListener {

            //只显示当前选择

            if (mChange){
                if (index!=-1)

                    mTotalList[index].select =false

                obj.select = true

                index = position

                notifyDataSetChanged()
            }

            if (mGiftClickListen!=null)
                mGiftClickListen?.giftClick(obj,position)
        }


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        var itemView=mInflater.inflate(R.layout.item_giftshow, parent, false)

        return ListViewHolder(itemView!!, mContext)
    }

    override fun getItemCount(): Int {

        return mTotalList.size
    }

    class ListViewHolder(itemView: View, context: Context) : androidx.recyclerview.widget.RecyclerView.ViewHolder(itemView) {

        var gift_img: ImageView = itemView.findViewById(R.id.gift_img) as ImageView
        var gift_count: TextView = itemView.findViewById(R.id.gift_count) as TextView
        var gift_root: RelativeLayout = itemView.findViewById(R.id.gift_root) as RelativeLayout

    }

    interface GiftClickListen{
        fun giftClick(gift:GiftInfo, position: Int)
    }



}