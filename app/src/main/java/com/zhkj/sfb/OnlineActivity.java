package com.zhkj.sfb;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.zhkj.sfb.common.BasetActivity;

public class OnlineActivity extends BasetActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_online);
        TextView theme = (TextView) findViewById(R.id.theme);
        theme.setText("电商服务");
    }
}
