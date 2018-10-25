package com.lwt.qmqiu.widget

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.text.Html
import android.view.*
import android.widget.Button
import android.widget.TextView
import com.baidu.location.BDLocation
import com.lwt.qmqiu.R
import com.lwt.qmqiu.utils.DeviceUtil

/**
 * Created by Administrator on 2018\2\1 0001.
 */
class MapNoticeDialog : Dialog {

    constructor(context: Context) : super(context) {}

    constructor(context: Context, themeResId: Int) : super(context, themeResId) {}

    protected constructor(context: Context, cancelable: Boolean, cancelListener: DialogInterface.OnCancelListener?) : super(context, cancelable, cancelListener) {}

    interface MapNoticeDialogListen {

        fun clickState(status:Boolean,location: BDLocation, mapNoticeDialog: MapNoticeDialog)

    }

    class Builder(private val mContext: Context,private val location: BDLocation) : View.OnClickListener{

        private var mMapNoticeDialogListen: MapNoticeDialogListen? = null

        private var mMapNoticeDialog: MapNoticeDialog? = null
        private var layout: View? = null

        private var locationPoint: TextView? = null
        private var joinYes: Button? = null
        private var joinNo: Button? = null



        fun create(mapNoticeDialogListen: MapNoticeDialogListen): MapNoticeDialog {

            this.mMapNoticeDialogListen = mapNoticeDialogListen

            val inflater = mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

            mMapNoticeDialog = MapNoticeDialog(mContext, R.style.MyDialog)
            mMapNoticeDialog!!.setCanceledOnTouchOutside(true)
            layout = inflater.inflate(R.layout.dialog_ordernotice, null)
            mMapNoticeDialog!!.addContentView(layout!!, ViewGroup.LayoutParams(
                    DeviceUtil.dip2px(mContext, 360f), DeviceUtil.dip2px(mContext, 72f)))

            val window = mMapNoticeDialog!!.window

            window!!.setGravity(Gravity.TOP)
            //window.setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT)
            //window.setType(WindowManager.LayoutParams.TYPE_TOAST)
            initView()

            return mMapNoticeDialog!!
        }

        private fun initView() {



            locationPoint = layout!!.findViewById(R.id.location_point) as TextView

            joinNo = layout!!.findViewById(R.id.join_no) as Button
            joinYes = layout!!.findViewById(R.id.join_yes) as Button

            setData(location)

            //layout!!.setOnClickListener(this)
            joinYes!!.setOnClickListener(this)
            joinNo!!.setOnClickListener(this)

        }

        private fun setData(location: BDLocation) {

            locationPoint!!.text= "进入经纬度(${location.longitude },${location.latitude })的房间"


        }

        fun updata(location: BDLocation){

            setData(location)

        }

        override fun onClick(v: View?) {

            if (mMapNoticeDialogListen == null) {
                return
            }

            when (v?.id) {

                R.id.join_no -> {
                    mMapNoticeDialogListen!!.clickState(false,location,mMapNoticeDialog!!)
                }

                R.id.join_yes -> {
                    mMapNoticeDialogListen!!.clickState(true,location,mMapNoticeDialog!!)
                }
            }

        }

    }
}