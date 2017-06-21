package com.gz.hkjs.app.ui.main.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.ContextCompat;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.ggx.ruler_lib.RulerView;
import com.ggx.ruler_lib.RulerView2;
import com.gz.hkjs.app.R;
import com.gz.hkjs.app.util.CacheActivity;
import com.gz.hkjs.app.util.permission.PermissionCallBack;
import com.gz.hkjs.app.util.permission.PermissionUtil;
import com.gz.hkjs.app.util.popwindow.ShowPopupPhoto;
import com.gz.hkjs.app.widget.Photo_State;
import com.jaydenxiao.common.base.BaseActivity;
import com.jaydenxiao.common.commonwidget.StatusBarCompat;
import com.lxh.userlibrary.UserCenter;
import com.lxh.userlibrary.constant.Constants;
import com.lxh.userlibrary.dialog.LoadingDialog;
import com.lxh.userlibrary.manager.DefaultSharePrefManager;
import com.lxh.userlibrary.manager.UserManage;
import com.lxh.userlibrary.mineInterface.UserInterface;
import com.lxh.userlibrary.utils.ToastUtil;
import com.orhanobut.logger.Logger;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import butterknife.BindView;
import okhttp3.Call;

import static com.gz.hkjs.app.ui.main.fragment.MineMainFragment.IMAGE_COMPLETE;
import static com.gz.hkjs.app.ui.main.fragment.MineMainFragment.PHOTOTAKE;
import static com.gz.hkjs.app.ui.main.fragment.MineMainFragment.PHOTOZOOM;


/**
 * Created by Administrator on 2017/4/13.
 */

public class JibenInformitionFour extends BaseActivity {


    @BindView(R.id.bt_to_train)
    Button btToTrain;
    @BindView(R.id.tv_height_digit)
    TextView tvHeightDigit;
    @BindView(R.id.rv_height)
    RulerView rvHeight;
    @BindView(R.id.tv_weight_digit)
    TextView tvWeightDigit;
    @BindView(R.id.rv_weight)
    RulerView2 rvWeight;
    @BindView(R.id.tv_skip)
    TextView tvSkip;
    @BindView(R.id.iv_img_change)
    ImageView ivImgChange;
    @BindView(R.id.et_name_initial)
    EditText etNameInitial;

    private File imageFile;
    private String uid = "";
    private String loginkey = "";
    private String name = "";
    private String weight = "";
    private String more = "";
    private String height = "";
    private String photoSaveName = "temp_photo.jpg";
    private String uploadImageUrl = "";

    /**
     * 入口
     *
     * @param activity
     */
    public static void startAction(Activity activity) {
        Intent intent = new Intent(activity, JibenInformitionFour.class);
        activity.startActivity(intent);
        activity.overridePendingTransition(R.anim.fade_in,
                com.jaydenxiao.common.R.anim.fade_out);
    }

    /**
     * 着色状态栏（4.4以上系统有效）
     */
    protected void SetStatusBarColor() {
        StatusBarCompat.setStatusBarColor(this, ContextCompat.getColor(this, R.color.start_color));
    }

    /**
     * 着色状态栏（4.4以上系统有效）
     */
    protected void SetStatusBarColor(int color) {
        StatusBarCompat.setStatusBarColor(this, color);
    }

    /**
     * 沉浸状态栏（4.4以上系统有效）
     */
    protected void SetTranslanteBar() {
        StatusBarCompat.translucentStatusBar(this);
    }


    @Override
    public int getLayoutId() {
        return R.layout.activity_jibenxinxi_four;
    }


    @Override
    public void initPresenter() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!CacheActivity.activityList.contains(JibenInformitionFour.this)) {
            CacheActivity.addActivity(JibenInformitionFour.this);
        }
    }

    @Override
    public void initView() {

        btToTrain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uid = DefaultSharePrefManager.getString(Constants.KEY_USER_ID, "");
                loginkey = DefaultSharePrefManager.getString(Constants.LOGIN_KEY, "");
                name = etNameInitial.getText().toString();
                if (!tvHeightDigit.getText().toString().equals(""))
                    height = tvHeightDigit.getText().toString();
                if (!tvWeightDigit.getText().toString().equals(""))
                    weight = tvWeightDigit.getText().toString();

                more = "height:" + height + ",weight:" + weight + ",";
                UserCenter.editUserInfo(uid, loginkey, "", name, "", "", "", "", "", more, new UserInterface() {
                    @Override
                    public void onError(Call call, Exception e, String msg) {
                        Logger.e("完善用户资料失败 msg = " + msg);
                    }

                    @Override
                    public void onSucceed(int state, String msg, String data, JSONObject obj) {
                        if (state == 200) {
                            Logger.e("完善用户资料成功 obj = "+obj);
                        }
                        finish();
                        CacheActivity.finishSingleActivityByClass(JibenInformitionOne.class);
                        CacheActivity.finishSingleActivityByClass(JibenInformitionTwo.class);
                        CacheActivity.finishSingleActivityByClass(JibenInformitionThree.class);
                        CacheActivity.finishSingleActivityByClass(RegActivity.class);
//                        LoginActivity.startAction(JibenInformitionFour.this);
                    }
                });
            }
        });


        rvHeight.setCallback(new RulerView.RulerCallback() {
            @Override
            public void resultNum(int num) {
                tvHeightDigit.setText("" + num);
            }
        });

        rvWeight.setCallback(new RulerView2.RulerCallback() {
            @Override
            public void resultNum(int num) {
                tvWeightDigit.setText("" + num);
            }
        });
        tvSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.startAction(JibenInformitionFour.this);
            }
        });
        ivImgChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                PermissionUtil.getCameraPermission(JibenInformitionFour.this, new PermissionCallBack() {
                    @Override
                    public void PermissionSuccess() {
                        new ShowPopupPhoto(JibenInformitionFour.this).setConfig(0, new Photo_State() {
                            @Override
                            public void first_item() {//相册
                                Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                                startActivityForResult(i, PHOTOZOOM);
                            }

                            @Override
                            public void second_item() {//拍照
                                camera(v);

                            }
                        }).showPopupWindow(ivImgChange);

                    }

                    @Override
                    public void PermissionFail() {

                    }
                });
            }
        });
    }

    /**
     * 激活相机
     *
     * @param view
     */
    private void camera(View view) {
        // 激活相机
        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
        // 判断存储卡是否可以用，可用进行存储
        if (hasSdcard()) {
            imageFile = new File(Environment.getExternalStorageDirectory(),
                    photoSaveName);
            // 从文件中创建uri
            Uri uri = Uri.fromFile(imageFile);
            Logger.i("uri = " + uri);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        }
        // 开启一个带有返回值的Activity，请求码为PHOTO_REQUEST_CAREMA
        startActivityForResult(intent, PHOTOTAKE);
    }


    /**
     * 判断sdcard是否可用
     */
    private boolean hasSdcard() {
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 图片结果
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) {
            return;
        }
        switch (requestCode) {
            case PHOTOZOOM://相册

                if (data != null) {
                    // 得到图片的全路径
                    Uri uri = data.getData();
//                    imageFile = new File(uri.getPath());
                    crop(uri);
                }
                break;
            case PHOTOTAKE://拍照
                if (hasSdcard()) {
                    crop(Uri.fromFile(imageFile));
                } else {
                    ToastUtil.toastShort(JibenInformitionFour.this,"未找到存储卡，无法存储照片！");
                }
                break;
            default:
                break;
        }
        if (requestCode == IMAGE_COMPLETE) {
            if (data != null) {
                Bitmap bitmap = data.getParcelableExtra("data");
                imageFile = saveBitmapFile(bitmap);
                this.ivImgChange.setImageBitmap(bitmap);
            }
            try {
                Logger.e("完成剪切");
                // 将临时文件删除

            } catch (Exception e) {
                e.printStackTrace();
            }
//            final String filePath = imageFile.getPath();
            uploadImageUrl = String.valueOf(imageFile);
//            uploadImageUrl = mObject + System.currentTimeMillis() + ".jpg";
            Logger.e("uploadImageUrl" + uploadImageUrl);
            uploadImage(uploadImageUrl);
        }
    }

    private void uploadImage(String filePath) {
        uid = DefaultSharePrefManager.getString(Constants.KEY_USER_ID, "");
        loginkey = DefaultSharePrefManager.getString(Constants.LOGIN_KEY, "");
        final LoadingDialog loadingDialog = new LoadingDialog(mContext, "头像上传中，请稍候");
        loadingDialog.show();
        File file = new File(filePath);
        String fileName = filePath.substring(filePath.lastIndexOf("/") + 1);
        Bitmap bitmap = BitmapFactory.decodeFile(file.getPath());
        String photo = convertIconToString(bitmap);

        UserManage.UserUploadLogo(fileName, file, uid, "", photo, loginkey, new UserInterface() {
            @Override
            public void onError(Call call, Exception e, String msg) {
                loadingDialog.dismiss();
                Logger.e("上传头像失败--msg ：" + msg);
            }

            @Override
            public void onSucceed(int state, String msg, String data, JSONObject obj) {
                loadingDialog.dismiss();
                if (state == 200) {
                    Logger.e("上传头像成功--msg ：" + msg);
                    Logger.e("obj" + obj);
                }

            }
        });
    }

    /**
     * 图片转成string
     *
     * @param bitmap
     * @return
     */
    public String convertIconToString(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();// outputstream
        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, baos);
        byte[] icon = baos.toByteArray();// 转为byte数组
        return Base64.encodeToString(icon, Base64.DEFAULT);

    }

    public File saveBitmapFile(Bitmap bitmap) {
        File file = new File(Environment.getExternalStorageDirectory(),
                photoSaveName);//将要保存图片的路径
        try {
            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
            bos.flush();
            bos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file;
    }

    /**
     * 剪切图片
     */
    private void crop(Uri uri) {
        // 裁剪图片意图
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
        // 裁剪框的比例，1：1
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // 裁剪后输出图片的尺寸大小
        intent.putExtra("outputX", 250);
        intent.putExtra("outputY", 250);
        intent.putExtra("outputFormat", "JPEG");// 图片格式
        intent.putExtra("noFaceDetection", true);// 取消人脸识别
        intent.putExtra("return-data", true);
        // 开启一个带有返回值的Activity，请求码为PHOTO_REQUEST_CUT
        startActivityForResult(intent, IMAGE_COMPLETE);
    }

}
