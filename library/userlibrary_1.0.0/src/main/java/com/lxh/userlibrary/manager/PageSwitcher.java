package com.lxh.userlibrary.manager;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.lxh.userlibrary.activity.SubActivity;


/**
 * Created with Android Studio.
 * Authour：Eric_chan
 * Date：2016/5/16 14:25
 * Des：界面跳转管理器
 */
public class PageSwitcher {
    public final static String INTENT_EXTRA_FRAGMENT_TYPE = "fragment_type";
    public final static String INTENT_EXTRA_FRAGMENT_ARGS = "args";
    public final static String BUNDLE_FRAGMENT_CACHE = "cache";
    public final static String BUNDLE_FRAGMENT_ANIM = "anim";

    public static void switchToPage(Context context, int fragmentType, Bundle bundle, int intentFlag){
        Intent intent = new Intent(context, SubActivity.class);
        if (intentFlag != 0) {
            intent.setFlags(intentFlag);
        }
        intent.putExtra(INTENT_EXTRA_FRAGMENT_TYPE, fragmentType);
        if (bundle != null) {
            intent.putExtra(INTENT_EXTRA_FRAGMENT_ARGS, bundle);
        }
        context.startActivity(intent);
    }

    public static void switchToPage(Context context, int fragmentType){
        switchToPage(context, fragmentType, null, Intent.FLAG_ACTIVITY_NEW_TASK);
    }

    public static void switchToPage(Context context, int fragmentType, Bundle bundle) {
        switchToPage(context, fragmentType, bundle, Intent.FLAG_ACTIVITY_NEW_TASK);
    }

    /**
     * 跳转至分类界面
     * @param context
     * @param info
     */
//    public static void siwtchToCategory(Context context,MenuBean info){
//        Bundle args = new Bundle();
//        args.putParcelable("MenuBean", Parcels.wrap(info));
//        PageSwitcher.switchToPage(context, FragmentFactory.FRAGMENT_TYPE_CATEGORY, args);
//    }


}
