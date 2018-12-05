package com.lwt.qmqiu.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.RelativeLayout
import android.widget.TextView
import com.lwt.qmqiu.R
import com.lwt.qmqiu.bean.LocationInfo



class LocationListAdapter(context: Context, list: ArrayList<LocationInfo>, listen:TextClickListen?) : androidx.recyclerview.widget.RecyclerView.Adapter<LocationListAdapter.ListViewHolder>() {


    private var mContext: Context = context
    private var mTotalList: ArrayList<LocationInfo> = list
    private var mInflater: LayoutInflater = LayoutInflater.from(mContext)
    private var mTextClickListen: TextClickListen? = listen
    private var index = 0


    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {

        val obj= mTotalList[position]

        if (position == 0 ){

            holder.location_name.text=  obj.locationName.plus("(${obj.locationWhere})")

            holder.location_where.visibility = View.GONE


        }else{

            holder.location_name.text=  obj.locationName

            holder.location_where.text= obj.locationWhere
        }

        holder.location_cb.visibility = if (obj.select) View.VISIBLE else View.GONE


        holder.location_root.setOnClickListener {

            if (position != index){


                obj.select = true

                mTotalList[index].select = false

                index = position


                notifyDataSetChanged()
            }

            if (mTextClickListen !=null)
                mTextClickListen?.textClick(obj,position)

        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        var itemView=mInflater.inflate(R.layout.item_location, parent, false)

        return ListViewHolder(itemView!!, mContext)
    }

    override fun getItemCount(): Int {

        return mTotalList.size
    }

    class ListViewHolder(itemView: View, context: Context) : androidx.recyclerview.widget.RecyclerView.ViewHolder(itemView) {

        var location_name: TextView = itemView.findViewById(R.id.location_name) as TextView
        var location_where: TextView = itemView.findViewById(R.id.location_where) as TextView
        var location_root: RelativeLayout = itemView.findViewById(R.id.location_root) as RelativeLayout
        var location_cb: CheckBox = itemView.findViewById(R.id.location_cb) as CheckBox

    }

    interface TextClickListen{
        fun textClick(content:LocationInfo, position: Int)
    }

}