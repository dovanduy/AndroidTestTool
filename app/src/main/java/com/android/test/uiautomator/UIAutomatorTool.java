package com.android.test.uiautomator;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;

import com.android.test.Constant;
import com.android.test.log.LogTool;
import com.android.test.shell.utest.UMVirtualTerminal;
import com.android.test.shell.utest.VTCommandResult;

import org.xml.sax.SAXException;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

/**
 * Created by elvis on 2016/9/29.
 * UIAutomator采集页面数据工具
 */
public class UIAutomatorTool {

    private static String TAG = "UIAutomatorTool";

    //通讯：
    private static Handler uiHandler;
    //执行：
    private static HandlerThread collectionHandlerThread;
    private static CollectionHandler collectionHandler;
    private static Timer collectionTimer;
    private static Timer environmentTimer;
    //状态信息：
    private static Date lastCollectDate;
    public static boolean isStateChanged = false;

    /**
     * 开启采集线程
     * @return
     */
    public static void startCollection( final Handler uiHandler){

        LogTool.makeLog(LogTool.LEVEL.Level_e,TAG,"startCollection----------------------------");
        new Thread(){
            @Override
            public void run() {
                //检测运行环境
                if (checkEnvironment()==false){
                    return ;
                }
                //如果线程在运行，则停止
                if(isRun()){
                    stopCollection();
                }
                //赋值属性
                UIAutomatorTool.uiHandler = uiHandler;
                //开启当前采集线程
                if (collectionHandlerThread==null){
                    collectionHandlerThread = new HandlerThread("UIAutomator");
                    collectionHandlerThread.start();
                }
                //初始化handler
                collectionHandler = new CollectionHandler(collectionHandlerThread.getLooper());
                //初始化Timer,开启timer,立刻执行，周期3秒
                collectionTimer = new Timer();
                collectionTimer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        try{
                            Message msg = collectionHandler.obtainMessage(CollectionHandler.MSG_COLLECT, "");
                            collectionHandler.sendMessage(msg);
                        }catch (Exception e){
                            //防止任务停止，指针为空
                            LogTool.makeLog(LogTool.LEVEL.Level_e,TAG,"任务停止，指针为空");
                        }

                    }
                }, 0, 3000);
                //开启环境监测timer,开启timer,立刻执行，周期30秒
                environmentTimer = new Timer();
                environmentTimer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        if (!checkEnvironment()){
                            sendMessageToUI("环境不可用，服务停止！");
                            stopCollection();
                        }
                    }
                }, 0, 30000);
                sendMessageToUI("任务开始！");
                UIAutomatorTool.isStateChanged = true;
            }
        }.start();

    }


    /**
     * 检测是否在运行,thread和Handler和timer不为空，并且timer正在运行---提示已经在运行了。
     * @return
     */
    public static boolean isRun(){
        if(collectionHandlerThread==null
                ||collectionHandler==null
                ||environmentTimer==null
                ||collectionTimer==null){
            stopCollection();
            return false;
        }
        return true;
    }

    /**
     * 结束采集
     * @return
     */
    public static String stopCollection(){
        LogTool.makeLog(LogTool.LEVEL.Level_e,TAG,"stopCollection----------------------");
        if (collectionTimer==null){
            return "notExist";
        }else {
            collectionTimer .cancel();
            collectionTimer = null;
            if (collectionHandler!=null){
                Message msg = collectionHandler.obtainMessage(CollectionHandler.MSG_COLLECT_STOP, "");
                collectionHandler.sendMessage(msg);
                collectionHandler = null;
            }
            if (collectionHandlerThread!=null){
                collectionHandlerThread.getLooper().quit();
                collectionHandlerThread = null;
            }
            if (environmentTimer!=null){
                environmentTimer.cancel();
                environmentTimer = null;
            }
            lastCollectDate = null;
            UIAutomatorTool.isStateChanged = true;
            return "success";
        }
    }





    /**
     * 检测当前运行环境--Shell代理是否开启、采集文件路径是否可用
     * @return
     */
    public static boolean checkEnvironment(){
        //检测shell代理
        try {
            UMVirtualTerminal umVirtualTerminal = new UMVirtualTerminal();
        } catch (IOException | InterruptedException e) {
            sendMessageToUI("shell代理未开启,请手动运行脚本开启！");
            LogTool.makeLog(LogTool.LEVEL.Level_e,TAG,"shell代理未开启,请手动运行脚本开启！");
            LogTool.makeLog(LogTool.LEVEL.Level_e,TAG,e.getMessage());
            return false;
        }
        //检测数据存储路径
        File uiautomatorTempDir = new File(Constant.UIAUTOMATOR_PATH);
        if (!uiautomatorTempDir.exists()){
            uiautomatorTempDir.mkdirs();
        }
        if (!uiautomatorTempDir.exists()){
            sendMessageToUI("文件保存路径不可用！");
            LogTool.makeLog(LogTool.LEVEL.Level_e,TAG,"文件保存路径不可用！");
            return false;
        }
        return true;
    }

    /**
     * 发送消息给UI线程
     * @param msgString
     */
    private static void sendMessageToUI(String msgString){
        if (uiHandler!=null){
            Message message = Message.obtain(uiHandler,Constant.HANDLER_UIAUTOMATOR,msgString);
            uiHandler.sendMessage(message);
        }
    }



    /**
     * 采集方法：
     */
    public static class CollectionHandler extends Handler{

        public static final String TAG = "CollectionHandler";
        public static final int MSG_COLLECT = 101;
        public static final int MSG_COLLECT_STOP = 102;

        //保存数据相关：
        File xmlFile;
        File dataSaveFile;
        FileOutputStream dataSaveFileOutputStream;
        BufferedWriter dataSaveFileWriter;
        //构造函数
        public CollectionHandler(Looper looper) {
            super(looper);
            xmlFile = new File(Constant.UIAutomatorViewFilePath);
            dataSaveFile = new File(Constant.UIAutomatorDataFilePath);
            try {
                dataSaveFileOutputStream =  new FileOutputStream(dataSaveFile, true);
                dataSaveFileWriter = new BufferedWriter(new OutputStreamWriter(dataSaveFileOutputStream));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                LogTool.makeLog(LogTool.LEVEL.Level_e,TAG,e.getMessage());
            }
        }

        /**
         * 采集数据
         * @param msg
         */
        @Override
        public void handleMessage(Message msg) {
            if (msg==null)return;
            switch (msg.what){
                case MSG_COLLECT:
                    //采集一次数据
                    collect();
                    break;
                case MSG_COLLECT_STOP:
                    close();
                    break;
            }
        }

        /**
         * 采集一次数据
         */
        void collect(){
            //保存当前采集时间：
            Date nowCollectDate = new Date(System.currentTimeMillis());
            if (lastCollectDate!=null&&nowCollectDate.getTime()-lastCollectDate.getTime()>1000*60){
                //如果超过一分钟没采集，则关闭线程。
                stopCollection();
            }else {
                lastCollectDate = new Date(System.currentTimeMillis());
            }

            //执行获取控件树命令
            UMVirtualTerminal umVirtualTerminal = null;
            try {
                umVirtualTerminal = new UMVirtualTerminal();
                VTCommandResult vtCommandResult = umVirtualTerminal.runCommand(String.format("uiautomator dump %s",Constant.UIAutomatorViewFilePath));
                if (vtCommandResult != null&&vtCommandResult.success()){
                    LogTool.makeLog(LogTool.LEVEL.Level_e,TAG,"拉取控件树数据成功！");
                    //执行命令获取当前包名
                    String packageName = "";
                    try{
                        vtCommandResult = umVirtualTerminal.runCommand("dumpsys activity | grep \"mFocusedActivity\"");
                        if (vtCommandResult != null&&vtCommandResult.success()){
                            LogTool.makeLog(LogTool.LEVEL.Level_e,TAG,"当前顶层Activity信息："+vtCommandResult.stdout);
                            String[] s1 = vtCommandResult.stdout.split("/");
                            if (s1.length>0){
                                String sLeft = s1[0];
                                String[] s2  = sLeft.split(" ");
                                if (s2.length>0){
                                    packageName = s2[s2.length-1];
                                }
                            }
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                        LogTool.makeLog(LogTool.LEVEL.Level_e,TAG,e.getMessage());
                        packageName = "";
                    }
                    AnalysisXMLFileToData(packageName,Constant.UIAutomatorViewFilePath);
                }else {
                    LogTool.makeLog(LogTool.LEVEL.Level_e,TAG,"未得到命令结果！");
                }
            } catch (Exception e) {
                e.printStackTrace();
                LogTool.makeLog(LogTool.LEVEL.Level_e,TAG,e.getMessage());
            }finally {
                if (umVirtualTerminal!=null){
                    umVirtualTerminal.shutdown();
                }
            }

        }
        /**
         * 分析xml控件树文件
         */
        void AnalysisXMLFileToData(String packageName , String xmlFileName){
            File xmlFile = new File(xmlFileName);
            String hashValue = "";
            if (xmlFile.exists()){
                hashValue = getHashOfXML(xmlFile);
            }
            String  data = packageName+"_"+hashValue;
            //写入数据文件(包名+xml文件hash值)
            try{
                xmlFile.delete();
            }catch (Exception e){
                LogTool.makeLog(LogTool.LEVEL.Level_e,TAG,e.getMessage());
            }
            //写入磁盘
            try {
                dataSaveFileWriter.write(data);
                dataSaveFileWriter.write(System.getProperty("line.separator"));
                dataSaveFileWriter.flush();
            } catch (IOException e) {
                e.printStackTrace();
                LogTool.makeLog(LogTool.LEVEL.Level_e,TAG,e.getMessage());
            }
        }

        void close(){
            //关闭输入输出流
            try {
                if (dataSaveFileOutputStream != null)
                    dataSaveFileOutputStream.close();
                if (dataSaveFileWriter != null)
                    dataSaveFileWriter.close();
            } catch (IOException e) {
                e.printStackTrace();
                LogTool.makeLog(LogTool.LEVEL.Level_e,TAG,"正在执行close函数，发生异常");
                LogTool.makeLog(LogTool.LEVEL.Level_e,TAG,e.getMessage());
            }
        }



        /**
         * 获取dump页面数据文件的内容
         * @param dumpFile
         * @return
         */
        public static String getStrustOfXML(File dumpFile){
            MyXMLReadHandler myDefaultHandler = getMyDefaultHandler(dumpFile);
            if (myDefaultHandler!=null){
                return myDefaultHandler.getStrustOfXML();
            }
            return "";
        }

        /**
         * 获取dump页面数据文件的结构
         */
        public static String getHashOfXML(File dumpFile){
            MyXMLReadHandler myDefaultHandler = getMyDefaultHandler(dumpFile);
            if (myDefaultHandler!=null){
                return myDefaultHandler.getHashOfXML();
            }
            return "";
        }

        /**
         * 获取读取XML文件的MyXMLReadHandler对象
         */
        private static MyXMLReadHandler getMyDefaultHandler(File dumpFile){
            try {
                FileInputStream is = new FileInputStream(dumpFile);
                //更新数据策略：
                MyXMLReadHandler myDefaultHandler = new MyXMLReadHandler();
                SAXParserFactory sFactory= SAXParserFactory.newInstance();
                SAXParser parser;
                parser = sFactory.newSAXParser();
                parser.parse(is, myDefaultHandler);
                return myDefaultHandler;
            } catch (ParserConfigurationException | SAXException | IOException e) {
                e.printStackTrace();
                LogTool.makeLog(LogTool.LEVEL.Level_e,TAG,"读取XML文件异常"+e.getMessage());
            }
            return null;
        }





    }






}
