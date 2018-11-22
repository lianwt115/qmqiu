package com.lwt.qmqiu.adapter

import android.content.Context
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.core.view.ViewCompat
import com.bumptech.glide.Glide
import com.joooonho.SelectableRoundedImageView
import com.lwt.qmqiu.App
import com.lwt.qmqiu.R
import com.lwt.qmqiu.bean.PhotoViewData
import com.lwt.qmqiu.bean.QMMessage
import com.lwt.qmqiu.download.DownloadListen
import com.lwt.qmqiu.download.DownloadManager
import com.lwt.qmqiu.network.ApiService
import com.lwt.qmqiu.utils.applySchedulers
import com.orhanobut.logger.Logger
import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import java.text.SimpleDateFormat
import java.util.concurrent.TimeUnit


class IMListAdapter(context: Context, list: List<QMMessage>, listen:IMClickListen?) : androidx.recyclerview.widget.RecyclerView.Adapter<IMListAdapter.ListViewHolder>() {


    var context: Context? = null
    var mTotalList: List<QMMessage>? = null
    var inflater: LayoutInflater? = null
    var listen: IMClickListen? = null
    private var taskDisposable: Disposable? = null
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

        var data = App.instanceApp().getShowMessage(obj.message)

        when (obj.type) {

            0 -> {

                holder.message_content.text = data

                holder.message_voice_time.visibility = View.GONE

                holder.message_content.setCompoundDrawablesWithIntrinsicBounds(null,
                        null, null, null)
            }

            3 -> {

                var dataAll = data.split("_ALWTA_")

                if (dataAll?.size>=2){

                    var drawableLeft = context!!.getDrawable(
                            R.mipmap.voice_type)

                    holder.message_content.setCompoundDrawablesWithIntrinsicBounds(drawableLeft,
                            null, null, null)

                    holder.message_content.compoundDrawablePadding = 10

                    holder.message_content.text=""


                    holder.message_voice_time.visibility = View.VISIBLE
                    holder.message_voice_time.text = "${data[1]}''"

                    //进行声音预下载
                    DownloadManager(object :DownloadListen{
                        override fun onStartDownload() {

                        }

                        override fun onProgress(progress: Int) {

                        }

                        override fun onFinishDownload(path: String) {

                        }

                        override fun onFail(errorInfo: String) {
                            Logger.e("onFail:$errorInfo")
                        }
                    },dataAll[0],3)


                }else{

                    holder.message_content.text=dataAll[0]

                }

            }

            4 -> {

                var dataAll = data.split("_ALWTA_")


                holder.message_content.setCompoundDrawablesWithIntrinsicBounds(null,
                        null, null, null)

                DownloadManager(object : DownloadListen {
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
                },dataAll[0],4)

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
                listen?.imClick(obj,WHOCLICK,false,position)
        }

        holder.img_root.setOnClickListener {
            if (listen!=null)
                listen?.imClick(obj, CONTENTCLICK,false,position,holder.photo_view,PhotoViewData(position,obj.message))
        }

        holder.message_content.setOnClickListener {

            if (obj.type == 3) {

                var data = App.instanceApp().getShowMessage(obj.message).split("_ALWTA_")

                if (data?.size >= 2) {
                    Logger.e("time:${data[1].toInt() * 5}")
                    taskDisposable = Observable.interval(200, TimeUnit.MILLISECONDS).applySchedulers().subscribe({

                        Logger.e("it:$it")
                        if (it >= data[1].toInt() * 5) {

                            if (taskDisposable!=null)

                                taskDisposable!!.dispose()

                            holder.message_content.setCompoundDrawablesWithIntrinsicBounds( context!!.getDrawable(
                                    R.mipmap.voice_type) as Drawable,
                                    null, null, null)

                            return@subscribe

                        }

                        var drawableLeft = when (it % 3) {

                            0L -> {

                                context!!.getDrawable(
                                        R.mipmap.voice1) as Drawable
                            }

                            1L -> {

                                context!!.getDrawable(
                                        R.mipmap.voice2) as Drawable
                            }

                            2L -> {

                                context!!.getDrawable(
                                        R.mipmap.voice3) as Drawable
                            }

                            else -> {

                                context!!.getDrawable(
                                        R.mipmap.voice_type) as Drawable
                            }

                        }

                        holder.message_content.setCompoundDrawablesWithIntrinsicBounds(drawableLeft,
                                null, null, null)

                    }, {

                        Logger.e(it.localizedMessage)
                    })
                }

            }
            if (listen!=null)
                listen?.imClick(obj,CONTENTCLICK,false,position)
        }

        holder.message_content.setOnLongClickListener {

            if (listen!=null)
                listen?.imClick(obj,CONTENTCLICK,true,position)

            return@setOnLongClickListener true
        }
        holder.img_root.setOnLongClickListener {

            if (listen!=null)
                listen?.imClick(obj,CONTENTCLICK,true,position)

            return@setOnLongClickListener true
        }


        //绑定
        ViewCompat.setTransitionName(holder.photo_view, data)


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
        fun imClick(content:QMMessage, type: Int,longClick:Boolean,position: Int,view:View?=null,data:PhotoViewData?=null)
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

    fun  getPhotoViewData():ArrayList<PhotoViewData>{

        var list =ArrayList<PhotoViewData>()


        mTotalList?.forEachIndexed { index, qmMessage ->

            //图片信息
            if (qmMessage.type == 4)

                list.add(PhotoViewData(index,qmMessage.message))

        }

        return list
    }


}