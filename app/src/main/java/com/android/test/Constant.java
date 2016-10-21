package com.android.test;

import android.app.Activity;
import android.content.Context;
import android.os.Environment;

import java.io.File;

/**
 * Created by elvis on 2016/10/11.
 * 全局常量
 */
public class Constant {

    /** 全局： **/
    public static final String APP_PATH = Environment.getExternalStorageDirectory() + File.separator + "AndroidTestTool";
    public static final String APP_PATH_SHELL = "/sdcard"+ File.separator + "AndroidTestTool";
    static {
        String[] paths = {
                "/sdcard",
                "/storage/emulated/0"
        };
        for (int i = 0;i<paths.length;i++){
            File file = new File(paths[i]);
            if (file.exists()){
                UIAUTOMATOR_PATH_SHELL = paths[i]+ File.separator + "AndroidTestTool";
            }
        }
    }

    /** 模块1--Handler: **/
    public static final int HANDLER_UIAUTOMATOR = 101;//UIAutomator标记
    public static final int HANDLER_UIAUTOMATOR_START = 102;//UIAutomator标记--开始
    public static final int HANDLER_UIAUTOMATOR_STOP = 103;//UIAutomator标记--开始

    /** 模块2--LogTool: **/
    //目录
    public static final String LOG_PATH = APP_PATH + File.separator + "Log";

    /** 模块3--Crash: **/
    //目录
    public static final String CRASH_PATH = APP_PATH + File.separator + "Crash";

    /** 模块4--下载: **/
    public static final String DOWNLOAD_PATH = APP_PATH + File.separator + "Download";

    /** 模块5--UIAutomator: **/
    //目录
    public static final String UIAUTOMATOR_PATH = APP_PATH + File.separator + "UIAutomator";
    public static String UIAUTOMATOR_PATH_SHELL = APP_PATH_SHELL+ File.separator + "UIAutomator";
    //文件
    public static final String UIAutomatorDataFilePath = UIAUTOMATOR_PATH + File.separator + "data.txt";
    public static final String UIAutomatorViewFilePath = UIAUTOMATOR_PATH_SHELL + File.separator+"view.xml";
    public static final String UIAutomatorReportFilePath = UIAUTOMATOR_PATH + File.separator+"report.txt";





    /**
     * 文件夹创建：
     */
    static {
        String[] paths= {
                APP_PATH,
                DOWNLOAD_PATH,UIAUTOMATOR_PATH,LOG_PATH,CRASH_PATH
        };
        for (int i=0;i<paths.length;i++){
            File file = new File(paths[i]);
            if (!file.exists()){
                try {
                    file.mkdirs();
                }catch (Exception e){}
            }
        }
    }


}
