package com.gz.hkjs.app.ui.main.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.gz.hkjs.app.R;
import com.gz.hkjs.app.app.AppApplication;
import com.gz.hkjs.app.bean.FeedbackData;
import com.gz.hkjs.app.parameter.JMClassAdvice;
import com.gz.hkjs.app.ui.main.contract.FeedBackModelContract;
import com.gz.hkjs.app.ui.main.model.FeedBackModel;
import com.gz.hkjs.app.ui.main.presenter.FeedBackPresenter;
import com.gz.hkjs.app.util.UpLoadingManager;
import com.gz.hkjs.app.util.XUtilNet;
import com.gz.hkjs.app.util.permission.PermissionCallBack;
import com.gz.hkjs.app.util.permission.PermissionUtil;
import com.gz.hkjs.app.util.popwindow.ShowPopupPhoto;
import com.gz.hkjs.app.widget.Photo_State;
import com.jaydenxiao.common.base.BaseActivity;
import com.jaydenxiao.common.commonwidget.MineTitleBar;
import com.lxh.userlibrary.dialog.LoadingDialog;
import com.lxh.userlibrary.utils.ToastUtil;
import com.orhanobut.logger.Logger;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.gz.hkjs.app.ui.main.fragment.MineMainFragment.IMAGE_COMPLETE;
import static com.gz.hkjs.app.ui.main.fragment.MineMainFragment.PHOTOTAKE;
import static com.gz.hkjs.app.ui.main.fragment.MineMainFragment.PHOTOZOOM;

/**
 * 意见反馈 lwy
 */

public class FeedbackActivity extends BaseActivity<FeedBackPresenter, FeedBackModel> implements FeedBackModelContract.View {


    @BindView(R.id.mtb_feedback)
    MineTitleBar mineTitleBar;
    @BindView(R.id.tv_feedback)
    TextView tvFeedback;
    @BindView(R.id.et_feedback_content)
    EditText etFeedbackContent;
    @BindView(R.id.iv_feedback_img)
    ImageView ivFeedbackImg;
    @BindView(R.id.tv_feedback_phone)
    TextView tvFeedbackPhone;
    @BindView(R.id.et_qq_email)
    EditText etQqEmail;

    //Data
    private String photoSaveName = "temp_photo.jpg";//照片的名字
    private File imageFile;
    private String feedbackContent = "";//反馈内容
    private String feedbackContactWay = "";//联系方式
    private String uploadImageUrl = "";
    private String mObject = "jubao/";
    private LoadingDialog loadingDialog;

    @Override
    public int getLayoutId() {
        return R.layout.mine_feedback;
    }

    @Override
    public void initPresenter() {
        mPresenter.setVM(this, mModel);
    }


    @Override
    public void initView() {
        mineTitleBar.setLeftBack(true);
        mineTitleBar.setOnLeftImagListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mineTitleBar.setRightcommit(true);
        mineTitleBar.setTtileRightText(getString(R.string.my_feedback_up));
        mineTitleBar.setTitleText(getString(R.string.my_feedback));

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
        loadingDialog = new LoadingDialog(this, " ");
        loadingDialog.setCanceledOnTouchOutside(true);

    }


    @OnClick({R.id.tv_title_right, R.id.et_feedback_content, R.id.iv_feedback_img, R.id.et_qq_email})
    public void onViewClicked(final View view) {
        switch (view.getId()) {

            case R.id.tv_title_right://提交

                feedbackContent = etFeedbackContent.getText().toString();
                feedbackContactWay = etQqEmail.getText().toString();
                if (TextUtils.isEmpty(feedbackContent)) {//必须发送内容
                    ToastUtil.toastLong(FeedbackActivity.this,"请输入反馈内容");
                } else {
                    if (XUtilNet.isNetConnected()) {
                        feedBackData();
                    } else {
                        ToastUtil.toastLong(FeedbackActivity.this,"请检查网络连接");
                    }
                }
                break;
            case R.id.et_feedback_content:
                break;
            case R.id.iv_feedback_img://选择图片

                PermissionUtil.getCameraPermission(this, new PermissionCallBack() {
                    @Override
                    public void PermissionSuccess() {
                        new ShowPopupPhoto(FeedbackActivity.this).setConfig(0, new Photo_State() {
                            @Override
                            public void first_item() {//相册
                                Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                                startActivityForResult(i, PHOTOZOOM);
                            }

                            @Override
                            public void second_item() {//拍照
                                camera(view);

                            }
                        }).showPopupWindow(ivFeedbackImg);
                    }

                    @Override
                    public void PermissionFail() {

                    }
                });
                break;
            case R.id.et_qq_email:
                break;
        }
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
        // 开启一个带有返回值的Activity，请求码为PHOTO_REQUEST_CAREMA
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
        // 开启一个带有返回值的Activity，请求码为PHOTO_REQUEST_CUT
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
     * 反馈的数据
     */
    private void feedBackData() {
        if (loadingDialog != null) {
            loadingDialog.setContent(" ");
        }
        assert loadingDialog != null;
        loadingDialog.show();

        //如果有图片先传图片
        if (imageFile != null) {
            upLoadImage();
        } else {
//            if (RegularExpressionTools.isMobile(feedbackContactWay) || isEmail(feedbackContactWay)) {
                feedBack();
//            } else {
//                ToastUtil.toastShort(FeedbackActivity.this,"请输入正确的手机号或邮箱");
                return;
//            }
        }
    }

    private void feedBack() {
        mPresenter.getFeedBackDataRequest(JMClassAdvice.MyJMClass(feedbackContent, feedbackContactWay, uploadImageUrl));
    }


    private void upLoadImage() {
        final String ObjectKey = mObject + System.currentTimeMillis() + ".jpg";
        final String filePath = imageFile.getPath();
        Logger.e("filePath = " + filePath);

        new Thread(new Runnable() {
            @Override
            public void run() {
                new UpLoadingManager(AppApplication.getContext(), ObjectKey, filePath, new UpLoadingManager.UpLoadingCallBack() {
                    @Override
                    public void UpLoadingSuccess() {
                        imageFile.delete();
                        uploadImageUrl = ObjectKey;
                        Logger.e("上传成功");
                        feedBack();
                    }

                    @Override
                    public void UpLoadingFail() {
                        Logger.e("上传失败");
                    }
                }).asyncUpLoadingFile();
            }
        }).start();
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
                    ToastUtil.toastShort(FeedbackActivity.this, "未找到存储卡，无法存储照片！");
                }
                break;
            default:
                break;
        }
        if (requestCode == IMAGE_COMPLETE) {
            if (data != null) {
                Bitmap bitmap = data.getParcelableExtra("data");
                imageFile = saveBitmapFile(bitmap);
                this.ivFeedbackImg.setImageBitmap(bitmap);
            }
            try {
                Logger.e("完成剪切");
                // 将临时文件删除

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
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

    @Override
    public void showLoading(String title) {

    }

    @Override
    public void stopLoading() {

    }

    @Override
    public void showErrorTip(String msg) {

    }

    @Override
    public void returnFeedBackData(FeedbackData dataBeen) {
        if (dataBeen != null) {
            Logger.i("dataBeen" + dataBeen.toString());
            loadingDialog.dismiss();
        }
        finish();
        ToastUtil.toastShort(FeedbackActivity.this, "感谢你的反馈，我们会及时查看!");
    }
}
