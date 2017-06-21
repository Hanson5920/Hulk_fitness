package com.jaydenxiao.common.download;

import android.os.Environment;

import com.liulishuo.filedownloader.BaseDownloadTask;
import com.liulishuo.filedownloader.FileDownloadListener;
import com.liulishuo.filedownloader.FileDownloadQueueSet;
import com.liulishuo.filedownloader.FileDownloader;
import com.liulishuo.filedownloader.util.FileDownloadUtils;
import com.orhanobut.logger.Logger;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

/**
 * Created by xxq on 2017/5/8 0008.
 * 文件下载工具，基于 FileDownloader
 * 文件下载基础路径在 baseApplication 已经设置好，也可以在其他地方设置，全局变量
 */

public class FileDownloadUtil {

    private static String videoFile = FileDownloadUtils.getDefaultSaveRootPath() + File.separator + "videoName" + File.separator;//保存hashMap(文件名字，文件路径)
    private static String videoFileNameSuffix = ".txt";//文件名字后缀

    /**
     * 例子：http://alipic.cnys.com/upload9/video/2017-04-11/kexk4xadMd.mp4
     * 返回：kexk4xadMd.mp4
     *
     * @param fileUrl
     * @return 文件名字
     */
    public static String createFileName(String fileUrl) {
        String[] strings = fileUrl.split("/");
        return strings[strings.length - 1];
    }

    /**
     * 输入文件的名字，返回路径
     *
     * @param fileName kexk4xadMd.mp4
     * @return Android/data/com.XXX.XX/file/download/kexk4xadMd.mp4
     */
    public static String getFilePathName(String fileName) {
        return FileDownloadUtils.getDefaultSaveRootPath() + File.separator + fileName;
    }

    /**
     * 将 listMap 转为 json 保存在  videoName.txt 文件中
     *
     * @param videoName 健身项目组视频的名字
     * @param listMap   HashMap<视频名字，视频地址>
     */
    public static void saveHashMapToFile(String videoName, HashMap<String, String> listMap) {
        FileOutputStream fileOutputStream;
        //创建File对象
        File file = new File(videoFile);
        if (!file.exists()) {
            file.mkdirs();
        }
        //将list转成String类型
        List<String> cache = new ArrayList<>();
        JSONObject obj = new JSONObject(listMap);
        cache.add(obj.toString());
        // 可存储的字符串数据
        String listStr = cache.toString();
        //判断SD卡是否可读写
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            try {
                fileOutputStream = new FileOutputStream(new File(videoFile + videoName + videoFileNameSuffix));
                fileOutputStream.write(listStr.getBytes());
                if (fileOutputStream != null) {
                    fileOutputStream.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    /**
     * 根据 健身项目视频名字 返回 hash
     *
     * @param videoName
     * @return
     */
    public static HashMap<String, String> getHashMapFromFile(String videoName) {
        //读取文件内容保存到resultStr
        String resultStr = null;
        File file = new File(videoFile + videoName + videoFileNameSuffix);
        try {
            FileInputStream fileInputStream = new FileInputStream(file);
            byte[] b = new byte[fileInputStream.available()];
            fileInputStream.read(b);
            resultStr = new String(b);
            if (fileInputStream != null) {
                fileInputStream.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
            Logger.e("读文件出错");
        }
        HashMap<String, String> tempList = new HashMap<>();
        try {
            JSONArray jsonArray = new JSONArray(resultStr);
            if (jsonArray.length() > 0) {
                JSONObject jsonObject = new JSONObject(jsonArray.get(0).toString());
                Iterator it = jsonObject.keys();
                // 遍历jsonObject数据，添加到Map对象
                while (it.hasNext()) {
                    String key = String.valueOf(it.next());
                    String value = (String) jsonObject.get(key);
                    tempList.put(key, value);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Logger.e("转化list出错");
        }
        return tempList;
    }

    /**
     * 下载单个文件
     */
    public static void downlaodSingleFile(String fileUrl, final DownloadListener downloadListener) {
        String fileName = createFileName(fileUrl);
        FileDownloader.getImpl().create(fileUrl)
                .setPath(FileDownloadUtils.getDefaultSaveRootPath() + File.separator + fileName)
                .setListener(new FileDownloadListener() {
                    @Override
                    protected void pending(BaseDownloadTask task, int soFarBytes, int totalBytes) {
                    }

                    @Override
                    protected void connected(BaseDownloadTask task, String etag, boolean isContinue, int soFarBytes, int totalBytes) {
                    }

                    @Override
                    protected void progress(BaseDownloadTask task, int soFarBytes, int totalBytes) {
                        downloadListener.progress(soFarBytes, totalBytes);
                    }

                    @Override
                    protected void blockComplete(BaseDownloadTask task) {
                    }

                    @Override
                    protected void retry(final BaseDownloadTask task, final Throwable ex, final int retryingTimes, final int soFarBytes) {
                    }

                    @Override
                    protected void completed(BaseDownloadTask task) {
                        downloadListener.success(task.getPath());
                    }

                    @Override
                    protected void paused(BaseDownloadTask task, int soFarBytes, int totalBytes) {

                    }

                    @Override
                    protected void error(BaseDownloadTask task, Throwable e) {
                    }

                    @Override
                    protected void warn(BaseDownloadTask task) {
                    }
                }).start();

    }

    /**
     * 下载一组视频
     *
     * @param fileName
     * @param tasks
     * @param downloadListener
     */
    public static void downLoadGroupVideo(String fileName, List<BaseDownloadTask> tasks, DownloadListener downloadListener) {
        FileDownloadListener fileDownloadListener = createLis(fileName, tasks.size(), downloadListener);
        FileDownloadQueueSet queueSet = new FileDownloadQueueSet(fileDownloadListener);
        // 由于是队列任务, 这里是我们假设了现在不需要每个任务都回调`FileDownloadListener#progress`, 我们只关系每个任务是否完成, 所以这里这样设置可以很有效的减少ipc.
        queueSet.disableCallbackProgressTimes();

        // 所有任务在下载失败的时候都自动重试一次
        queueSet.setAutoRetryTimes(1);

        //使用并行下载
//        queueSet.downloadSequentially(tasks);//串行
        queueSet.downloadTogether(tasks);//并行
        queueSet.start();
    }

    private static FileDownloadListener createLis(final String fileTxtName, final int allTaskNum, final DownloadListener downloadListener) {
        final int[] completeNum = new int[1];
        completeNum[0] = 0;
        final HashMap<String, String> fileNameToPathHashMap = new HashMap<>();
        return new FileDownloadListener() {

            @Override
            protected void pending(BaseDownloadTask task, int soFarBytes, int totalBytes) {
                // 之所以加这句判断，是因为有些异步任务在pause以后，会持续回调pause回来，而有些任务在pause之前已经完成，
                // 但是通知消息还在线程池中还未回调回来，这里可以优化
                // 后面所有在回调中加这句都是这个原因
                if (task.getListener() != this) {
                    return;
                }
            }

            @Override
            protected void connected(BaseDownloadTask task, String etag, boolean isContinue,
                                     int soFarBytes, int totalBytes) {

                super.connected(task, etag, isContinue, soFarBytes, totalBytes);
                if (task.getListener() != this) {
                    return;
                }

            }

            @Override
            protected void progress(BaseDownloadTask task, int soFarBytes, int totalBytes) {
                if (task.getListener() != this) {
                    return;
                }
            }

            @Override
            protected void blockComplete(BaseDownloadTask task) {
                if (task.getListener() != this) {
                    return;
                }
            }

            @Override
            protected void retry(BaseDownloadTask task, Throwable ex, int retryingTimes, int soFarBytes) {
                super.retry(task, ex, retryingTimes, soFarBytes);
                if (task.getListener() != this) {
                    return;
                }

            }

            @Override
            protected void completed(BaseDownloadTask task) {
                if (task.getListener() != this) {
                    return;
                }
                completeNum[0] = completeNum[0] + 1;
                Logger.i(" 完成下载数量 = " + completeNum[0] + "  需要下载总数量 = " + allTaskNum);
                fileNameToPathHashMap.put(task.getFilename(), task.getPath());
                if (completeNum[0] == allTaskNum) {
//                    for (Map.Entry<String, String> entry : stringStringHashMap.entrySet()) {
//                        Logger.i("key = " + entry.getKey() + "\n value = " + entry.getValue());
//                    }
                    FileDownloadUtil.saveHashMapToFile(fileTxtName, fileNameToPathHashMap);
                    downloadListener.success(fileNameToPathHashMap);
                }
            }

            @Override
            protected void paused(BaseDownloadTask task, int soFarBytes, int totalBytes) {
                if (task.getListener() != this) {
                    return;
                }
            }

            @Override
            protected void error(BaseDownloadTask task, Throwable e) {
                if (task.getListener() != this) {
                    return;
                }
            }

            @Override
            protected void warn(BaseDownloadTask task) {
                if (task.getListener() != this) {
                    return;
                }
                Logger.i("warn = " + task.getTag() + " task= " + task.getUrl());

            }
        };
    }

    /**
     * 判断一个文件是否已经下载
     *
     * @param fileUrl
     * @return
     */
    public static boolean isHaveDownload(String fileUrl) {
        String filePath = getFilePathName(createFileName(fileUrl));
        File file = new File(filePath);
        if (file.exists()) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 判断所有文件是否下载完
     *
     * @param fileUrlList
     * @return
     */
    public static boolean isHaveDownLoadGroup(List<String> fileUrlList) {
        boolean isHaveDownloadAll = true;
        int listSize = fileUrlList.size();
        for (int index = 0; index < listSize; index++) {
            if (!isHaveDownload(fileUrlList.get(index))) {//只要有一个没下载完成就跳出循环
                isHaveDownloadAll = false;
                break;
            }
        }
        return isHaveDownloadAll;
    }


}
