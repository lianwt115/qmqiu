package com.lwt.qmqiu.dao

import com.lwt.qmqiu.App
import com.lwt.qmqiu.bean.DownloadFileDb
import com.lwt.qmqiu.greendao.DownloadFileDbDao


class DownloadFileDbUtils {

    companion object:BaseDaoUtils<DownloadFileDb> {

        private val dao = App.instanceApp().getDaoSession().downloadFileDbDao


        override fun insertOrReplace(t: DownloadFileDb) {

            dao.insertOrReplace(t)
        }

        override fun getAll(normal: Boolean): MutableList<DownloadFileDb>? {

            //按时间排序

            var list = dao.loadAll()

            list.sortBy {

                if (normal) it.time else -it.time

            }

            return list
        }

        override fun findByIdOne(id: String): DownloadFileDb? {

            var billCashInfoList = findById(id)

            return if (billCashInfoList?.isNotEmpty()!!){

                billCashInfoList[0]

            }else{

                null

            }

        }

        override fun findById(id: String): MutableList<DownloadFileDb>? {

            var build = dao.queryBuilder()

            return build.where(DownloadFileDbDao.Properties.FileId.eq(id)).list()

        }

        override fun deleteById(id: String) {

            var obj = findByIdOne(id)

            if (obj !=null)
                dao.delete(obj)

        }

        override fun getCount(): Long {

            return dao.count()
        }

    }
}