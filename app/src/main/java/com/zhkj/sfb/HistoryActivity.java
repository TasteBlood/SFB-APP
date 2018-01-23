package com.zhkj.sfb;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.TextView;

import com.zhkj.sfb.adapter.HistoryAdapter;
import com.zhkj.sfb.common.BasetActivity;
import com.zhkj.sfb.common.CommonBean;
import com.zhkj.sfb.common.DeviceUuidFactory;
import com.zhkj.sfb.common.OutJsonUtil;
import com.zhkj.sfb.common.ServiceUtil;
import com.zhkj.sfb.pojo.HistoryPojo;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class HistoryActivity extends BasetActivity {
    private RecyclerView historyRecyclerView;
    private HistoryAdapter historyAdapter;
    private List<HistoryPojo> historyPojos;
    private final static String URL = "http://shifei.yungoux.com/zhkj/getHistories.do";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        TextView theme = (TextView) findViewById(R.id.theme);
        theme.setText("历史配方");
        historyRecyclerView = (RecyclerView)findViewById(R.id.history_list);
        new Thread(){
            @Override
            public void run() {
                new HistoryTask().execute();
            }
        }.start();
    }
    //异步查询点子列表
    private class HistoryTask extends AsyncTask<Void, Void, String> {
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if(historyPojos!=null && historyPojos.size()>0){
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getParent());
                linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                historyRecyclerView.setLayoutManager(linearLayoutManager);
                historyAdapter = new HistoryAdapter(HistoryActivity.this,historyPojos);
                historyRecyclerView.setHasFixedSize(true);
                historyRecyclerView.setAdapter(historyAdapter);
            }

        }

        @Override
        protected String doInBackground(Void... param) {
            try {
                DeviceUuidFactory deviceUuidFactory = new DeviceUuidFactory(HistoryActivity.this);

                Log.d("mobileId======", deviceUuidFactory.getDeviceUuid().toString());
                String params = "mobileId="+deviceUuidFactory.getDeviceUuid().toString();
                String results = ServiceUtil.sendPostRequest(URL,params);
                CommonBean commonBeans = OutJsonUtil.json2Bean(results,CommonBean.class);
                if(commonBeans.getStatus()==200){
                    String infos = OutJsonUtil.toJson(commonBeans.getInfo());
                    JSONArray jsonArray = new JSONArray(infos);
                    historyPojos = new ArrayList<HistoryPojo>();
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                        String title = jsonObject.getString("title");
                        String time = jsonObject.getString("createTime");
                        String address = jsonObject.getString("address");
                        String no = jsonObject.getString("no");
                        Integer id = jsonObject.getInt("id");
                        HistoryPojo historyPojo = new HistoryPojo();
                        historyPojo.setTitle(title);
                        historyPojo.setCreateTime(time);
                        historyPojo.setAddress(address);
                        historyPojo.setNo(no);
                        historyPojo.setId(id);
                        historyPojos.add(historyPojo);
                    }
                }
                return  null;
            }catch (Exception e){
                e.printStackTrace();
            }
            return null;
        }
    }
}
