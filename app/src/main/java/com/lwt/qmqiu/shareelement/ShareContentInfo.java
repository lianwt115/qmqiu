package com.lwt.qmqiu.shareelement;

import android.os.Parcel;

import android.view.View;

import com.hw.ycshareelement.transition.ShareElementInfo;
import com.lwt.qmqiu.bean.PhotoViewData;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;


/**
 * Created by huangwei on 2018/9/27.
 */
public class ShareContentInfo extends ShareElementInfo<PhotoViewData> {

    public ShareContentInfo(@NonNull View view, @Nullable PhotoViewData data) {
        super(view, data);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
    }

    protected ShareContentInfo(Parcel in) {
        super(in);
    }

    public static final Creator<ShareContentInfo> CREATOR = new Creator<ShareContentInfo>() {
        @Override
        public ShareContentInfo createFromParcel(Parcel source) {
            return new ShareContentInfo(source);
        }

        @Override
        public ShareContentInfo[] newArray(int size) {
            return new ShareContentInfo[size];
        }
    };
}
