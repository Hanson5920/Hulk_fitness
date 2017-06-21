package com.lxh.userlibrary.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.text.TextUtils;


import com.lxh.userlibrary.UserCenter;
import com.lxh.userlibrary.constant.AppStatus;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;

/**
 * Created by Administrator on 2016/5/17.
 */
public class FileUtil {

    public static final String ENCODING = "UTF-8";

    /**
     * 从文件路径里获取文件名 ，含文件后缀名
     *
     * @param filePath
     * @return
     */
    public static String getFileName(String filePath) {
        if (TextUtils.isEmpty(filePath))
            return filePath;

        int filePosi = filePath.lastIndexOf(File.separator);
        return (filePosi == -1) ? filePath : filePath.substring(filePosi + 1);
    }

    /**
     * 获取该文件的所属上级文件夹
     *
     * @param filePath
     * @return
     */
    public static String getFolderName(String filePath) {
        if (TextUtils.isEmpty(filePath))
            return filePath;

        File file = new File(filePath);
        if (file.exists() && file.isDirectory())
            return filePath;

        int filePosi = filePath.lastIndexOf(File.separator);
        return (filePosi == -1) ? "" : filePath.substring(0, filePosi);
    }

    /**
     * 获取该文件路径的文件后缀名
     *
     * @param filePath
     * @return
     */
    public static String getFileExtension(String filePath) {
        if (TextUtils.isEmpty(filePath))
            return filePath;

        int extenPosi = filePath.lastIndexOf(".");
        int filePosi = filePath.lastIndexOf(File.separator);
        if (extenPosi == -1)
            return "";
        return (filePosi >= extenPosi) ? "" : filePath.substring(extenPosi + 1).toLowerCase();
    }

    /**
     * 创建文件夹
     */
    public static boolean makeDirs(String filePath) {
        String folderName = getFolderName(filePath);

        if (TextUtils.isEmpty(folderName))
            return false;

        File folder = new File(folderName);
        if (folder.exists() && folder.isDirectory())
            return true;

        return folder.mkdirs();
    }

    /**
     * 判断制定文件路径的文件或文件夹是否存在
     *
     * @param filePath
     * @return
     */
    public static boolean isFileExist(String filePath) {
        if (TextUtils.isEmpty(filePath)) {
            return false;
        }

        File file = new File(filePath);
        return file.exists();
    }

    public static boolean isFileExist(File file) {
        return file != null && file.exists() && file.length() > 0;
    }

    /**
     * 重命名
     *
     * @param oldPath 原文件路径
     * @param newPath 新文件路径
     *                *
     */
    public static void renameFile(String oldPath, String newPath) {
        File oldFile = new File(oldPath);
        File newFile = new File(newPath);
        if (oldFile.exists())
            oldFile.renameTo(newFile);
    }

    public static boolean writeFile(String filePath, String content, boolean append) {
        if (TextUtils.isEmpty(content)) {
            return false;
        }

        FileWriter fileWriter = null;

        try {
            makeDirs(filePath);
            fileWriter = new FileWriter(filePath, append);
            fileWriter.write(content);
            fileWriter.close();
            return true;
        } catch (IOException e) {
            throw new RuntimeException("IOException occurred. ", e);
        } finally {
            if (fileWriter != null) {
                try {
                    fileWriter.close();
                } catch (IOException e) {
                    throw new RuntimeException("IOException occurred. ", e);
                }
            }
        }
    }


    /**
     * write file, the string will be written to the begin of the file
     *
     * @param filePath
     * @param content
     * @return
     */
    public static boolean writeFile(String filePath, String content) {
        return writeFile(filePath, content, false);
    }


    /**
     * write file, the bytes will be written to the begin of the file
     *
     * @param filePath
     * @param stream
     * @return
     * @see {@link #writeFile(String, InputStream, boolean)}
     */
    public static boolean writeFile(String filePath, InputStream stream) {
        return writeFile(filePath, stream, false);
    }

    /**
     * write file
     *
     * @param filePath the file to be opened for writing.
     * @param stream   the input stream
     * @param append   if <code>true</code>, then bytes will be written to the end of the file rather than the beginning
     * @return return true
     * @throws RuntimeException if an error occurs while operator FileOutputStream
     */
    public static boolean writeFile(String filePath, InputStream stream, boolean append) {
        return writeFile(filePath != null ? new File(filePath) : null, stream, append);
    }

    /**
     * write file, the bytes will be written to the begin of the file
     *
     * @param file
     * @param stream
     * @return
     * @see {@link #writeFile(File, InputStream, boolean)}
     */
    public static boolean writeFile(File file, InputStream stream) {
        return writeFile(file, stream, false);
    }

    /**
     * write file
     *
     * @param file   the file to be opened for writing.
     * @param stream the input stream
     * @param append if <code>true</code>, then bytes will be written to the end of the file rather than the beginning
     * @return return true
     * @throws RuntimeException if an error occurs while operator FileOutputStream
     */
    public static boolean writeFile(File file, InputStream stream, boolean append) {
        OutputStream o = null;
        try {
            makeDirs(file.getAbsolutePath());
            o = new FileOutputStream(file, append);
            byte data[] = new byte[1024];
            int length = -1;
            while ((length = stream.read(data)) != -1) {
                o.write(data, 0, length);
            }
            o.flush();
            return true;
        } catch (FileNotFoundException e) {
            throw new RuntimeException("FileNotFoundException occurred. ", e);
        } catch (IOException e) {
            throw new RuntimeException("IOException occurred. ", e);
        } finally {
            Utils.closeCloseable(o);
            Utils.closeCloseable(stream);
        }
    }

    /**
     * Indicates if this file represents a directory on the underlying file system.
     *
     * @param directoryPath
     * @return
     */
    public static boolean isFolderExist(String directoryPath) {
        if (TextUtils.isEmpty(directoryPath)) {
            return false;
        }

        File dire = new File(directoryPath);
        return (dire.exists() && dire.isDirectory());
    }

    /**
     * 获取临时文件名
     *
     * @param fileame
     * @return
     */
    public static String getTempFileName(String fileame) {
        return "temp_" + fileame;
    }

    /**
     * 删除文件或文件夹
     *
     * @param path
     * @return
     */
    public static boolean deleteFile(String path) {
        if (TextUtils.isEmpty(path))
            return false;
        File file = new File(path);
        if (!file.exists())
            return true;

        if (file.isFile())
            return deleteFileSafely(file);

        for (File f : file.listFiles()) {
            if (!deleteFile(f.getAbsolutePath()))
                return false;
        }
        return deleteFileSafely(file);
    }

    /**
     * 安全删除文件. 解决文件删除后重新创建导致报错的问题：open failed: EBUSY (Device or resource busy)
     * http://stackoverflow.com/questions/11539657/open-failed-ebusy-device-or-resource-busy
     *
     * @param file
     * @return
     */
    public static boolean deleteFileSafely(File file) {
        if (file == null) return true;
        String tmpPath = file.getAbsolutePath() + System.currentTimeMillis();
        File tmp = new File(tmpPath);
        file.renameTo(tmp);
        return tmp.delete();
    }

    /**
     * 读取文本文件为String
     *
     * @param file
     * @return
     */
    public static String readFileAsString(File file) {
        if (!isFileExist(file))
            return null;

        StringBuilder sb = new StringBuilder();
        BufferedReader br = null;

        try {
            br = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
            String line;

            while ((line = br.readLine()) != null) {
                sb.append(line).append('\n');
            }

            return sb.toString();
        } catch (Exception e) {
            // do nothing
        } finally {
            if (br != null) {
                try {
                    br.close();

                } catch (IOException e) {
//                    XUtilLog.log_e(e.toString());
                }
            }
        }

        return null;
    }

    /**
     * 读取文本文件为String
     *
     * @param inputStream
     * @return
     */
    public static String readFileAsString(InputStream inputStream) {

        StringBuilder sb = new StringBuilder();
        BufferedReader br = null;

        try {
            br = new BufferedReader(new InputStreamReader(inputStream));
            String line;

            while ((line = br.readLine()) != null) {
                sb.append(line).append('\n');
            }

            return sb.toString();
        } catch (Exception e) {
//            XUtilLog.log_e(e.toString());
        } finally {
            if (br != null) {
                try {
                    br.close();

                } catch (IOException e) {
//                    XUtilLog.log_e(e.toString());
                }
            }
        }

        return null;
    }


    public static boolean writeSerializeFile(String filename, Context context, Object obj) {
        FileOutputStream ostream = null;
        try {
            ostream = context.openFileOutput(filename, Context.MODE_PRIVATE);
        } catch (FileNotFoundException e1) {
            e1.printStackTrace();
            return false;
        }

        ObjectOutputStream p = null;
        try {
            p = new ObjectOutputStream(ostream);
            p.writeObject(obj);
            p.flush();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        } finally {
            Utils.closeCloseable(p);
            Utils.closeCloseable(ostream);
        }
        return true;
    }

    @SuppressWarnings("unchecked")
    public static Object readSerializeFile(String filename, Context context) {
        FileInputStream istream = null;
        try {
            istream = context.openFileInput(filename);
        } catch (FileNotFoundException e1) {
            e1.printStackTrace();
            return null;
        }
        ObjectInputStream q = null;
        try {
            q = new ObjectInputStream(istream);
            return q.readObject();
        } catch (IOException e) {
//            XUtilLog.log_e(e.toString());
        } catch (ClassNotFoundException e) {
//            XUtilLog.log_e(e.toString());
        } finally {
            Utils.closeCloseable(q);
            Utils.closeCloseable(istream);
        }
        return null;
    }

    /**
     * get file name from path, not include suffix
     * <p/>
     * <pre>
     *      getFileNameWithoutExtension(null)               =   null
     *      getFileNameWithoutExtension("")                 =   ""
     *      getFileNameWithoutExtension("   ")              =   "   "
     *      getFileNameWithoutExtension("abc")              =   "abc"
     *      getFileNameWithoutExtension("a.mp3")            =   "a"
     *      getFileNameWithoutExtension("a.b.rmvb")         =   "a.b"
     *      getFileNameWithoutExtension("c:\\")              =   ""
     *      getFileNameWithoutExtension("c:\\a")             =   "a"
     *      getFileNameWithoutExtension("c:\\a.b")           =   "a"
     *      getFileNameWithoutExtension("c:a.txt\\a")        =   "a"
     *      getFileNameWithoutExtension("/home/admin")      =   "admin"
     *      getFileNameWithoutExtension("/home/admin/a.txt/b.mp3")  =   "b"
     * </pre>
     *
     * @param filePath
     * @return file name from path, not include suffix
     * @see
     */
    public static String getFileNameWithoutExtension(String filePath) {
        if (TextUtils.isEmpty(filePath)) {
            return filePath;
        }

        int extenPosi = filePath.lastIndexOf(".");
        int filePosi = filePath.lastIndexOf(File.separator);
        if (filePosi == -1) {
            return (extenPosi == -1 ? filePath : filePath.substring(0, extenPosi));
        }
        if (extenPosi == -1) {
            return filePath.substring(filePosi + 1);
        }
        return (filePosi < extenPosi ? filePath.substring(filePosi + 1, extenPosi) : filePath.substring(filePosi + 1));

    }


    public static boolean isGpkFile(File file) {
        String fileExtension = getFileExtension(file.getAbsolutePath());
        if (fileExtension.equals(AppStatus.TYPE_GPK))
            return true;
        return false;
    }

    public static boolean isApkFile(File file) {
        String fileExtension = getFileExtension(file.getAbsolutePath());
        if (fileExtension.equals(AppStatus.TYPE_APK))
            return true;
        return false;
    }

    public static void deleteDir(File dest) throws IOException {

        if (dest.exists() == false) {
            throw new FileNotFoundException("Not a directory: " + dest);
        }

        if (dest.isDirectory() == false) {
            throw new IOException("Not a directory: " + dest);
        }

        File[] files = dest.listFiles();
        if (files == null) {
            throw new IOException("Failed to list contents of: " + dest);
        }

        IOException exception = null;
        for (File file : files) {
            try {
                if (file.isDirectory()) {
                    deleteDir(file);
                } else {
                    file.delete();
                }
            } catch (IOException ioex) {
                exception = ioex;
                continue;
            }
        }
        if (exception != null) {
            throw exception;
        }
    }

    public static boolean initDownFile(File file){
        if(file.exists()){
            return true;
        }
        return false;
    }


    /**
     * 递归删除目录下的所有文件及子目录下所有文件
     * @param dir 将要删除的文件目录
     * @return boolean Returns "true" if all deletions were successful.
     *                 If a deletion fails, the method stops attempting to
     *                 delete and returns "false".
     */
    public static boolean deleteDirs(File dir) {
        if (dir.isDirectory()) {
            String[] children = dir.list();
            //递归删除目录中的子目录下
            for (int i=0; i<children.length; i++) {
                File file = new File(dir, children[i]);
                boolean success = deleteDirs(file);
                if (!success) {
                    return false;
                }
            }
        }
        // 目录此时为空，可以删除
        return dir.delete();
    }

    /**
     * 删除空文件夹
     * @param dir
     * @return
     */
    public static boolean doDeleteEmptyDir(String dir) {
        boolean success = (new File(dir)).delete();
        if (success) {
            System.out.println("Successfully deleted empty directory: " + dir);
        } else {
            System.out.println("Failed to delete empty directory: " + dir);
        }
        return success;
    }

    /**
     * 获取应用目录，当SD卡存在时，获取SD卡上的目录，当SD卡不存在时，获取应用的cache目录
     */
    public static String getDir(String name) {
        StringBuilder sb = new StringBuilder();
        if (FileUtils.isSDCardAvailable() && Environment.getExternalStorageDirectory() != null && Environment.getExternalStorageDirectory().getAbsolutePath() != null) {
            //"sdcard/data/wapu/DJCC/cache/"
            sb.append(Environment.getExternalStorageDirectory().getAbsolutePath());
            sb.append(File.separator);
            sb.append("data");
            sb.append(File.separator);
            sb.append("wapu");
            sb.append(File.separator);
            sb.append("DJCC");
            sb.append(File.separator);
            sb.append("cache");
            sb.append(File.separator);
        } else {
            sb.append(getCachePath());
        }
        sb.append(name);
        sb.append(File.separator);
        String path = sb.toString();
        if (!createDirs(path)) {
            File destDir = new File(path);
            destDir.mkdirs();
        }
        return path;
    }
    /**
     * 获取应用的cache目录
     */
    public static String getCachePath() {
        File f = UserCenter.getContext().getCacheDir();
        if (null == f) {
            return null;
        } else {
            return f.getAbsolutePath() + "/";
        }
    }

    /**
     * 创建文件夹
     */
    public static boolean createDirs(String dirPath) {
        File file = new File(dirPath);
        if (!file.exists() || !file.isDirectory()) {
            return file.mkdirs();
        }
        return true;
    }

    /**
     * Description 计算文件夹大小
     * UpdateUser husong.
     * UpdateDate 2016/10/12 11:31.
     * Version 1.0
     */
    public static long getFolderSize(java.io.File file){
        long size = 0;
        java.io.File[] fileList = file.listFiles();
        for (int i = 0; i < fileList.length; i++)
        {
            if (fileList[i].isDirectory())
            {
                size = size + getFolderSize(fileList[i]);
            } else
            {
                size = size + fileList[i].length();
            }
        }
        return size;
    }

    /**
     * 获取当前程序路径
     */
    public static String getAppPath(Context context) {
        String path=context.getApplicationContext().getFilesDir().getAbsolutePath();
        return path;
    }

    /**
     * 获得当前版本号
     * @return
     */
    public static String getVersion(Context context) {
        try {
            PackageManager manager = context.getPackageManager();
            PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
            String version = info.versionName;
            return version;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}