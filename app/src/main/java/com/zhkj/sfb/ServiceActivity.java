package com.zhkj.sfb;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zhkj.sfb.common.BasetActivity;

public class ServiceActivity extends BasetActivity {
    private RelativeLayout centerLayout,expertLayout,serviceLayout,featureLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service);
        TextView theme = (TextView) findViewById(R.id.theme);
        theme.setText("服务指南");
        centerLayout = (RelativeLayout)findViewById(R.id.service_layout_introduce);
        expertLayout = (RelativeLayout)findViewById(R.id.service_layout_expert);
        serviceLayout = (RelativeLayout)findViewById(R.id.service_layout_work);
        featureLayout= (RelativeLayout)findViewById(R.id.service_layout_feature);
    }
    public  void toCenter(View v){
        //Intent intent = new Intent(ServiceActivity.this,CenterProfileActivity.class);
        Intent intent = new Intent(ServiceActivity.this,CityActivity.class);
        intent.putExtra("flag",2);
        startActivity(intent);

    }
    public void toExpert(View v){
        //Intent intent = new Intent(ServiceActivity.this,ExpertActivity.class);
        Intent intent = new Intent(ServiceActivity.this,CityActivity.class);
        intent.putExtra("flag",3);
        startActivity(intent);
    }
    public void toService(View v){
        //Intent intent = new Intent(ServiceActivity.this,ServiceGuideActivity.class);
        Intent intent = new Intent(ServiceActivity.this,CityActivity.class);
        intent.putExtra("flag",4);
        startActivity(intent);
    }
    public void toFeature(View v){
        //Intent intent = new Intent(ServiceActivity.this,ServiceGuideActivity.class);
        Intent intent = new Intent(ServiceActivity.this,CityActivity.class);
        intent.putExtra("flag",5);
        startActivity(intent);
    }
}
