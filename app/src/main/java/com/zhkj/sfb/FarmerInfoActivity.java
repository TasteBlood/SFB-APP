package com.zhkj.sfb;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.reflect.TypeToken;
import com.zhkj.sfb.adapter.FarmerAdapter;
import com.zhkj.sfb.common.BasetActivity;
import com.zhkj.sfb.common.CommonBean;
import com.zhkj.sfb.common.OutJsonUtil;
import com.zhkj.sfb.common.ServiceUtil;
import com.zhkj.sfb.pojo.FarmerPojo;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class FarmerInfoActivity extends BasetActivity {
    private List<FarmerPojo> farmerPojos;
    private RecyclerView farmerInfoView;
    private RecyclerView plotView;
    private Integer villageId;
    private String telphone;
    private Integer areaId ;
    private static String URL="http://shifei.yungoux.com/zhkj/getFarmer.do";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_farmer_info);
        TextView theme = (TextView) findViewById(R.id.theme);
        farmerInfoView = (RecyclerView) findViewById(R.id.farmer_info_list);
        plotView = (RecyclerView) findViewById(R.id.plot_list);
        theme.setText("农户施肥");
        Intent intent = getIntent();
        String villageIdStr = intent.getStringExtra("villageId");
        telphone = intent.getStringExtra("telphone");
        areaId = intent.getIntExtra("areaId",0);
        villageId = Integer.parseInt(villageIdStr);
        new Thread(){
            @Override
            public void run(){
                super.run();
                new FarmerInfoTask().execute();
            }
        }.start();
    }


    public class FarmerInfoTask extends AsyncTask<Void,Void,String>{


        @Override
        protected String doInBackground(Void... params) {
            try {
                farmerPojos = new ArrayList<FarmerPojo>();
                String str="villageId="+villageId;
                String result = ServiceUtil.sendPostRequest(URL,str);
                CommonBean commonBeans = OutJsonUtil.json2Bean(result,CommonBean.class);
                String infos = OutJsonUtil.toJson(commonBeans.getInfo());
                if(commonBeans.getStatus()==200){
                    Type type = new TypeToken<ArrayList<FarmerPojo>>() {}.getType();
                    farmerPojos = OutJsonUtil.json2List(infos,type);
                }
                return  result;
            }catch (Exception e){
                e.printStackTrace();
            }


            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (farmerPojos!=null && farmerPojos.size()>0) {
                farmerInfoView.setLayoutManager(new LinearLayoutManager(getParent()));
                FarmerAdapter farmerAdapter = new FarmerAdapter(FarmerInfoActivity.this,farmerPojos,plotView,telphone,areaId);
                farmerInfoView.setHasFixedSize(true);
                farmerInfoView.setAdapter(farmerAdapter);
            }else{
                Toast toast = Toast.makeText(FarmerInfoActivity.this, "没有农户信息", Toast.LENGTH_SHORT);
                toast.show();
            }
        }
    }
}
