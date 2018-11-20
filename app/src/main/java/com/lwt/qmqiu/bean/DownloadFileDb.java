package com.lwt.qmqiu.bean;


import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.Unique;

/**
 * 实体类必须要用java才行
 */
@Entity
public class DownloadFileDb {

    @Id
    private Long id;
    @NotNull
    //文件名
    private String fileName;
    //文件id
    @Unique
    private String fileId;
    //下载时间
    private Long time;
    //文件path
    private String filePath;
    @Generated(hash = 498193780)
    public DownloadFileDb(Long id, @NotNull String fileName, String fileId,
            Long time, String filePath) {
        this.id = id;
        this.fileName = fileName;
        this.fileId = fileId;
        this.time = time;
        this.filePath = filePath;
    }
    @Generated(hash = 885526998)
    public DownloadFileDb() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getFileName() {
        return this.fileName;
    }
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
    public String getFileId() {
        return this.fileId;
    }
    public void setFileId(String fileId) {
        this.fileId = fileId;
    }
    public Long getTime() {
        return this.time;
    }
    public void setTime(Long time) {
        this.time = time;
    }
    public String getFilePath() {
        return this.filePath;
    }
    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

}
