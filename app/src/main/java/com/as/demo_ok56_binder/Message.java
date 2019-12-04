package com.as.demo_ok56_binder;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * -----------------------------
 * Created by zqf on 2019/12/4.
 * ---------------------------
 */
public class Message implements Parcelable {

    //发送内容
    private String content;

    //发送状态
    private boolean isSendSuccess;

    // set get


    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public boolean isSendSuccess() {
        return isSendSuccess;
    }

    public void setSendSuccess(boolean sendSuccess) {
        isSendSuccess = sendSuccess;
    }

    //alert insert  parcelable
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.content);
        dest.writeByte(this.isSendSuccess ? (byte) 1 : (byte) 0);
    }

    public Message() {
    }

    protected Message(Parcel in) {
        this.content = in.readString();
        this.isSendSuccess = in.readByte() != 0;
    }

    public static final Parcelable.Creator<Message> CREATOR = new Parcelable.Creator<Message>() {
        @Override
        public Message createFromParcel(Parcel source) {
            return new Message(source);
        }

        @Override
        public Message[] newArray(int size) {
            return new Message[size];
        }
    };

    public void readFromParcel(Parcel parcel) {
        content = parcel.readString();
        isSendSuccess = parcel.readByte() == 1;
    }
}
