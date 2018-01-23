package com.zhkj.sfb;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.zhkj.sfb.common.BasetActivity;

public class ForumActivity extends BasetActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forum);
        TextView theme = (TextView) findViewById(R.id.theme);
        theme.setText("施肥讲堂");
    }
    public  void toOne(View v){
        Intent intent = new Intent(ForumActivity.this,ForumOneActivity.class);
        startActivity(intent);
    }
    public  void toTwo(View v){
        Intent intent = new Intent(ForumActivity.this,ForumTwoActivity.class);
        startActivity(intent);
    }
    public  void toThree(View v){
        Intent intent = new Intent(ForumActivity.this,ForumThreeActivity.class);
        startActivity(intent);
    }
    public  void toFour(View v){
        Intent intent = new Intent(ForumActivity.this,ForumFourActivity.class);
        startActivity(intent);
    }
    public  void toFive(View v){
        Intent intent = new Intent(ForumActivity.this,PolicyActivity.class);
        startActivity(intent);
    }
}
