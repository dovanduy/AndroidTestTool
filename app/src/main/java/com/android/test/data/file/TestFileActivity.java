package com.android.test.data.file;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;


import com.android.test.R;

import java.io.File;
import java.io.IOException;

public class TestFileActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_file);



        Toast.makeText(this,"--"+Environment.getExternalStorageDirectory(),Toast.LENGTH_LONG).show();
        Log.e("sss",Environment.getExternalStorageDirectory().getAbsolutePath());

        File file = new File("/data/local/tmp/view.xml");
        if (file.exists()){
            Toast.makeText(this,"我草",Toast.LENGTH_LONG).show();
        }else {
            Toast.makeText(this,"--",Toast.LENGTH_LONG).show();
        }



        String path = Environment.getExternalStorageDirectory() + File.separator + "Utest" +File.separator +"p.apk";
        Log.e("elvis",path);
        File a = new File(path);
        if (!a.exists()){
            try {
                a.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
}
