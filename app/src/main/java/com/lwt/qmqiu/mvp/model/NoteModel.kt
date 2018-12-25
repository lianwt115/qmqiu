package com.lwt.qmqiu.mvp.model

import android.content.Context
import com.lwt.qmqiu.bean.NoteCommentLog
import com.lwt.qmqiu.bean.NoteLog
import com.lwt.qmqiu.network.HttpResultFunc
import io.reactivex.Observable


class NoteModel(context: Context) : BaseModel(context) {


    fun getNote(name:String,noteType:Int,latitude:Double, longitude:Double): Observable<List<NoteLog>>?{

        return apiService?.noteGet(name,noteType,latitude,longitude)?.map(HttpResultFunc())

    }

    fun goodNote(name:String,id:String): Observable<Boolean>?{

        return apiService?.noteGood(name,id)?.map(HttpResultFunc())

    }
    fun reportNote(name:String,id:String,why:Int): Observable<Boolean>?{

        return apiService?.noteReport(name,id,why)?.map(HttpResultFunc())

    }

    fun getComment(name:String,id:String): Observable<List<NoteCommentLog>>?{

        return apiService?.commentGet(name,id)?.map(HttpResultFunc())

    }

    fun createComment(name:String,id:String,commentText:String): Observable<NoteCommentLog>?{

        return apiService?.commentCreate(name,id,commentText)?.map(HttpResultFunc())

    }

    fun deleteComment(name:String,id:String): Observable<Boolean>?{

        return apiService?.commentDelete(name,id)?.map(HttpResultFunc())

    }
    fun reportComment(name:String,id:String,why:Int): Observable<Boolean>?{

        return apiService?.commentReport(name,id,why)?.map(HttpResultFunc())

    }

}