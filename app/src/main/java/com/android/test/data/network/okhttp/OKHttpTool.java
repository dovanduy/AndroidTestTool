package com.android.test.data.network.okhttp;

import android.os.Environment;
import android.os.Message;
import android.util.Log;

import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by elvis on 2016/9/26.
 */
public class OKHttpTool {






    public static void download(final String filePath, String url){
        Request request = new Request.Builder().url(url).build();
        OkHttpClient mOkHttpClient = new OkHttpClient();
        mOkHttpClient.newCall(request).enqueue(new Callback() {
            @SuppressWarnings("resource")
            @Override
            public void onResponse(Response response) throws IOException {
                InputStream is = null;
                byte[] buf = new byte[2048];
                int len = 0;
                FileOutputStream fos = null;
                try {
                    is = response.body().byteStream();
                    long total = response.body().contentLength();
                    File file = new File(filePath);
                    if (!file.exists()){
                        Log.e("elvis",file.getAbsolutePath());
                        file.createNewFile();
                    }
                    fos = new FileOutputStream(file);
                    long sum = 0;
                    while ((len = is.read(buf)) != -1) {
                        fos.write(buf, 0, len);
                        sum += len;
                        int progress = (int) (sum * 1.0f / total * 100);
                        Log.e("h_bl", "progress=" + progress);
//                        Message msg = mHandler.obtainMessage();
//                        msg.what = 1;
//                        msg.arg1 = progress;
//                        mHandler.sendMessage(msg);
                    }
                    fos.flush();
                    Log.e("h_bl", "文件下载成功");
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.e("h_bl", "文件下载失败");
                } finally {
                    try {
                        if (is != null)
                            is.close();
                    } catch (IOException e) {
                    }
                    try {
                        if (fos != null)
                            fos.close();
                    } catch (IOException e) {
                    }
                }
            }

            @Override
            public void onFailure(Request arg0, IOException arg1) {
                Log.d("h_bl", "onFailure");
            }
        });}
}
