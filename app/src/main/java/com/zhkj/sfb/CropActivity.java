package com.zhkj.sfb;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.google.gson.reflect.TypeToken;
import com.zhkj.sfb.common.BasetActivity;
import com.zhkj.sfb.common.CommonBean;
import com.zhkj.sfb.common.OutJsonUtil;
import com.zhkj.sfb.common.ServiceUtil;
import com.zhkj.sfb.pojo.CropPojo;
import com.zhkj.sfb.pojo.FertilityCropPojo;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
public class CropActivity extends BasetActivity {
    private List<FertilityCropPojo> fertilityCropPojos;
    private List<CropPojo> cropPojos;
    private String landNum;
    private String farmerName;
    private String elementO;
    private String elementP;
    private String elementN;
    private String elementK;
    private String telphone;
    private Integer areaId;
    private static String URL="http://shifei.yungoux.com/zhkj/getFertility.do";
    private Spinner spinner;
    private String className;
    private String data;
    private TextView farmerView,landView,fertilityView,amountView;
    private TextView oView,nView,pView,kView;
    private String  fertility=null;
    private Integer fertilityId;
    private String fertilityDetail;
    private String fertilityName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crop);
        TextView theme = (TextView) findViewById(R.id.theme);
        theme.setText("农户施肥");
        Intent intent = getIntent();
        ArrayList<String> dataInfo = intent.getStringArrayListExtra("dataInfo");
        landNum = dataInfo.get(0);
        farmerName= dataInfo.get(1);
        elementO =dataInfo.get(2);
        elementN= dataInfo.get(3);
        elementP =dataInfo.get(4);
        elementK =dataInfo.get(5);
        telphone= dataInfo.get(6);
        areaId = Integer.parseInt(dataInfo.get(7));
        farmerView= (TextView) findViewById(R.id.farmer_value);
        amountView= (TextView) findViewById(R.id.amount_value);
        farmerView.setText(farmerName);
        landView =(TextView) findViewById(R.id.plot_value);
        landView.setText(landNum);
        oView= (TextView) findViewById(R.id.element_result1);
        nView=(TextView) findViewById(R.id.element_result2);
        pView = (TextView) findViewById(R.id.element_result3);
        kView= (TextView) findViewById(R.id.element_result4);
        oView.setText(elementO);
        pView.setText(elementP);
        nView.setText(elementN);
        kView.setText(elementK);
        spinner = (Spinner)findViewById(R.id.fertility_value);
new Thread(){
    @Override
    public void run() {
        super.run();
     new FertilityTask().execute();
    }
}.start();
        fertilityView=(TextView) findViewById(R.id.fertility_data);
        //查询农作物类型
          spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                spinner.setSelection(position, true);
                fertilityId =((FertilityCropPojo)spinner.getSelectedItem()).getId();
                fertilityDetail= ((FertilityCropPojo)spinner.getSelectedItem()).getDescription();
                fertilityName = ((FertilityCropPojo) spinner.getSelectedItem()).getName();

            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        fertilityView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                data =amountView.getText().toString();
                if (data.equals("") || data==null){
                    Toast.makeText(CropActivity.this,"请填写目标产量",Toast.LENGTH_LONG);
                    return;
                }

                Intent intent = new Intent(CropActivity.this,FertilizateDetailActivity.class);
                intent.putExtra("landNum",landNum);
                intent.putExtra("telphone",telphone);
                intent.putExtra("areaId",areaId);
                intent.putExtra("fertilityId",fertilityId);
                intent.putExtra("fertilityName",fertilityName);
                intent.putExtra("fertilityDetail",fertilityDetail);
                intent.putExtra("amount",amountView.getText().toString());
                startActivity(intent);
            }
        });
    }



    public  class FertilityTask extends  AsyncTask<Void,Void,String>{
        @Override
        protected String doInBackground(Void... params) {
            try {
                String param = "areaId="+areaId;
                fertilityCropPojos = new ArrayList<FertilityCropPojo>();
                String result = ServiceUtil.sendPostRequest(URL,param);
                CommonBean commonBeans = OutJsonUtil.json2Bean(result,CommonBean.class);
                String infos = OutJsonUtil.toJson(commonBeans.getInfo());
                if(commonBeans.getStatus()==200){
                    Type type = new TypeToken<ArrayList<FertilityCropPojo>>() {}.getType();
                    fertilityCropPojos = OutJsonUtil.json2List(infos,type);
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
            if (fertilityCropPojos!=null && fertilityCropPojos.size()>0){
                ArrayAdapter<FertilityCropPojo> adapter = new ArrayAdapter<FertilityCropPojo>(CropActivity.this,
                        android.R.layout.simple_spinner_item, fertilityCropPojos);
                spinner.setAdapter(adapter);
            }
        }
    }
    }

