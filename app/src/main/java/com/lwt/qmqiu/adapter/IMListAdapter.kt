package com.lwt.qmqiu.adapter

import android.content.Context
import android.graphics.drawable.Drawable
import androidx.recyclerview.widget.RecyclerView
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.TextView
import com.bumptech.glide.Glide
import com.github.chrisbanes.photoview.PhotoView
import com.joooonho.SelectableRoundedImageView
import com.lwt.qmqiu.App
import com.lwt.qmqiu.R
import com.lwt.qmqiu.R.id.voice_play
import com.lwt.qmqiu.bean.QMMessage
import com.lwt.qmqiu.download.DownloadListen
import com.lwt.qmqiu.download.DownloadManager
import com.lwt.qmqiu.network.ApiService
import com.lwt.qmqiu.utils.RSAUtils
import com.lwt.qmqiu.utils.UiUtils
import com.orhanobut.logger.Logger
import kotlinx.android.synthetic.main.activity_im.*
import java.text.SimpleDateFormat


class IMListAdapter(context: Context, list: List<QMMessage>, listen:IMClickListen?) : androidx.recyclerview.widget.RecyclerView.Adapter<IMListAdapter.ListViewHolder>() {


    var context: Context? = null
    var mTotalList: List<QMMessage>? = null
    var inflater: LayoutInflater? = null
    var listen: IMClickListen? = null
    private  var textTime:String="-1:-1:-1"

    private val formatter = SimpleDateFormat("yyyy-MM-dd*HH:mm:ss")
    private val formatter2 = SimpleDateFormat("HH:mm:ss")
    private val formatter3 = SimpleDateFormat("yyyy-MM-dd EEEE HH:mm:ss")

    companion object {

          var WHOCLICK   =  0
          var CONTENTCLICK = 1

    }

    init {
        this.context = context
        this.mTotalList = list
        this.listen = listen
        this.inflater = LayoutInflater.from(context)

    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {

        val obj=mTotalList?.get(position)


        Glide.with(context!!).load(ApiService.BASE_URL_Api.plus(obj?.imgPath)).into(holder.message_who)


        contentBg(obj!!.colorIndex,holder.message_content)

        //解决图片和文字,语音消息错乱
        holder.text_root.visibility =if (obj.type == 4)View.GONE else View.VISIBLE

        holder.img_root.visibility =if (obj.type == 4)View.VISIBLE else View.GONE

        when (obj.type) {

            0 -> {

                holder.message_content.text=App.instanceApp().getShowMessage(obj.message)

                holder.message_voice_time.visibility = View.GONE

                holder.message_content.setCompoundDrawablesWithIntrinsicBounds(null,
                        null, null, null)
            }

            3 -> {

                var data = App.instanceApp().getShowMessage(obj.message).split("_ALWTA_")

                if (data?.size>=2){

                    var drawableLeft = context!!.getDrawable(
                            R.mipmap.voice_type)

                    holder.message_content.setCompoundDrawablesWithIntrinsicBounds(drawableLeft,
                            null, null, null)

                    holder.message_content.compoundDrawablePadding = 10

                    holder.message_content.text=""


                    holder.message_voice_time.visibility = View.VISIBLE
                    holder.message_voice_time.text = "${data[1]}''"

                }else{

                    holder.message_content.text=data[0]

                }


            }

            4 -> {

                var data = App.instanceApp().getShowMessage(obj.message).split("_ALWTA_")


                holder.message_content.setCompoundDrawablesWithIntrinsicBounds(null,
                        null, null, null)

                var down = DownloadManager(object : DownloadListen {
                    override fun onStartDownload() {
                        Logger.e("onStartDownload")
                    }

                    override fun onProgress(progress: Int) {

                        Logger.e("onProgress:$progress")

                        holder.img_progress_text.text = "$progress %"
                    }

                    override fun onFinishDownload(path: String) {

                        holder.img_progress_text.visibility =View.GONE
                        holder.img_progress.visibility =View.GONE
                        Glide.with(context!!).load(path).into(holder.photo_view)

                    }

                    override fun onFail(errorInfo: String) {
                        Logger.e("onFail:$errorInfo")
                    }
                },data[0],4)


            }
        }



        val messageTime= timeData(obj.time)

        holder.message_time.text = messageTime

        //同一分钟就小时
        if (this.textTime.split(":")[1] == messageTime.split(":")[1])
            holder.message_time.visibility =View.GONE

        this.textTime = messageTime

        holder.message_who.setOnClickListener {
            if (listen!=null)
                listen?.imClick(obj,WHOCLICK,false)
        }

        holder.img_root.setOnClickListener {
            if (listen!=null)
                listen?.imClick(obj, CONTENTCLICK,false)
        }
        holder.message_content.setOnClickListener {
            if (listen!=null)
                listen?.imClick(obj,CONTENTCLICK,false)
        }

        holder.message_content.setOnLongClickListener {

            if (listen!=null)
                listen?.imClick(obj,CONTENTCLICK,true)

            return@setOnLongClickListener true
        }


    }

    private fun contentBg(from: Int, text: TextView) {

        var bg = context?.getDrawable(R.drawable.bg_20dp_1)

        try {

            when(from%24) {

                1 -> {
                    bg =  context?.getDrawable(R.drawable.bg_20dp_1)
                }
                2 -> {
                    bg =  context?.getDrawable(R.drawable.bg_20dp_2)
                }
                3 -> {
                    bg =  context?.getDrawable(R.drawable.bg_20dp_3)
                }
                4 -> {
                    bg =  context?.getDrawable(R.drawable.bg_20dp_4)
                }
                5 -> {
                    bg =  context?.getDrawable(R.drawable.bg_20dp_5)
                }
                6 -> {
                    bg =  context?.getDrawable(R.drawable.bg_20dp_6)
                }
                7 -> {
                    bg =  context?.getDrawable(R.drawable.bg_20dp_7)
                }
                8 -> {
                    bg =  context?.getDrawable(R.drawable.bg_20dp_8)
                }
                9 -> {
                    bg =  context?.getDrawable(R.drawable.bg_20dp_9)
                }
                10 -> {
                    bg =  context?.getDrawable(R.drawable.bg_20dp_10)
                }
                11 -> {
                    bg =  context?.getDrawable(R.drawable.bg_20dp_11)
                }
                12 -> {
                    bg =  context?.getDrawable(R.drawable.bg_20dp_12)
                }
                13-> {
                    bg =  context?.getDrawable(R.drawable.bg_20dp_13)
                }
                14 -> {
                    bg =  context?.getDrawable(R.drawable.bg_20dp_14)
                }
                15 -> {
                    bg =  context?.getDrawable(R.drawable.bg_20dp_15)
                }
                16 -> {
                    bg =  context?.getDrawable(R.drawable.bg_20dp_16)
                }
                17 -> {
                    bg =  context?.getDrawable(R.drawable.bg_20dp_17)
                }
                18 -> {
                    bg =  context?.getDrawable(R.drawable.bg_20dp_18)
                }
                19 -> {
                    bg =  context?.getDrawable(R.drawable.bg_20dp_19)
                }
                20 -> {
                    bg =  context?.getDrawable(R.drawable.bg_20dp_20)
                }
                21 -> {
                    bg =  context?.getDrawable(R.drawable.bg_20dp_21)
                }
                22 -> {
                    bg =  context?.getDrawable(R.drawable.bg_20dp_22)
                }
                23 -> {
                    bg =  context?.getDrawable(R.drawable.bg_20dp_23)
                }
                0 -> {
                    bg =  context?.getDrawable(R.drawable.bg_20dp_24)
                }

            }

            text.background = bg

        }catch (e:Exception){

            text.background = bg

        }


    }





    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        var itemView=inflater?.inflate(R.layout.item_immessage, parent, false)

        return ListViewHolder(itemView!!, context!!)
    }

    override fun getItemCount(): Int {

        return mTotalList?.size ?: 0
    }

    class ListViewHolder(itemView: View, context: Context) : androidx.recyclerview.widget.RecyclerView.ViewHolder(itemView) {

        var message_who: SelectableRoundedImageView = itemView?.findViewById(R.id.message_who) as SelectableRoundedImageView
        var message_content: TextView = itemView?.findViewById(R.id.message_content) as TextView
        var message_time: TextView = itemView?.findViewById(R.id.message_time) as TextView
        var message_voice_time: TextView = itemView?.findViewById(R.id.message_voice_time) as TextView
        var text_root: RelativeLayout = itemView?.findViewById(R.id.text_root) as RelativeLayout
        var img_root: RelativeLayout = itemView?.findViewById(R.id.img_root) as RelativeLayout
        var photo_view: ImageView = itemView?.findViewById(R.id.photo_view) as ImageView
        var img_progress: ProgressBar = itemView?.findViewById(R.id.img_progress) as ProgressBar
        var img_progress_text: TextView = itemView?.findViewById(R.id.img_progress_text) as TextView

    }

    interface IMClickListen{
        fun imClick(content:QMMessage, type: Int,longClick:Boolean)
    }

    private fun timeData(currentTime :Long):String{

        var today =formatter.format(System.currentTimeMillis())


        var messageTime =formatter.format(currentTime)


        //是否是今天
        if (today.split("*")[0] == messageTime.split("*")[0]){


            return formatter2.format(currentTime)

        }else{

            return formatter3.format(currentTime)
        }


    }

}