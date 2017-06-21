package com.gz.hkjs.app.ui.main.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.IdRes;
import android.text.InputFilter;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.TextAppearanceSpan;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.gz.hkjs.app.R;
import com.gz.hkjs.app.bean.InformationBean;
import com.gz.hkjs.app.util.ChangeAddressPopwindow;
import com.gz.hkjs.app.util.CircleImageView;
import com.gz.hkjs.app.util.permission.PermissionCallBack;
import com.gz.hkjs.app.util.permission.PermissionUtil;
import com.gz.hkjs.app.util.popwindow.ShowPopupPhoto;
import com.gz.hkjs.app.widget.CustomDatePicker;
import com.gz.hkjs.app.widget.Photo_State;
import com.jaydenxiao.common.base.BaseActivity;
import com.jaydenxiao.common.commonutils.ImageLoaderUtils;
import com.jaydenxiao.common.commonwidget.MineTitleBar;
import com.lxh.userlibrary.UserCenter;
import com.lxh.userlibrary.constant.Constants;
import com.lxh.userlibrary.dialog.LoadingDialog;
import com.lxh.userlibrary.entity.LoginUserInfo;
import com.lxh.userlibrary.manager.DefaultSharePrefManager;
import com.lxh.userlibrary.manager.UserManage;
import com.lxh.userlibrary.message.Message;
import com.lxh.userlibrary.message.MessageCallback;
import com.lxh.userlibrary.message.MessageHelper;
import com.lxh.userlibrary.mineInterface.UserInterface;
import com.lxh.userlibrary.utils.ToastUtil;
import com.orhanobut.logger.Logger;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;

import static com.gz.hkjs.app.ui.main.fragment.MineMainFragment.IMAGE_COMPLETE;
import static com.gz.hkjs.app.ui.main.fragment.MineMainFragment.PHOTOTAKE;
import static com.gz.hkjs.app.ui.main.fragment.MineMainFragment.PHOTOZOOM;
import static com.lxh.userlibrary.message.Message.Type.USER_DATA_PHOTO_CHAANGE;
import static com.lxh.userlibrary.message.Message.Type.USER_DATA_STATE;

/**
 * 资料 lwy
 */

public class InformationActivity extends BaseActivity implements MessageCallback {


    @BindView(R.id.mtb_information)
    MineTitleBar mineTitleBar;
    @BindView(R.id.civ_information_user)
    CircleImageView civInformationUser;
    @BindView(R.id.tv_information_user_name)
    EditText tvInformationUserName;
    @BindView(R.id.rl_mine_name)
    RelativeLayout rlMineName;
    @BindView(R.id.rb_sex_nan)
    RadioButton rbSexNan;
    @BindView(R.id.rb_sex_nv)
    RadioButton rbSexNv;
    @BindView(R.id.tv_mine_weight)
    TextView tvMineWeight;
    @BindView(R.id.tv_information_user_weight)
    EditText tvInformationUserWeight;
    @BindView(R.id.tv_mine_height)
    TextView tvMineHeight;
    @BindView(R.id.tv_information_user_height)
    EditText tvInformationUserHeight;
    @BindView(R.id.tv_information_user_data)
    TextView tvInformationUserData;
    @BindView(R.id.tv_information_user_city)
    TextView tvInformationUserCity;
    @BindView(R.id.tv_information_save)
    TextView tvInformationSave;
    @BindView(R.id.rg_sex)
    RadioGroup rgSex;

    private String photoSaveName = "temp_photo.jpg";//照片的名字
    private File imageFile;
    private int maxLength = 20;//最大输入字符数
    private String uid = "";
    private String loginKey = "";
    private String birthDay = "";
    private String userName = "";
    private String userCity = "";
    private String userHeight = "";
    private String userWeight = "";
    private String more = "";
    private int userSex;
    private String uploadImageUrl = "";

    private MessageHelper mMessageHelper;

    private CustomDatePicker customDatePicker;//日期选择控件

    @Override
    public int getLayoutId() {
        return R.layout.mine_information;
    }

    @Override
    public void initPresenter() {

    }

    @Override
    public void initView() {

        uid = UserCenter.getUid();
        loginKey = DefaultSharePrefManager.getString(Constants.LOGIN_KEY, "");

        UserCenter.getUserInfo(uid, loginKey, new UserInterface() {//获取用户信息
            @Override
            public void onError(Call call, Exception e, String msg) {
                Logger.e("获取失败222:" + msg);
            }

            @Override
            public void onSucceed(int state, String msg, String data, JSONObject obj) {
                if (state == 200) {
//                    refreshLayout();
                    Logger.e("obj" + obj.toJSONString());
                    String obj2 = obj.getJSONArray("data").get(0).toString();
                    LoginUserInfo info = JSON.parseObject(obj2, LoginUserInfo.class);

                    refreshLayout(info.getLogo(), info.getName(), Integer.parseInt(info.getSex()), info.getMore(), info.getBirthday());

                }
            }
        });
        mMessageHelper = new MessageHelper();
        mMessageHelper.setMessageCallback(this);
        mMessageHelper.attachMessage(USER_DATA_PHOTO_CHAANGE);
        mMessageHelper.attachMessage(USER_DATA_STATE);
        mMessageHelper.registerMessages();//注册MessageHelper

        mineTitleBar.setLeftBack(true);
        mineTitleBar.setOnLeftImagListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mineTitleBar.setTitleText(getString(R.string.my_information));

        //身高字体颜色
        String mWeight = "身高(cm)";
        SpannableString spannableString = new SpannableString(mWeight);
        spannableString.setSpan(new TextAppearanceSpan(this, R.style.infor_text_style), 0, 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(new TextAppearanceSpan(this, R.style.mine_information_text_unit), 2, mWeight.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        tvMineHeight.setText(spannableString, TextView.BufferType.SPANNABLE);
        //体重字体颜色
        String mHeight = "体重(kg)";
        SpannableString spannableString1 = new SpannableString(mHeight);
        spannableString1.setSpan(new TextAppearanceSpan(this, R.style.infor_text_style), 0, 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString1.setSpan(new TextAppearanceSpan(this, R.style.mine_information_text_unit), 2, mHeight.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        tvMineWeight.setText(spannableString1, TextView.BufferType.SPANNABLE);
        tvInformationUserName.setFilters(new InputFilter[]{
                new InputFilter() {
                    @Override
                    public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                        for (int i = start; i < end; i++) {
                            if (!Character.isLetterOrDigit(source.charAt(i)) && Character.toString(source.charAt(i)).equals(("_"))) {
                                return "";
                            }
                        }
                        return null;
                    }
                }
        });
        InputFilter[] fArray = new InputFilter[1];
        fArray[0] = new InputFilter.LengthFilter(maxLength);

        tvInformationUserName.setFilters(fArray);

        rgSex.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                switch (checkedId) {
                    case R.id.rb_sex_nan:
                        userSex = 1;
                        break;
                    case R.id.rb_sex_nv:
                        userSex = 0;
                        break;

                }
            }
        });
        initDatePicker();
    }

    private void initDatePicker() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.CHINA);
        String now = sdf.format(new Date());
//        tvInformationUserData.setText(now.split(" ")[0]);

        customDatePicker = new CustomDatePicker(this, new CustomDatePicker.ResultHandler() {
            @Override
            public void handle(String time) { // 回调接口，获得选中的时间
                tvInformationUserData.setText(time.split(" ")[0]);
            }
        }, "1000-01-01 00:00", now); // 初始化日期格式请用：yyyy-MM-dd HH:mm，否则不能正常运行
        customDatePicker.showSpecificTime(false); // 不显示时和分
        customDatePicker.setIsLoop(false); // 不允许循环滚动


    }

    private void refreshLayout(String imgUrl, String nickName, int sex, String more, String birthDay) {

        ImageLoaderUtils.display(this, civInformationUser, imgUrl);
        tvInformationUserName.setText(nickName);
//        tvInformationUserName.setSelection(tvInformationUserName.getText().length());
        if (sex == 1) {
            rbSexNan.setChecked(true);

        } else {
            rbSexNv.setChecked(true);
        }
        InformationBean ib = JSON.parseObject(more, InformationBean.class);
        if(ib!=null) {
            userWeight = ib.getWeight();
            userHeight = ib.getHeight();
            tvInformationUserWeight.setText(TextUtils.isEmpty(userWeight) ? "" : userWeight);
            tvInformationUserHeight.setText(TextUtils.isEmpty(userHeight) ? "" : userHeight);
            if(!TextUtils.isEmpty(ib.getCity()))
                tvInformationUserCity.setText(ib.getCity());

//            tvInformationUserWeight.setSelection(tvInformationUserWeight.getText().length());
//            tvInformationUserHeight.setSelection(tvInformationUserHeight.getText().length());
        }
        tvInformationUserData.setText(birthDay);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }


    @OnClick({R.id.civ_information_user, R.id.tv_information_user_data, R.id.tv_information_user_city, R.id.tv_information_save})
    public void onViewClicked(final View view) {
        switch (view.getId()) {
            case R.id.civ_information_user:
                PermissionUtil.getCameraPermission(this, new PermissionCallBack() {
                    @Override
                    public void PermissionSuccess() {
                        new ShowPopupPhoto(InformationActivity.this).setConfig(0, new Photo_State() {
                            @Override
                            public void first_item() {//相册
                                Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                                startActivityForResult(i, PHOTOZOOM);
                            }

                            @Override
                            public void second_item() {//拍照
                                camera(view);

                            }
                        }).showPopupWindow(civInformationUser);
                    }

                    @Override
                    public void PermissionFail() {

                    }
                });


                break;

            case R.id.tv_information_user_data:
                customDatePicker.show(tvInformationUserData.getText().toString());

                break;
            case R.id.tv_information_user_city:

                ChangeAddressPopwindow mChangeAddressPopwindow = new ChangeAddressPopwindow(InformationActivity.this);

                mChangeAddressPopwindow.setAddress("广东", "深圳");
                mChangeAddressPopwindow.showAtLocation(tvInformationUserCity, Gravity.BOTTOM, 0, 0);
                mChangeAddressPopwindow
                        .setAddresskListener(new ChangeAddressPopwindow.OnAddressCListener() {

                            @Override
                            public void onClick(String province, String city) {
                                // TODO Auto-generated method stub

                                tvInformationUserCity.setText(province + city);
                            }
                        });
                break;
            case R.id.tv_information_save:
                userName = tvInformationUserName.getText().toString();
                birthDay = tvInformationUserData.getText().toString();
                userCity = tvInformationUserCity.getText().toString();
                if (!tvInformationUserHeight.getText().toString().equals(""))
                    userHeight = tvInformationUserHeight.getText().toString();
                if (!tvInformationUserWeight.getText().toString().equals(""))
                    userWeight = tvInformationUserWeight.getText().toString();

                InformationBean ib = new InformationBean();
                ib.setCity(userCity);
                ib.setHeight(userHeight);
                ib.setWeight(userWeight);
                more = JSON.toJSONString(ib);

                if (!userName.equals("")) {
                    String saveInfor = "修改中，请稍后";
                    final LoadingDialog loadingDialog = new LoadingDialog(mContext, saveInfor);
                    loadingDialog.show();
                    Log.e("husong", "------cc------>>birthDay="+birthDay);
                    UserCenter.editUserInfo(uid, loginKey, "", userName, String.valueOf(userSex), birthDay, "", "", "", more, new UserInterface() {
                        @Override
                        public void onError(Call call, Exception e, String msg) {
                            loadingDialog.dismiss();
                            ToastUtil.toastLong(InformationActivity.this, "修改失败，请稍后再试");
                        }

                        @Override
                        public void onSucceed(int state, String msg, String data, JSONObject obj) {
                            loadingDialog.dismiss();
                            if (state == 200) {
                                ToastUtil.toastLong(InformationActivity.this, "修改成功");
                            }
                            finish();
                        }
                    });
                } else {
                    ToastUtil.toastLong(InformationActivity.this, "昵称不能为空");
                }
                break;
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        mMessageHelper.unRegisterMessages();
        mMessageHelper.clearMessages();
    }

    /**
     * 从相机获取
     */
    public void camera(View view) {
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
        // 开启一个带有返回值的Activity，请求码为PHOTOTAKE
        startActivityForResult(intent, PHOTOTAKE);
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
        // 开启一个带有返回值的Activity，请求码为IMAGE_COMPLETE
        startActivityForResult(intent, IMAGE_COMPLETE);
    }

    /*
     * 判断sdcard是否被挂载
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
     * 图片选择或拍照结果
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
                    ToastUtil.toastLong(InformationActivity.this, "未找到存储卡，无法存储照片！");
                }
                break;
            default:
                break;
        }
        if (requestCode == IMAGE_COMPLETE) {
            if (data != null) {
                Bitmap bitmap = data.getParcelableExtra("data");
                imageFile = saveBitmapFile(bitmap);
                this.civInformationUser.setImageBitmap(bitmap);
            }
            try {
                Logger.e("完成剪切");
                // 将临时文件删除

            } catch (Exception e) {
                e.printStackTrace();
            }
            uploadImageUrl = String.valueOf(imageFile);
            uploadImage(uploadImageUrl);
        }
    }

    /**
     * 头像图片上传
     *
     * @param filePath
     */
    private void uploadImage(String filePath) {
        final LoadingDialog loadingDialog = new LoadingDialog(mContext, "头像上传中，请稍候");
        loadingDialog.show();
        File file = new File(filePath);
        String fileName = filePath.substring(filePath.lastIndexOf("/") + 1);
        Bitmap bitmap = BitmapFactory.decodeFile(file.getPath());
        String photo = convertIconToString(bitmap);

        UserManage.UserUploadLogo(fileName, file, uid, "", photo, loginKey, new UserInterface() {
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

    /**
     * 保存截取的图片
     * @param bitmap
     * @return
     */
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


    @Override
    public void onReceiveMessage(Message message) {

    }
}
