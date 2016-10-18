package com.android.test.log;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.android.test.R;

public class TestLogToolActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_log_tool);

        Button oneButton = (Button) findViewById(R.id.but_one);
        oneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LogTool.makeLog(LogTool.LEVEL.Level_e,"aaa","cacacac");
            }
        });
    }
}
