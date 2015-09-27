package me.kellymckinnon.shirtswap;

import java.util.Date;

public class Message {
    private String mText;
    private String mSender;
    private Date mDate;

    public String getText() {
        return mText;
    }

    public void setText(String text) {
        mText = text;
    }

    public String getSender() {
        return mSender;
    }

    public void setSender(String sender) {
        mSender = sender;
    }

    public Date getDate() {
        return mDate;
    }

    public void setDate(Date date) {
        mDate = date;
    }
}
