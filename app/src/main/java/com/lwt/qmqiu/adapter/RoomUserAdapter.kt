package com.lwt.qmqiu.adapter

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView
import com.bumptech.glide.Glide
import com.joooonho.SelectableRoundedImageView
import com.lwt.qmqiu.R
import com.lwt.qmqiu.bean.BaseUser
import com.lwt.qmqiu.network.ApiService


class RoomUserAdapter(context: Context, list: List<BaseUser>, listen:UserClickListen?) : androidx.recyclerview.widget.RecyclerView.Adapter<RoomUserAdapter.ListViewHolder>() {


    var context: Context? = null
    var mTotalList: List<BaseUser>? = null
    var inflater: LayoutInflater? = null
    var listen: UserClickListen? = null


    init {
        this.context = context
        this.mTotalList = list
        this.listen = listen
        this.inflater = LayoutInflater.from(context)

    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {

        val obj=mTotalList?.get(position)


        Glide.with(context!!).load(ApiService.BASE_URL_Api.plus(obj?.imgPath)).into(holder.user_img)


        holder.user_name.text=obj?.name


        holder.user_root.setOnClickListener {
            if (listen!=null)
                listen?.userClick(obj!!,position)
        }


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        var itemView=inflater?.inflate(R.layout.item_roomuser, parent, false)

        return ListViewHolder(itemView!!, context!!)
    }

    override fun getItemCount(): Int {

        return mTotalList?.size ?: 0
    }

    class ListViewHolder(itemView: View, context: Context) : androidx.recyclerview.widget.RecyclerView.ViewHolder(itemView) {

        var user_img: SelectableRoundedImageView = itemView.findViewById(R.id.user_img) as SelectableRoundedImageView
        var user_name: TextView = itemView.findViewById(R.id.user_name) as TextView
        var user_root: RelativeLayout = itemView.findViewById(R.id.user_root) as RelativeLayout

    }

    interface UserClickListen{
        fun userClick(user:BaseUser, position: Int)
    }



}