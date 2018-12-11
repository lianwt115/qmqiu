package com.lwt.qmqiu.adapter

import android.content.Context
import android.text.ClipboardManager
import android.text.TextUtils
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import com.lwt.qmqiu.App
import com.lwt.qmqiu.R
import com.lwt.qmqiu.R.id.creatcharge_num
import com.lwt.qmqiu.bean.CoinLog
import com.lwt.qmqiu.bean.GiftLog
import com.lwt.qmqiu.bean.IMChatRoom
import com.lwt.qmqiu.utils.StaticValues
import com.lwt.qmqiu.utils.UiUtils
import kotlinx.android.synthetic.main.activity_charge.*
import java.text.SimpleDateFormat
import java.util.regex.Pattern


class CoinListAdapter(context: Context, list: ArrayList<CoinLog>, mStrategy: Int) : androidx.recyclerview.widget.RecyclerView.Adapter<CoinListAdapter.ListViewHolder>() {


    private var mContext: Context = context
    private var mTotalList: ArrayList<CoinLog> = list
    private var mInflater: LayoutInflater = LayoutInflater.from(mContext)
    private val formatter = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
    private val today = formatter.format(System.currentTimeMillis())
    private val mType = mStrategy
    protected val giftNameList = StaticValues.giftNameList



    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {

        val obj= mTotalList[position]

        when (mType) {

            //2兑换记录
            0,1,2-> {

                holder.coin_type.setImageDrawable(mContext.getDrawable(if (mType == 0 || mType == 2) R.mipmap.giftplus else R.mipmap.giftminus))

                holder.coin_name.text = when (mType) {
                    0 -> {
                      "充值"
                    }
                    1 -> {
                       "消费"
                    }
                    2 -> {
                        "兑换"
                    }
                    else->{
                        "其他"
                    }
                }
                //如果是消费 则记录用途 0 开附近房 1开公共房 2购买礼物 3视频聊天 4开私人房
                if (mType==1){

                    holder.coin_content.text = when (obj.toType) {

                                            1 -> {
                                                "创建公共聊天室"
                                            }

                                            2 -> {
                                                "购买礼物"
                                            }
                                            3 -> {
                                                "视频聊天"
                                            }

                                            4 -> {
                                                "创建私人聊天室"
                                            }
                                            else -> {
                                                "创建附近聊天室"
                                            }
                                        }

                }

            }

            3 -> {

                holder.coin_type.setImageDrawable(mContext.getDrawable(if (obj.used) R.mipmap.used else R.mipmap.notuser))
                holder.coin_name.text ="充值码"

                if (!obj.used){

                    holder.root_contain.setOnLongClickListener {

                        val cm = mContext.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                        // 将文本内容放到系统剪贴板里。

                        cm.text = obj.chargeNumber

                        UiUtils.showToast("已经复制到剪切板")


                        true
                    }


                }

            }
        }


        holder.coin_time.text = timeData(if (mType == 0)obj.chargeTime else obj.happenTime)

        holder.coin_cash.text =  "${if (obj.coinType == 0)"青木" else "青木球"}:${obj.cash}"


    }

    private fun showContent(giftCount: String): String {

        var giftCount = giftCount.split("*")

        var returnContent = StringBuilder()

        var appendCount = 0
        giftCount.forEachIndexed { index, s ->

            if (s != "0"){
                appendCount++
                returnContent.append(giftNameList[index].plus("x$s${if (appendCount==2)"\n" else ""}"))
            }

        }

        return returnContent.toString()

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        var itemView=mInflater.inflate(R.layout.item_coinlist, parent, false)

        return ListViewHolder(itemView!!, mContext)
    }

    override fun getItemCount(): Int {

        return mTotalList.size
    }

    class ListViewHolder(itemView: View, context: Context) : androidx.recyclerview.widget.RecyclerView.ViewHolder(itemView) {

        var coin_type: ImageView = itemView.findViewById(R.id.coin_type) as ImageView
        var coin_name: TextView = itemView.findViewById(R.id.coin_name) as TextView
        var coin_content: TextView = itemView.findViewById(R.id.coin_content) as TextView
        var coin_time: TextView = itemView.findViewById(R.id.coin_time) as TextView
        var coin_cash: TextView = itemView.findViewById(R.id.coin_cash) as TextView
        var root_contain: RelativeLayout = itemView.findViewById(R.id.root_contain) as RelativeLayout

    }

    private fun timeData(currentTime :Long):String{

        return formatter.format(currentTime)

    }

}