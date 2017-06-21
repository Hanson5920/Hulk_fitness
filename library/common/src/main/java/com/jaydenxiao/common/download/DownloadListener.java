package com.jaydenxiao.common.download;

import java.util.HashMap;

/**
 * Created by xxq on 2017/5/9 0009.
 * 目前只是简单判断下载成功，后面添加下载失败情况
 */

public interface DownloadListener {
    //针对下载一组文件
    //HashMap<"文件名", "路径">
    void success(HashMap<String, String> FilePathMap);

    //下下载单个文件
    void success(String filePath);

    //下载单个文件的进度
    void progress(int soFarBytes, int totalBytes);
}
