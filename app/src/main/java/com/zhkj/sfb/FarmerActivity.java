package com.zhkj.sfb;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;
import com.google.gson.reflect.TypeToken;
import com.zhkj.sfb.adapter.FarmerCityAdapter;
import com.zhkj.sfb.common.BasetActivity;
import com.zhkj.sfb.common.CommonBean;
import com.zhkj.sfb.common.OutJsonUtil;
import com.zhkj.sfb.common.ServiceUtil;
import com.zhkj.sfb.pojo.CityPojo;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
public class FarmerActivity extends BasetActivity {
    private List<CityPojo> cityPojos;
    private RecyclerView cityView;
    private RecyclerView areaView;
    private RecyclerView townshipView;
    private RecyclerView villageView;
    private static String URL="http://shifei.yungoux.com/zhkj/getCity.do";
    private int flag=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_farmer);
        TextView theme = (TextView) findViewById(R.id.theme);
        theme.setText("农户施肥");
        cityView = (RecyclerView)findViewById(R.id.city_list);
        areaView = (RecyclerView) findViewById(R.id.area_list);
        townshipView = (RecyclerView) findViewById(R.id.township_list);
        villageView =  (RecyclerView)findViewById(R.id.village_list);
        Intent intent = getIntent();
        flag = intent.getIntExtra("flag",10);
        new Thread(){
            @Override
            public void run() {
                super.run();
                new FarmerActivity.CityTask().execute();
            }
        }.start();
    }
    public class CityTask extends AsyncTask<Void, Void, String> {
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if(cityPojos!=null && cityPojos.size()>0){
                cityView.setLayoutManager(new LinearLayoutManager(getParent()));
                FarmerCityAdapter cityAdapter = new FarmerCityAdapter(FarmerActivity.this,cityPojos,areaView,townshipView,villageView,flag);
                cityView.setHasFixedSize(true);
                cityView.setAdapter(cityAdapter);
            }else{
            }
        }
        @Override
        protected String doInBackground(Void... param) {
            try {
                String result = ServiceUtil.sendPostRequest(URL,"");
                CommonBean commonBeans = OutJsonUtil.json2Bean(result,CommonBean.class);
                String infos = OutJsonUtil.toJson(commonBeans.getInfo());
                if(commonBeans.getStatus()==200){
                    Type type = new TypeToken<ArrayList<CityPojo>>() {}.getType();
                    cityPojos = OutJsonUtil.json2List(infos,type);
                }
                return  result;
            }catch (Exception e){
                e.printStackTrace();
            }
            return null;
        }
    }
}
