package com.android.test.uiautomator;

import android.content.Context;
import android.os.Build;
import android.text.TextUtils;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.android.test.Constant;
import com.android.test.tools.PackageTool;
import com.android.test.tools.TimerTool;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by elvis on 2016/10/20.
 * UIAutomator数据处理
 */

public class UIAutomatorDataUtil {
    /**
     * 读取数据文件，返回JSONArray数据
     * @return
     */
    public static JSONArray getReportJson() {
        //检查数据文件是否存在
        File dataFile = new File(Constant.UIAutomatorDataFilePath);
        if (!dataFile.exists()){return new JSONArray();}
        //读取文件
        BufferedReader reader = null;
        Map<String, UIAutomatorData> dataMap = new HashMap<String, UIAutomatorData>();
        Map<String, String> pageMap = new HashMap<String, String>();//(哈希值，包名)页面库，用来保存已经经历过的页面
        try {
            //开始遍历文件
            String line = null;
            reader = new BufferedReader(new InputStreamReader( new FileInputStream(dataFile)));
            String lastPageKey = null;//上条采集页面
            String thisPageKey = null;//这条采集页面
            double lastCollectTime = 0;//上条采集时间
            double thisCollectTime = 0;//这条采集时间
            while ((line = reader.readLine()) != null) {
                //检查数据
                String[] data = line.split("_");
                if (data.length!=3){
                    continue;
                }
                //拆分本条数据
                thisCollectTime = Double.parseDouble(data[0]);
                String packageName = data[1];
                thisPageKey =data[2];
                //如果不存在此包名的信息,则添加相关信息
                if (!dataMap.containsKey(packageName)) {
                    dataMap.put(packageName, new UIAutomatorData(packageName));
                }
                //时间差小于30秒，就累计此时间
                double reallyTime = 0;
                if (thisCollectTime!=0&&lastCollectTime!=0&&thisCollectTime-lastCollectTime<30000){
                    reallyTime = thisCollectTime-lastCollectTime;
                }
                lastCollectTime = thisCollectTime;
                //先判断页面是否发生变化：
                if (lastPageKey!=null&&thisPageKey!=null&&!thisPageKey.equals(lastPageKey)){
                    dataMap.get(packageName).totalTimePlus(reallyTime/1000);//页面发生变化
                    dataMap.get(packageName).changeTimePlus(reallyTime/1000);
                }else {
                    dataMap.get(packageName).totalTimePlus(reallyTime/1000);//页面没有发生变化
                }
                lastPageKey = thisPageKey;
                //在判断页面是否是新页面：
                if (!pageMap.containsKey(thisPageKey)){
                    dataMap.get(packageName).pageNumberPlus();//是新页面
                    pageMap.put(thisPageKey,packageName);
                }
            }
        }  catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (reader != null)
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
        }
        JSONArray jsonArray=new JSONArray();
        for (Map.Entry<String, UIAutomatorData> entry : dataMap.entrySet()) {
            UIAutomatorData testData = entry.getValue();
            JSONObject jsonObject=new JSONObject();
            jsonObject.put("packageName",testData.getPackageName());
            jsonObject.put("pageNumber",testData.getPageNumber());
            jsonObject.put("totalTime",(int)testData.getTotalTime());
            jsonObject.put("changeTime",(int)testData.getChangeTime());
            jsonArray.add(jsonObject);
        }
        return jsonArray;
    }

    /**
     * 将JSONArray转换成
     * @param jsonArray
     * @param context
     * @return
     */
    public static String getReportStringWithAppName(JSONArray jsonArray, Context context){

        String result = "";

        StringBuilder builder = new StringBuilder();
        for (int i=0;jsonArray!=null&&i<jsonArray.size();i++){
            JSONObject thisJson = (JSONObject) jsonArray.get(i);
            String packageName = thisJson.getString("packageName");
            int pageNumber = thisJson.getIntValue("pageNumber");
            double totalTime = thisJson.getDouble("totalTime");
            double changeTime = thisJson.getDouble("changeTime");

            builder.append("<涉及应用：").append(PackageTool.getAPPName(packageName,context)) .append(System.getProperty("line.separator"))
                    .append("  应用内页面数：").append(pageNumber).append(System.getProperty("line.separator"))
                    .append("  应用内总时长：").append(TimerTool.formatSecond(totalTime)).append(System.getProperty("line.separator"))
                    .append("  应用内操作时长：").append(TimerTool.formatSecond(changeTime)) .append(">") .append(System.getProperty("line.separator"));
        }
        try {
            result = new String(builder.toString().getBytes(), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return  result;
    }

    /**
     * 生成报告文件
     * @param reportStr
     * @return
     */
    public static File makeReportFile(String reportStr) {

        //数据信息：
        File result = null;
        if (TextUtils.isEmpty(reportStr)) {
            reportStr = "";
        }
        //添加设备信息：
        String deviceInfo = "测试机型：" + Build.MANUFACTURER + " " + Build.MODEL + "\r\n"
                            + "测试日期：" +TimerTool.getYearMonthDay() + "\r\n";
        //写入文件
        String fileString = deviceInfo+reportStr;
        try {
            File save = new File(Constant.UIAutomatorReportFilePath);
            File parentPath = new File(Constant.UIAUTOMATOR_PATH);
            if (!parentPath.exists())
                parentPath.mkdirs();
            if (save.exists()) {
                save.deleteOnExit();
                save.createNewFile();
            }else {
                save.createNewFile();
            }
            FileOutputStream fos = new FileOutputStream(save);
            fos.write(fileString.getBytes());
            fos.close();
            result = save;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

}
