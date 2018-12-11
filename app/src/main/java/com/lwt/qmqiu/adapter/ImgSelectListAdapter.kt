package com.lwt.qmqiu.adapter

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import com.bumptech.glide.Glide
import com.joooonho.SelectableRoundedImageView
import com.lwt.qmqiu.App
import com.lwt.qmqiu.R
import com.lwt.qmqiu.R.id.user_img
import com.lwt.qmqiu.bean.GiftLog
import com.lwt.qmqiu.bean.IMChatRoom
import com.lwt.qmqiu.network.ApiService
import com.lwt.qmqiu.utils.StaticValues
import kotlinx.android.synthetic.main.activity_userinfo.*
import java.text.SimpleDateFormat
import java.util.regex.Pattern


class ImgSelectListAdapter(context: Context, list: List<String>,listen:ImgSelectClick?=null) : androidx.recyclerview.widget.RecyclerView.Adapter<ImgSelectListAdapter.ListViewHolder>() {


    private var mContext: Context = context
    private var mTotalList: List<String> = list
    private var mInflater: LayoutInflater = LayoutInflater.from(mContext)
    private var mListen:ImgSelectClick? = listen
    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {

        val obj= mTotalList[position]

        //图像
        Glide.with(mContext).load(ApiService.BASE_URL_Api.plus(obj)).into(holder.user_img)

        holder.user_img.setOnClickListener {

            if (mListen!=null)
                mListen?.imgSelect(obj)

        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        var itemView=mInflater.inflate(R.layout.item_imgselect, parent, false)

        return ListViewHolder(itemView!!, mContext)
    }

    override fun getItemCount(): Int {

        return mTotalList.size
    }

    class ListViewHolder(itemView: View, context: Context) : androidx.recyclerview.widget.RecyclerView.ViewHolder(itemView) {

        var user_img: SelectableRoundedImageView = itemView.findViewById(R.id.user_img) as SelectableRoundedImageView

    }

    interface ImgSelectClick{

        fun  imgSelect(imgPath:String)

    }


}