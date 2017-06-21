package com.lxh.userlibrary.utils;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;
import android.text.TextUtils;


//import com.lxh.userlibrary.UserApplication;
//import com.lxh.userlibrary.constant.Constants;
//import com.lxh.userlibrary.entity.LoginUserInfo;
//import com.lxh.userlibrary.greendao.LoginUserInfoDao;
//import com.lxh.userlibrary.manager.DefaultSharePrefManager;
//
//import org.greenrobot.greendao.query.QueryBuilder;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Locale;

/**
 * 常用工具类整理
 * Author:Eric_chan
 */
public class Utils {

    /** SD卡三种状 */
    public static enum MountStatuds {
        SD_CARD_AVAILABLE, SD_CARD_NOT_AVAILABLE, SD_CARD_SPACE_NOT_ENOUGH
    }

    /** 预设SD卡空间 (单位M) */
    public static final long CACHE_SIZE = 100;
    public static final int MB = 1024 * 1024;
    public static final String SDCARD_PATH = ("Android" + File.separator + "data" + File.separator).intern();

    public static final Locale[] LANGUAGE_CATEGORY = { Locale.getDefault(), Locale.CHINESE, Locale.ENGLISH,
            Locale.KOREAN, Locale.TAIWAN, };

    /** 默认为可用状 */
    public static MountStatuds SDCardStatus = MountStatuds.SD_CARD_AVAILABLE;

    private static ThreadLocal<byte[]> threadSafeByteBuf = null;

    public static byte[] getThreadSafeByteBuffer() {
        if (threadSafeByteBuf == null) {
            threadSafeByteBuf = new ThreadLocal<byte[]>();
        }

        byte[] buf = threadSafeByteBuf.get();

        if (buf == null) {
            buf = new byte[1024 * 4]; // 4kb
            threadSafeByteBuf.set(buf);
        }

        return buf;
    }


    /**
     * 关闭IO流
     * @param obj
     */
    public static void closeCloseable(Closeable obj) {
        try {
            // 修复小米MI2的JarFile没有实现Closeable导致崩溃问题
            if (obj != null && obj instanceof Closeable)
                obj.close();

        } catch (IOException e) {
//            XUtilLog.log_e(e.toString());
        }
    }

    // 产生userAgent
    public static String gennerateUserAgent(Context context) {
        StringBuilder sb = new StringBuilder();

        sb.append("Mozilla/5.0 (Linux; U; Android");
        sb.append(Build.VERSION.RELEASE);
        sb.append("; ");
        sb.append(Locale.getDefault().toString());

        String model = Build.MODEL;
        if (!TextUtils.isEmpty(model)) {
            sb.append("; ");
            sb.append(model);
        }

        String buildId = Build.ID;
        if (!TextUtils.isEmpty(buildId)) {
            sb.append("; Build/");
            sb.append(buildId);
        }

        sb.append(") ");

        int versionCode = 0;
        String packageName = context.getPackageName();
        try {
            PackageManager manager = context.getPackageManager();
            PackageInfo packageInfo = manager.getPackageInfo(packageName, 0);
            versionCode = packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            // Keep the versionCode 0 as default.
        }

        sb.append(packageName);
        sb.append("/");
        sb.append(versionCode);

        sb.append("; ");
        return sb.toString();
    }

//    public static void closeHttpEntity(HttpEntity en) {
//        if (en != null) {
//            try {
//                en.consumeContent();
//            } catch (IOException e) {
//                // e.printStackTrace();
//            }
//        }
//    }


    public static String getImageCacheDir(Context context) {
        File f = new File(getRootPath(context), "image".intern());
        if (!f.exists()) {
            f.mkdirs();
        }
        return f.getPath();
    }

    /** root 路径 */
    public static String getRootPath(Context context) {
        StringBuilder sb = new StringBuilder();

        SDCardStatus = getSDCardStatus();
        switch (SDCardStatus) {
            case SD_CARD_AVAILABLE:
            case SD_CARD_SPACE_NOT_ENOUGH:
                sb.append(Environment.getExternalStorageDirectory().getPath()).append(File.separator).append(SDCARD_PATH)
                        .append(context.getPackageName());
                break;
            case SD_CARD_NOT_AVAILABLE:
                sb.append(context.getCacheDir().getPath());
                break;
        }
        return sb.toString();
    }

    public static MountStatuds getSDCardStatus() {
        MountStatuds status;
        String sdState = Environment.getExternalStorageState();
        if (sdState.equals(Environment.MEDIA_MOUNTED)) {
            File sdcardDir = Environment.getExternalStorageDirectory();
            StatFs sf = new StatFs(sdcardDir.getPath());
            long availCount = sf.getAvailableBlocks();
            long blockSize = sf.getBlockSize();
            long availSize = availCount * blockSize / MB;
            /** 100M内存空间大小 */
            if (availSize < CACHE_SIZE) {
                /** TODO 是否提示用户空间不够 */
                status = MountStatuds.SD_CARD_SPACE_NOT_ENOUGH;
            } else {
                status = MountStatuds.SD_CARD_AVAILABLE;
            }
        } else {
            status = MountStatuds.SD_CARD_NOT_AVAILABLE;
        }
        return status;
    }


    public static void installSelf(Context context, File apkFile) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setDataAndType(Uri.fromFile(apkFile), "application/vnd.android.package-archive");
        context.startActivity(intent);

    }

    private static final DecimalFormat DF = new DecimalFormat("0.00");

    public static String getDownloadPerSize(long finished, long total) {
        return DF.format((float) finished / (1024 * 1024)) + "M/" + DF.format((float) total / (1024 * 1024)) + "M";
    }

//    public static LoginUserInfo getLoginUser() {
//        String uid = DefaultSharePrefManager.getString(Constants.KEY_USER_ID, null);
//        if (TextUtils.isEmpty(uid)) return null;
//        LoginUserInfoDao loginDao = UserApplication.getInstance().getDaoSession().getLoginUserInfoDao();
//        QueryBuilder<LoginUserInfo> qb = loginDao.queryBuilder();
//        List<LoginUserInfo> userInfos = qb.list();
//        if (userInfos != null) {
//            for (LoginUserInfo user : userInfos) {
//                if (TextUtils.equals(uid, user.getUid()))
//                    return user;
//            }
//        }
//        return null;
//    }
}