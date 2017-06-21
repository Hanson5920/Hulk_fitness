package com.lxh.userlibrary.user;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.text.format.Formatter;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.jph.takephoto.app.TakePhoto;
import com.jph.takephoto.app.TakePhotoImpl;
import com.jph.takephoto.model.CropOptions;
import com.jph.takephoto.model.InvokeParam;
import com.jph.takephoto.model.TContextWrap;
import com.jph.takephoto.model.TResult;
import com.jph.takephoto.permission.InvokeListener;
import com.jph.takephoto.permission.PermissionManager;
import com.jph.takephoto.permission.TakePhotoInvocationHandler;
import com.lxh.userlibrary.R;
import com.lxh.userlibrary.UserCenter;
import com.lxh.userlibrary.base.BaseFragment;
import com.lxh.userlibrary.base.FragmentFactory;
import com.lxh.userlibrary.constant.Constants;
import com.lxh.userlibrary.entity.LoginUserInfo;
import com.lxh.userlibrary.entity.NewVersion;
//import com.lxh.userlibrary.greendao.LoginUserInfoDao;
import com.lxh.userlibrary.load.ImageLoaderManager;
import com.lxh.userlibrary.manager.DataCleanManager;
import com.lxh.userlibrary.manager.DefaultSharePrefManager;
import com.lxh.userlibrary.manager.PageSwitcher;
import com.lxh.userlibrary.mineInterface.UserInterface;
import com.lxh.userlibrary.manager.UserManage;
import com.lxh.userlibrary.manager.dialog.LoadingDialog;
import com.lxh.userlibrary.manager.dialog.CommonAlertDialog;
import com.lxh.userlibrary.manager.dialog.ShowPopupPhoto;
import com.lxh.userlibrary.manager.update.UpdateUtil;
import com.lxh.userlibrary.message.Message;
import com.lxh.userlibrary.tools.PermissionsChecker;
import com.lxh.userlibrary.utils.DisplayUtil;
import com.lxh.userlibrary.utils.FileUtil;
import com.lxh.userlibrary.utils.FileUtils;
import com.lxh.userlibrary.utils.HttpUtils;
import com.lxh.userlibrary.utils.Photo_State;
import com.lxh.userlibrary.utils.ToastUtil;
import com.lxh.userlibrary.utils.Utils;
import com.lxh.userlibrary.utils.ViewUtil;
import com.lxh.userlibrary.widget.DiaogAnimChoose;
import com.lxh.userlibrary.widget.NormalDialog;
import com.lxh.userlibrary.widget.OnBtnClickL;
import com.lxh.userlibrary.widget.animation.BaseAnimatorSet;
import com.makeramen.roundedimageview.RoundedImageView;
import com.zhy.http.okhttp.callback.FileCallBack;
//import org.greenrobot.greendao.query.QueryBuilder;
import java.io.ByteArrayOutputStream;
import java.io.File;

import okhttp3.Call;

import static com.lxh.userlibrary.message.Message.Type.USER_DATA_PHOTO_CHAANGE;
import static com.lxh.userlibrary.message.Message.Type.USER_DATA_STATE;
import static com.lxh.userlibrary.message.Message.Type.USER_EXIT_SATEA;
import static com.lxh.userlibrary.utils.FileUtil.getFolderSize;

/**
 * Created by lxh on 2016/11/16.
 * QQ-632671653
 */

public class MineFragment extends BaseFragment implements UpdateUtil.ShowUpdateDialog, TakePhoto.TakeResultListener, InvokeListener {


    private static final String TAG = MineFragment.class.getSimpleName();
    private LoadingDialog upPhotoDialog,exitDialog;
    private TextView displayVersion;
    private TextView cacheSize;
    private TextView loginPrompt;
    private TextView userExit;
    private String nickName = "";
    private String headUrl = "";
    private TextView userNickName;
    private RoundedImageView userHeadImg;
    private LoginUserInfo mLoginUser;
    private TakePhoto takePhoto;
    private InvokeParam invokeParam;

    private PermissionsChecker mPermissionsChecker; // 权限检测器
    static final String[] PERMISSIONS = new String[]{
            android.Manifest.permission.READ_EXTERNAL_STORAGE, android.Manifest.permission.CAMERA,
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE
//            ,android.Manifest.permission.MOUNT_UNMOUNT_FILESYSTEMS
    };


    public static MineFragment newInstance() {
        return new MineFragment();
    }

    /**
     * 获取TakePhoto实例
     *
     * @return
     */
    public TakePhoto getTakePhoto() {
        if (takePhoto == null) {
            takePhoto = (TakePhoto) TakePhotoInvocationHandler.of(this).bind(new TakePhotoImpl(this, this));
        }
        return takePhoto;
    }

    @Override
    protected int getlayoutRes() {
        return R.layout.mine_layout;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        getTakePhoto().onCreate(savedInstanceState);
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        getTakePhoto().onSaveInstanceState(outState);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void bindView() {
        super.bindView();
        findViewIcon(R.id.arrow1);
        findViewIcon(R.id.arrow2);
        findViewIcon(R.id.arrow3);

        userHeadImg = findViewAttachOnclick(R.id.user_head_portrait);
        userExit = findViewAttachOnclick(R.id.user_exit);
        userNickName = findViewAttachOnclick(R.id.user_nickname);
        loginPrompt = findView(R.id.login_prompt);

        displayVersion = findView(R.id.display_version);
        displayVersion.setText("V " + FileUtil.getVersion(mContext));
        cacheSize = findView(R.id.cache_size);

        findViewAttachOnclick(R.id.version_update_layout);
        findViewAttachOnclick(R.id.remind_layout);
        findViewAttachOnclick(R.id.clear_cache_layout);
        findViewAttachOnclick(R.id.feedback_layout);
        findViewAttachOnclick(R.id.share_app_layout);
        upPhotoDialog = new LoadingDialog(mActivity,"头像上传中,请稍候...");
        String length = Formatter.formatFileSize(mActivity, getCacheSize());
        cacheSize.setText(length);
        UserCenter.getInstance().getMessagePump().
                broadcastMessage(USER_DATA_STATE);

    }

    @Override
    public void onResume() {
        super.onResume();
        try {
            nickName = DefaultSharePrefManager.getString(Constants.USER_NAME, "");
            headUrl = DefaultSharePrefManager.getString(Constants.USER_LOGO_URL, "");
            UserCenter.getInstance().getMessagePump().
                    broadcastMessage(USER_DATA_STATE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        String uid = DefaultSharePrefManager.getString(Constants.KEY_USER_ID, "");
        int i = v.getId();
        if (i == R.id.user_head_portrait) {
            if (uid.equals("")) {//没有登录
                PageSwitcher.switchToPage(mActivity, FragmentFactory.FRAGMENT_TYPE_USER_LOGIN);
            } else {//选择图片
                new ShowPopupPhoto().showPopupWindow(mActivity, userHeadImg, new My_Photo_stae(), 0);
            }
        } else if (i == R.id.user_nickname) {
            if (!"".equals(uid)) {//登录了
                Bundle bundle = new Bundle();
                bundle.putString("NICKNAME", nickName);
                PageSwitcher.switchToPage(mActivity, FragmentFactory.FRAGMENT_TYPE_USER_DATA_UPDATE, bundle);
            }
        } else if (i == R.id.remind_layout) {
//            PageSwitcher.switchToPage(mActivity, FragmentFactory.FRAGMENT_TYPE_BIRTHDAY_REMINDER);

        } else if (i == R.id.feedback_layout) {
//            PageSwitcher.switchToPage(mActivity, FragmentFactory.FRAGMENT_TYPE_FEEDBACK);

        } else if (i == R.id.share_app_layout) {
//            PageSwitcher.switchToPage(mActivity, FragmentFactory.FRAGMENT_TYPE_FOUND_APP);

        } else if (i == R.id.clear_cache_layout) {
                                cleanCache();//
        } else if (i == R.id.version_update_layout) {
            UpdateUtil.updateAPK(mActivity, this, true);
        } else if (i == R.id.user_exit) {
            UserExit(true, DefaultSharePrefManager.getString(Constants.KEY_USER_ID, ""));
        }
    }



    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PermissionManager.TPermissionType type = PermissionManager.onRequestPermissionsResult(requestCode, permissions, grantResults);
        PermissionManager.handlePermissionsResult(mActivity, type, invokeParam, this);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        getTakePhoto().onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void attachAllMessage() {
        attachMessage(USER_DATA_PHOTO_CHAANGE);
        attachMessage(USER_DATA_STATE);
        attachMessage(USER_EXIT_SATEA);
    }

    @Override
    public void onReceiveMessage(Message message) {
        super.onReceiveMessage(message);
        switch (message.type) {
            case USER_DATA_PHOTO_CHAANGE: {//更新头像
                String headUrl = DefaultSharePrefManager.getString(Constants.USER_LOGO_URL, "");
//                Log.e("111111", "============" + headUrl);
                ImageLoaderManager.displayRoundImageByUrl(mContext, userHeadImg, headUrl, R.drawable.img_head);
                break;
            }
            case USER_DATA_STATE: {//登录状态 处理
                String id = DefaultSharePrefManager.getString(Constants.KEY_USER_ID,"");
                if (id.equals("")) {//没有登录
                    ImageLoaderManager.displayImageByDrawable(mActivity, userHeadImg, R.drawable.img_head);
                    ViewUtil.visible(loginPrompt);
                    ViewUtil.gone(userExit);
                } else {
                    userNickName.setText(nickName + "");
                    ViewUtil.setTextDrawable(mContext, userNickName, R.drawable.icon_eidt);
                    userNickName.setCompoundDrawablePadding(10);
                    ViewUtil.gone(loginPrompt);
                    ViewUtil.visible(userExit);
                    ImageLoaderManager.displayRoundImageByUrl(mActivity, userHeadImg, headUrl,R.drawable.img_head);
                }
                break;
            }
            case USER_EXIT_SATEA://退出状态 处理
//                ImageLoaderManager.displayImageByDrawable(mActivity, userHeadImg, R.drawable.img_me);
                userHeadImg.setImageResource(R.drawable.img_head);
                ViewUtil.visible(loginPrompt);
                ViewUtil.gone(userExit);
                userNickName.setText("登录/注册");
                ViewUtil.setTextDrawableNull(mContext, userNickName);
                break;
        }
    }

    @Override
    public void showUpdateDialog(final NewVersion updateInfo) {
        if (mActivity.isFinishing()) {
            return;
        }
        BaseAnimatorSet bas_in = DiaogAnimChoose.getShowAnim();
        BaseAnimatorSet bas_out = DiaogAnimChoose.getDissmissAnim();

        final NormalDialog dialog = new NormalDialog(mActivity);

        dialog.content(updateInfo.getDesc())//
                .title(getResources().getString(R.string.check_update_title))//
                .titleTextColor(Color.parseColor("#333333"))//
                .style(NormalDialog.STYLE_TWO)//
                .btnNum(2)//
                .titleTextSize(17)//
                .contentTextSize(14)//
                .btnTextSize(14)//
                .btnTextColor(Color.parseColor("#36be63"), Color.parseColor("#36be63"))//
                .btnText("取消", "更新")//
                .showAnim(bas_in)//
                .dismissAnim(bas_out)//
                .show();

        dialog.setOnBtnClickL(
                new OnBtnClickL() {
                    @Override
                    public void onBtnClick() {
                        dialog.dismiss();
                    }
                },
                new OnBtnClickL() {
                    @Override
                    public void onBtnClick() {
                        downloadApk(updateInfo.getApp_url(), "wannianli");
                        dialog.dismiss();
                    }
                });
    }

    @Override
    public void takeSuccess(TResult result) {
//        Log.i(TAG,"takeSuccess：" + result.getImage().getOriginalPath());
        editphoto(result.getImage().getOriginalPath());
    }

    @Override
    public void takeFail(TResult result, String msg) {
        Log.i(TAG, "takeFail:" + msg);
    }

    @Override
    public void takeCancel() {
        Log.i(TAG, getResources().getString(R.string.msg_operation_canceled));
    }

    @Override
    public PermissionManager.TPermissionType invoke(InvokeParam invokeParam) {
        PermissionManager.TPermissionType type = PermissionManager.checkPermission(TContextWrap.of(this), invokeParam.getMethod());
        if (PermissionManager.TPermissionType.WAIT.equals(type)) {
            this.invokeParam = invokeParam;
        }
        return type;
    }

    /**
     * 上传头像窗体
     */
    class My_Photo_stae implements Photo_State {
        @Override
        public void second_item() {//拍照
            File file = new File(Environment.getExternalStorageDirectory(), "/wannianli/" + System.currentTimeMillis() + ".jpg");
            if (!file.getParentFile().exists()) file.getParentFile().mkdirs();
            Uri imageUri = Uri.fromFile(file);
            CropOptions cropOptions = new CropOptions.Builder().setAspectX(1).setAspectY(1).setWithOwnCrop(true).create();
            takePhoto.onPickFromCaptureWithCrop(imageUri, cropOptions);
        }

        @Override
        public void first_item() {//从相册中获取
            File file = new File(Environment.getExternalStorageDirectory(), "/wannianli/" + System.currentTimeMillis() + ".jpg");
            if (!file.getParentFile().exists()) file.getParentFile().mkdirs();
            Uri imageUri = Uri.fromFile(file);
            CropOptions cropOptions = new CropOptions.Builder().setAspectX(1).setAspectY(1).setWithOwnCrop(true).create();
            takePhoto.onPickFromGalleryWithCrop(imageUri, cropOptions);
        }
    }

    /**
     * 头像上传
     */
    public void editphoto(final String filePath) {
        upPhotoDialog.show();
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (null == filePath) {
                    Toast.makeText(mContext, "头像裁剪失败", Toast.LENGTH_SHORT).show();
                    return;
                }
                File file = new File(filePath);
                String fileName = filePath.substring(filePath.lastIndexOf("/") + 1);
                Bitmap bitmap = BitmapFactory.decodeFile(file.getPath());
                String photo = convertIconToString(bitmap);
                String uid = DefaultSharePrefManager.getString(Constants.KEY_USER_ID, "");
                String loginKey = DefaultSharePrefManager.getString(Constants.LOGIN_KEY, "");
                UserManage.UserUploadLogo(fileName,file,uid,"",photo,loginKey, new UserInterface() {
                    @Override
                    public void onError(Call call, Exception e, String msg) {
//                        Log.e("11111", "=====222======" + e.toString());
                        ToastUtil.toastLong(mActivity, "头像上传失败");
                        upPhotoDialog.dismiss();
                    }

                    @Override
                    public void onSucceed(int state, String msg, String data, JSONObject obj) {
//                        Log.e("TAG","====="+obj.toString());
                        upPhotoDialog.dismiss();
                        switch (state) {
                            case 200:
                                try {
                                    JSONObject jo1 = JSON.parseObject(data);
                                    String url = jo1.getString("url");
                                    DefaultSharePrefManager.putString(Constants.USER_LOGO_URL, url);
                                    UserCenter.getInstance().getMessagePump().
                                            broadcastMessage(Message.Type.USER_DATA_PHOTO_CHAANGE);
                                    ToastUtil.toastLong(mActivity, "更换头像成功");
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                break;
                            default:
                                ToastUtil.toastLong(mActivity, msg);
                                break;
                        }
                    }
                });
            }
        }).start();
    }

    /**
     * 图片转成string
     *
     * @param bitmap
     * @return
     */
    public static String convertIconToString(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();// outputstream
        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, baos);
        byte[] icon = baos.toByteArray();// 转为byte数组
        return Base64.encodeToString(icon, Base64.DEFAULT);

    }

    /**
     * 获取缓存大小
     *
     * @return
     */
    private long getCacheSize() {
        long cacheLength = 0L;
        Context context = UserCenter.getContext();
        File cacheDir = context.getExternalCacheDir(); // /storage/emulated/0/android/data/cache
        if (cacheDir == null) {
            cacheDir = context.getCacheDir();
        }
        File imageDiskCache = new File(Utils.getImageCacheDir(mContext));// ImageLoader缓存在硬件上的图片位置
        File internalDir = getActivity().getCacheDir();
        if (cacheDir != null && cacheDir.exists()) {
            cacheLength += getFolderSize(cacheDir);// cacheDir
        }

        if (imageDiskCache != null && imageDiskCache.exists()) {
            cacheLength += getFolderSize(imageDiskCache);
        }
        if (internalDir != null && internalDir.exists()) {
            cacheLength += getFolderSize(internalDir);
        }

        return cacheLength;
    }

    /**
     * 清除缓存
     */
    private void cleanCache() {
        String rightText = getResources().getString(R.string.setting_clean);
        String content = getResources().getString(R.string.clean_cache_immediately);
        CommonAlertDialog clearCacheDialog = new CommonAlertDialog(getActivity(), CommonAlertDialog.DialogType.NORMAL_DIALOG);
        clearCacheDialog.setData(null, content, null, rightText, null);
        clearCacheDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = UserCenter.getContext();
                File cacheDir = context.getExternalCacheDir(); // /storage/emulated/0/android/data/cache
                if (cacheDir == null) {
                    cacheDir = context.getCacheDir();
                }
                File imageDiskCache = new File(Utils.getImageCacheDir(getActivity()));// ImageLoader缓存在硬件上的图片位置
                DataCleanManager.cleanInternalCache(getActivity());// /data/data/com.xxx.xxx/cache

                if (cacheDir != null && cacheDir.isDirectory()) {
                    DataCleanManager.cleanCustomCache(cacheDir.getPath());
                }

                if (imageDiskCache != null && imageDiskCache.isDirectory()) {
                    DataCleanManager.cleanCustomCache(imageDiskCache.getPath());
                }
                ToastUtil.toastShort(getActivity(), getResources().getString(R.string.mzw_set_clean_complete));
                //更新界面
                String length = Formatter.formatFileSize(getActivity(), getCacheSize());
                cacheSize.setText(length);
            }
        });
        clearCacheDialog.show();
    }

    private void downloadApk(String imgUrl, String name) {
        HttpUtils.downloadNewSelf(imgUrl, new FileCallBack(FileUtils.getDownloadApk(), name + ".apk") {
            @Override
            public void onError(Call call, Exception e) {
                ToastUtil.toastLong(mContext, "app下载失败，请稍后重试！");
            }

            @Override
            public void onResponse(File response) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setDataAndType(Uri.fromFile(response),
                        "application/vnd.android.package-archive");
                startActivity(intent);
            }

            @Override
            public void inProgress(float progress, long total) {
            }
        });
    }

    /**
     * 用户 退出登录
     *
     * @param isShowToast 是否显示 toast ; true = 显示  ， false=不显示
     * @param uid         用户id
     */
    public void UserExit(final boolean isShowToast, final String uid) {
        if (isShowToast) {
            String showText = mActivity.getResources().getString(R.string.in_exit);
            exitDialog = new LoadingDialog(mActivity, showText);
            exitDialog.show();
        }

        UserManage.UserLogout(uid, new UserInterface() {
            @Override
            public void onError(Call call, Exception e, String msg) {
                if (isShowToast) {
                    DisplayUtil.showToast(mActivity, msg);
                    exitDialog.dismiss();
                }
            }

            @Override
            public void onSucceed(int state, String msg, String data, JSONObject obj) {
                exitDialog.dismiss();
                switch (state) {
                    case 200:
                        if (isShowToast) {
                            DisplayUtil.showToast(mActivity, msg);
                        }
                        DefaultSharePrefManager.putString(Constants.KEY_USER_ID, "");
                        DefaultSharePrefManager.putString(Constants.USER_LOGO_URL,"");
                        DefaultSharePrefManager.putString(Constants.USER_NAME,"");
                        DefaultSharePrefManager.putString(Constants.LOGIN_KEY,"");
                        UserCenter.getInstance().getMessagePump().
                                broadcastMessage(USER_EXIT_SATEA);
                        break;
                    default:
                        if (isShowToast) {
                            DisplayUtil.showToast(mActivity, msg);
                        }
                        break;
                }
            }
        });
    }


}
