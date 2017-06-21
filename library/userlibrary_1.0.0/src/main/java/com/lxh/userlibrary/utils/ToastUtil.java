package com.lxh.userlibrary.utils;

import android.content.Context;
import android.widget.Toast;

import com.lxh.userlibrary.UserCenter;


/**
 * Created by wbb on 2016/6/17.
 * Author:Eric_chan
 */
public class ToastUtil {
    static Toast toast;

    public static void toastLong(Context mContext, String msg) {
        if (toast == null) {
            toast = Toast.makeText(mContext, msg, Toast.LENGTH_LONG);
        }
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setText(msg);

        toast.show();
    }

    public static void toastLong(Context mContext, int msgId) {
        if (toast == null) {
            toast = Toast.makeText(mContext, msgId, Toast.LENGTH_LONG);
        }
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setText(msgId);

        toast.show();
    }

    public static void toastShort(Context mContext, String msg) {

        if (toast == null) {
            toast = Toast.makeText(mContext, msg, Toast.LENGTH_SHORT);
        }
        toast.setDuration(Toast.LENGTH_SHORT);

        toast.setText(msg);

        toast.show();

    }

    public static void toastShort(Context mContext, int msgId) {

        if (toast == null) {
            toast = Toast.makeText(mContext, msgId, Toast.LENGTH_SHORT);
        }
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setText(msgId);
        toast.show();

    }

    /**
     * 新
     * @param msgId
     */
    public static void toastShort(int msgId) {

        if (toast == null) {
            toast = Toast.makeText(UserCenter.getContext(), UserCenter.getContext().getResources().getText(msgId), Toast.LENGTH_SHORT);
        }
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setText(msgId);
        toast.show();
    }

    public static void toastShort(String msg) {
        if (toast == null) {
            toast = Toast.makeText(UserCenter.getContext(), msg, Toast.LENGTH_SHORT);
        }
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setText(msg);
        toast.show();
    }

}
