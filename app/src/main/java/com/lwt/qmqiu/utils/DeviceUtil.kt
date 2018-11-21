package com.lwt.qmqiu.utils

import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.core.content.FileProvider
import android.telephony.TelephonyManager
import android.util.DisplayMetrics
import android.view.WindowManager
import com.lwt.qmqiu.App
import com.orhanobut.logger.Logger
import java.io.File



/**
 * Created by Administrator on 2018\1\15 0015.
 */
class DeviceUtil {
    companion object {
        @SuppressLint("MissingPermission")
        fun getDeviceImei(context: Context): String {
            return (context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager).deviceId
        }

        fun getAndroidId(context: Context): String {
            return Settings.System.getString(context.contentResolver, Settings.Secure.ANDROID_ID)
        }

        fun getVersionCode(context: Context): Int {
            val mContext = context.applicationContext
            var versionCode = -1
            try {
                val pi = mContext.packageManager.getPackageInfo(context.packageName, 0)
                versionCode = pi.versionCode
            } catch (e: PackageManager.NameNotFoundException) {
                e.printStackTrace()
            }

            return versionCode
        }

        fun getVersionName(context: Context): String {
            val mContext = context.applicationContext
            var versionName = "1.0"
            try {
                val pi = mContext.packageManager.getPackageInfo(context.packageName, 0)
                versionName = pi.versionName
            } catch (e: PackageManager.NameNotFoundException) {
                e.printStackTrace()
            }

            return versionName
        }


        /**
         * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
         */
        fun dip2px(context: Context, dpValue: Float): Int {
            val mContext = context.applicationContext
            if (mContext != null) {
                val scale = mContext.resources.displayMetrics.density
                return (dpValue * scale + 0.5f).toInt()
            }

            return 0
        }

        /**
         * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
         */
        fun px2dip(context: Context, pxValue: Float): Int {
            val mContext = context.applicationContext
            if (mContext != null) {
                val scale = mContext.resources.displayMetrics.density
                return (pxValue / scale + 0.5f).toInt()
            }
            return 0

        }

        /**
         * 将sp值转换为px值，保证文字大小不变
         *
         * @param spValue
         *
         * （DisplayMetrics类中属性scaledDensity）
         * @return
         */
        fun sp2px(context: Context, spValue: Float): Int {
            val mContext = context.applicationContext
            val fontScale = mContext.resources.displayMetrics.scaledDensity
            return (spValue * fontScale + 0.5f).toInt()
        }

        /**
         * px装换成sp
         *
         * @param context
         * @param pxValue
         * @return
         */
        fun px2sp(context: Context, pxValue: Float): Int {
            val mContext = context.applicationContext
            val fontScale = mContext.resources.displayMetrics.scaledDensity
            return (pxValue / fontScale + 0.5f).toInt()
        }

        fun getWidth(context: Context): Int {
            val mContext = context.applicationContext
            if (mContext != null) {
                val manager = mContext.getSystemService(Context.WINDOW_SERVICE) as WindowManager
                val dm = DisplayMetrics()
                manager.defaultDisplay.getMetrics(dm)
                return dm.widthPixels
            }
            return 0
        }

        fun getHeight(context: Context): Int {
            val mContext = context.applicationContext
            if (mContext != null) {
                val manager = mContext.getSystemService(Context.WINDOW_SERVICE) as WindowManager
                val dm = DisplayMetrics()
                manager.defaultDisplay.getMetrics(dm)
                return dm.heightPixels
            }
            return 0
        }

        fun fileToUri(context: Context, file: File): Uri? {

            val mContext = context.applicationContext
            var uFile: Uri? = null
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                uFile = FileProvider.getUriForFile(mContext, "nc.ctl.com.ctdnc.provider", file)
            } else {
                uFile = Uri.fromFile(file)
            }
            return uFile


        }

        /**
         * 获取apk包的信息：版本号，名称，图标等
         *
         * @param absPath apk包的绝对路径
         */
        fun getApkPathInfoCode(absPath: String): Int {

            val pm = App.instanceApp().packageManager
            val pkgInfo = pm.getPackageArchiveInfo(absPath, PackageManager.GET_ACTIVITIES)
            var versionCode = -1
            var version = ""
            if (pkgInfo != null) {
                val appInfo = pkgInfo!!.applicationInfo
                /* 必须加这两句，不然下面icon获取是default icon而不是应用包的icon */
                appInfo.sourceDir = absPath
                appInfo.publicSourceDir = absPath
                val appName = pm.getApplicationLabel(appInfo).toString()// 得到应用名
                val packageName = appInfo.packageName // 得到包名
                version = pkgInfo!!.versionName // 得到版本信息
                val pkgInfoStr = String.format("PackageName:%s, Vesion: %s, AppName: %s", packageName, version, appName)
                Logger.i(String.format("PkgInfo: %s", pkgInfoStr))
                versionCode = pkgInfo!!.versionCode
            }

            return versionCode
        }
    }
}