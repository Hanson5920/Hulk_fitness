package com.lxh.userlibrary.utils;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;


import com.lxh.userlibrary.entity.ShareInfo;

import java.util.HashMap;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.wechat.friends.Wechat;
import cn.sharesdk.wechat.moments.WechatMoments;

/**
 * 分享调用
 * lxh
 */
public class ShareUtils {

    public static final int SHARE_SUCCESS = 1;
    public static final int SHARE_CANCEL = 2;
    public static final int SHARE_FAILURE = 3;
    private static String PlateName;
    static Context contexts;

    /**
     * 分享
     *
     * @param context
     * @param mData
     * @param platformName    Wechat.NAME 微信
     *                WechatMoments.NAME 微信朋友圈
     *                QQ.NAME qq
     *                QZone.NAME qq空间
     *                SinaWeibo.NAME新浪微博
     *  @param isShareShot 是否截屏分享
     */


    public static void Share(Context context, ShareInfo mData, String platformName, boolean isShareShot) {
        contexts = context;
        //传入数据
        Platform.ShareParams shareParams = new Platform.ShareParams();
        if (platformName.equals(Wechat.NAME) || platformName.equals(WechatMoments.NAME)) {//微信
            if (isShareShot){
                shareParams.setTitle(mData.getTitle());
                shareParams.setImagePath(mData.getImgUrl());
                shareParams.setShareType(Platform.SHARE_IMAGE);
            }else {
                shareParams.setTitle(mData.getTitle());
                shareParams.setText(mData.getText());//详情
                shareParams.setUrl(mData.getUrl());
                shareParams.setImagePath(mData.getImgUrl());//分享的图标
                shareParams.setShareType(Platform.SHARE_WEBPAGE);
            }
        } else if (platformName.equals(SinaWeibo.NAME)) {//微博
            shareParams.setText(mData.getTitle() + " " + mData.getText() + mData.getUrl() + mData.getId());//详情
            shareParams.setImagePath(mData.getImgUrl());
        } else {//QQ
            if (isShareShot){
                shareParams.setImagePath(mData.getImgUrl());
            }else {
                shareParams.setText(mData.getText());//详情
                shareParams.setTitle(mData.getTitle());
                shareParams.setImagePath(mData.getImgUrl());//分享的图标
                shareParams.setTitleUrl(mData.getUrl());
            }
        }

        Platform platform = ShareSDK.getPlatform(platformName);
        platform.setPlatformActionListener(new PlatformActionListenerImpl());
        // 执行分享
        platform.share(shareParams);
    }

    static class PlatformActionListenerImpl implements PlatformActionListener {
        @Override
        public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {//回调的地方是子线程，进行UI操作要用handle处理
            Message msg = Message.obtain();
            msg.what = SHARE_SUCCESS;
            msg.obj = platform.getName();
            handler.sendMessage(msg);
        }

        @Override
        public void onError(Platform platform, int i, Throwable throwable) {//回调的地方是子线程，进行UI操作要用handle处理
            throwable.printStackTrace();
            Message msg = Message.obtain();
            msg.what = SHARE_FAILURE;
            msg.obj = throwable.getMessage();
            handler.sendMessage(msg);
        }

        @Override
        public void onCancel(Platform platform, int i) {//回调的地方是子线程，进行UI操作要用handle处理
            handler.sendEmptyMessage(SHARE_CANCEL);
        }
    }

    //记录成功的状态
// IsSuccess
    public static Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SHARE_SUCCESS:
                    PlateName = (String) msg.obj;
                    if (PlateName.equals("Wechat")) {
                        Toast.makeText(contexts, "微信分享成功", Toast.LENGTH_LONG).show();
                    } else if (PlateName.equals("WechatMoments")) {
                        Toast.makeText(contexts, "朋友圈分享成功", Toast.LENGTH_LONG).show();
                    } else if (PlateName.equals("SinaWeibo")) {
                        Toast.makeText(contexts, "新浪分享成功", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(contexts, PlateName + "分享成功", Toast.LENGTH_LONG).show();
                    }
//                    Statistics.ShareStatistics(mDatas, new StringCallback() {
//                        @Override
//                        public void onError(Call call, Exception e) {
//                            XUtilLog.log_e("cq", "" + e.getMessage());
//
//                        }
//
//                        @Override
//                        public void onResponse(String response) {
//
//                        }
//                    });
                    break;
                case SHARE_CANCEL:
                    Toast.makeText(contexts, "取消分享", Toast.LENGTH_LONG).show();
                    break;
                case SHARE_FAILURE:
                    Toast.makeText(contexts, "分享失败", Toast.LENGTH_LONG).show();
                    break;
                default:
                    break;
            }
        }
    };
}
