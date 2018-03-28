package com.txh.im;

import android.os.Environment;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * apk下载
 * content : 全局的配置
 */
public class AppConfig {

    /**
     * 默认文件下载的路径
     */
    public final static String DEFAULT_SAVE_IMAGE_PATH = Environment.getExternalStorageDirectory() + File.separator + "YunTianXia" + File.separator + "download" + File.separator;

    /**
     * 默认图片下载的路径
     * 待更新
     */


    public static List<String> xx = new ArrayList<>();


}
