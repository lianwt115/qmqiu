package com.lwt.qmqiu.utils

import android.graphics.Bitmap
import android.util.Base64
import android.view.Gravity
import android.widget.Toast
import com.lwt.qmqiu.App
import java.io.ByteArrayOutputStream
import java.io.IOException


class UiUtils {


    companion object {

        private var mToast:Toast? = null

        fun showToast(str: String,isShort:Boolean = true ){

            val instance = App.instanceApp()

            var short=if (isShort)Toast.LENGTH_SHORT else Toast.LENGTH_LONG

            if (instance != null) {

                if (mToast ==null){

                    mToast= Toast.makeText(instance,str,short)
                }else{
                    mToast!!.setText(str)
                }

                mToast?.setGravity(Gravity.CENTER,0,0)

                mToast?.show()
            }

        }

        //生物识别使用
        fun bitmapToBase64(bitmap: Bitmap?): String? {

            var result: String? = null
            var baos: ByteArrayOutputStream? = null
            try {
                if (bitmap != null) {
                    baos = ByteArrayOutputStream()
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)

                    baos.flush()
                    baos.close()

                    val bitmapBytes = baos.toByteArray()

                    result = Base64.encodeToString(bitmapBytes, Base64.DEFAULT)
                }
            } catch (e: IOException) {
                e.printStackTrace()
            } finally {
                try {
                    if (baos != null) {
                        baos.flush()
                        baos.close()
                    }
                } catch (e: IOException) {
                    e.printStackTrace()
                }

            }

            return result
        }

    }

}