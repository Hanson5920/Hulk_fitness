package com.gz.hkjs.app.util;

import android.content.Context;
import android.util.Log;

import com.alibaba.sdk.android.oss.ClientConfiguration;
import com.alibaba.sdk.android.oss.ClientException;
import com.alibaba.sdk.android.oss.OSS;
import com.alibaba.sdk.android.oss.OSSClient;
import com.alibaba.sdk.android.oss.ServiceException;
import com.alibaba.sdk.android.oss.callback.OSSCompletedCallback;
import com.alibaba.sdk.android.oss.callback.OSSProgressCallback;
import com.alibaba.sdk.android.oss.common.OSSLog;
import com.alibaba.sdk.android.oss.common.auth.OSSCredentialProvider;
import com.alibaba.sdk.android.oss.common.auth.OSSPlainTextAKSKCredentialProvider;
import com.alibaba.sdk.android.oss.model.ObjectMetadata;
import com.alibaba.sdk.android.oss.model.PutObjectRequest;
import com.alibaba.sdk.android.oss.model.PutObjectResult;
import com.gz.hkjs.app.BuildConfig;
import com.gz.hkjs.app.app.AppApplication;
import com.orhanobut.logger.Logger;

import java.util.HashMap;

/**
 * Created by lwy on 2017/4/21.
 */

public class UpLoadingManager {
    public interface UpLoadingCallBack {
        void UpLoadingSuccess();//成功

        void UpLoadingFail();//失败
    }

    //OSS的Bucket
    private String OSS_BUCKET;
    private String testObject;
    //设置OSS数据中心域名或者cname域名
    private String OSS_ENDPOINT = "http://oss-cn-beijing.aliyuncs.com";
    //key
    private OSS oss;
    private String uploadFilePath;
    private UpLoadingCallBack upLoadingCallBack;
    private String ACCESS_ID ;
    private String ACCESS_KEY;

    public UpLoadingManager(Context context, String ObjectKey, String uploadFilePath, UpLoadingCallBack upLoadingCallBack) {

        this.ACCESS_ID="LTAI6L8wYzFhiR3R";
        this.ACCESS_KEY=" 19r0RmmSkxBlWS8M03sKc2fgHwDsQY";
        this.uploadFilePath = uploadFilePath;
        this.upLoadingCallBack = upLoadingCallBack;
        this.OSS_BUCKET = "cnys-pic";
        this.testObject = ObjectKey;
        OSSCredentialProvider credentialProvider = new OSSPlainTextAKSKCredentialProvider(ACCESS_ID, ACCESS_KEY);
        ClientConfiguration conf = new ClientConfiguration();
        conf.setConnectionTimeout(15 * 1000); // 连接超时，默认15秒
        conf.setSocketTimeout(15 * 1000); // socket超时，默认15秒
        conf.setMaxConcurrentRequest(5); // 最大并发请求书，默认5个
        conf.setMaxErrorRetry(2); // 失败后最大重试次数，默认2次
        if (BuildConfig.DEBUG) {
            OSSLog.enableLog();
        }
        oss = new OSSClient(context, OSS_ENDPOINT, credentialProvider, conf);
    }
    public void asyncUpLoadingFile(){
        PutObjectRequest put = new PutObjectRequest(OSS_BUCKET, testObject , uploadFilePath);
        ObjectMetadata metadata = new ObjectMetadata();
        put.setMetadata(metadata);
        put.setCallbackParam(new HashMap<String, String>() {
            {
//                put("callbackBody", "bucket=${bucket}&object=${object}&etag=${etag}&size=${size}&mimeType=$" +
//                        "{mimeType}&imageInfo.height=${imageInfo.height}&" +
//                        "imageInfo.width=${imageInfo.width}&imageInfo.format=${imageInfo.format}&my_var=${x:my_var}");
                put("callbackBody", "{\"mimeType\":${mimeType},\"size\":${size}}");
            }
        });
        put.setProgressCallback(new OSSProgressCallback<PutObjectRequest>() {
            @Override
            public void onProgress(PutObjectRequest request, long currentSize, long totalSize) {
                Logger.i("PutObject", "currentSize: " + currentSize + " totalSize: " + totalSize);

            }
        });
        oss.asyncPutObject(put, new OSSCompletedCallback<PutObjectRequest, PutObjectResult>() {
            @Override
            public void onSuccess(final PutObjectRequest request, PutObjectResult result) {
                upLoadingCallBack.UpLoadingSuccess();
                Logger.i("PutObject", "UploadSuccess");
                Logger.i("ETag", result.getETag());
                Logger.i("RequestId", result.getRequestId());
                //将上传成功的图片地址传给自己的服务器后台，比如修改用户数据库中，用户头像的url。
                //修改后台url成功后，再利用glide 下载最新的照片，修改本地头像图片。
                //request.getObjectKey() 是图片地址，但是不包含，OSS 图片域名
            }

            @Override
            public void onFailure(PutObjectRequest request, ClientException clientExcepion, ServiceException serviceException) {
              upLoadingCallBack.UpLoadingFail();
                // 请求异常
                if (clientExcepion != null) {
                    // 本地异常如网络异常等
                    clientExcepion.printStackTrace();
                }
                if (serviceException != null) {
                    // 服务异常
                    Log.e("ErrorCode", serviceException.getErrorCode());
                    Log.e("RequestId", serviceException.getRequestId());
                    Log.e("HostId", serviceException.getHostId());
                    Log.e("RawMessage", serviceException.getRawMessage());
                }

            }
        });
    }
}
