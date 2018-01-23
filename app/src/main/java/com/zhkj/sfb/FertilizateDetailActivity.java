package com.zhkj.sfb;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import com.google.gson.reflect.TypeToken;
import com.zhkj.sfb.common.BasetActivity;
import com.zhkj.sfb.common.CommonBean;
import com.zhkj.sfb.common.DeviceUuidFactory;
import com.zhkj.sfb.common.OutJsonUtil;
import com.zhkj.sfb.common.ServiceUtil;
import com.zhkj.sfb.pojo.CropPojo;
import com.zhkj.sfb.pojo.FertilityInfoPojo;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
public class FertilizateDetailActivity extends BasetActivity {
private  String landNum;
 private  String  elementP;
    private String elementK;
    private String elementN;
    private String elementO;
    private String fertilityName;
    private Integer fertilityId;
    private String fertilityDetail;
    private String amount;
    private String address;
    private FertilityInfoPojo fertilityInfoPojo;
    private TextView elemento0View,elementp0View,elementn0View,elementk0View,amountView, elemento1View,elementp1View,elementn1View,elementk1View;
    private TextView elemento2View,elementp2View,elementn2View,elementk2View, elemento3View,elementp3View,elementn3View,elementk3View,elemento4View,elementp4View,elementn4View,elementk4View;
   private TextView bzView,telView,proView,nameVIew;
    private String telphone;
    private Integer areaId;
    private String URL ="http://shifei.yungoux.com/zhkj/getFertilityInfo.do";
    private String URL2 ="http://shifei.yungoux.com/zhkj/saveHistory.do";
    private static String URL_CROP="http://shifei.yungoux.com/zhkj/getCrop.do";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fertilizate_detail);
        TextView theme = (TextView) findViewById(R.id.theme);
        theme.setText("配方详情");
        amountView = (TextView) findViewById(R.id.fertilite_detail_amount);
        elemento0View = (TextView) findViewById(R.id.fertilite_detail_result01);
        elementn0View = (TextView) findViewById(R.id.fertilite_detail_result02);
        elementp0View = (TextView) findViewById(R.id.fertilite_detail_result03);
        elementk0View = (TextView) findViewById(R.id.fertilite_detail_result04);
        elemento1View = (TextView) findViewById(R.id.fertilite_detail_result11);
        elementn1View = (TextView) findViewById(R.id.fertilite_detail_result12);
        elementp1View = (TextView) findViewById(R.id.fertilite_detail_result13);
        elementk1View = (TextView) findViewById(R.id.fertilite_detail_result14);
        elemento2View = (TextView) findViewById(R.id.fertilite_detail_result21);
        elementn2View = (TextView) findViewById(R.id.fertilite_detail_result22);
        elementp2View = (TextView) findViewById(R.id.fertilite_detail_result23);
        elementk2View = (TextView) findViewById(R.id.fertilite_detail_result24);
        elemento3View = (TextView) findViewById(R.id.fertilite_detail_result31);
        elementn3View = (TextView) findViewById(R.id.fertilite_detail_result32);
        elementp3View = (TextView) findViewById(R.id.fertilite_detail_result33);
        elementk3View = (TextView) findViewById(R.id.fertilite_detail_result34);
        elemento4View = (TextView) findViewById(R.id.fertilite_detail_result41);
        elementn4View = (TextView) findViewById(R.id.fertilite_detail_result42);
        elementp4View = (TextView) findViewById(R.id.fertilite_detail_result43);
        elementk4View = (TextView) findViewById(R.id.fertilite_detail_result44);
        bzView = (TextView) findViewById(R.id.beizhu);
        proView =(TextView)findViewById(R.id.proportion_detail);
        telView = (TextView) findViewById(R.id.tel_detail_1);
        nameVIew= (TextView)findViewById(R.id.nzw_name);
        Intent intent = getIntent();
        landNum=   intent.getStringExtra("landNum");
         amount=intent.getStringExtra("amount");
        fertilityDetail=intent.getStringExtra("fertilityDetail");
        fertilityId = intent.getIntExtra("fertilityId",0);
        fertilityName = intent.getStringExtra("fertilityName");
        telphone= intent.getStringExtra("telphone");
        areaId = intent.getIntExtra("areaId",0);
        telView.setText("施肥咨询热线： "+telphone);
        telView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!telphone.equals("")) {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:" + telphone));
                    startActivity(intent);
                }
            }
        });
            bzView.setText(fertilityDetail);
            nameVIew.setText(fertilityName);
        new Thread(){
            @Override
            public void run() {
                super.run();
                new FertilizatedetailTask().execute();
            }
        }.start();
    }
    public class  FertilizatedetailTask extends AsyncTask<Void,Void,String>{
        @Override
        protected String doInBackground(Void... p) {
            //查询元素计算结果
            try {
                //查询土壤肥力
           List<CropPojo>    cropPojos = new ArrayList<CropPojo>();
                String str="landNum="+landNum;
                String result = ServiceUtil.sendPostRequest(URL_CROP,str);
                CommonBean commonCrop = OutJsonUtil.json2Bean(result,CommonBean.class);
                String cropinfos = OutJsonUtil.toJson(commonCrop.getInfo());
                if(commonCrop.getStatus()==200){
                    Type type = new TypeToken<ArrayList<CropPojo>>() {}.getType();
                    cropPojos = OutJsonUtil.json2List(cropinfos,type);
                    elementO = cropPojos.get(0).getElementO();
                    elementP = cropPojos.get(0).getElementP();
                     elementK =cropPojos.get(0).getElementK();
                     elementN = cropPojos.get(0).getElementN();
                     address = cropPojos.get(0).getVillageName();
                }

                String params1 ="cropType="+fertilityId+"&target="+amount+"&nAttach="+elementN+"&pAttach="+elementP+"&kAttach="+elementK+"&yAttach="+elementO+"&areaId="+areaId;
                String result1 = ServiceUtil.sendPostRequest(URL,params1);
                CommonBean commonBeans = OutJsonUtil.json2Bean(result1,CommonBean.class);
                if(commonBeans.getStatus()==200){
                    String infos = OutJsonUtil.toJson(commonBeans.getInfo());
                    fertilityInfoPojo = OutJsonUtil.json2Bean(infos,FertilityInfoPojo.class);
                }
                new Thread(){
                    @Override
                    public void run() {
                        super.run();
                        new SaveHistoryTask().execute();
                    }
                }.start();
            }catch (Exception e){
                e.printStackTrace();
            }
            return null;
        }
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            amountView.setText(amount);
            if (fertilityInfoPojo!=null) {
                String[] farmyard = fertilityInfoPojo.getElementO().split(",");
                String[] nitrogen = fertilityInfoPojo.getElementN().split(",");
                String[] phosphorus = fertilityInfoPojo.getElementP().split(",");
                String[] potassium = fertilityInfoPojo.getElementK().split(",");
                    elemento0View.setText(farmyard[1]);
                    elemento1View.setText(farmyard[2]);
                    elemento2View.setText(farmyard[3]);
                    elemento3View.setText(farmyard[4]);
                    elemento4View.setText(farmyard[5]);
                    elementn0View.setText(nitrogen[1]);
                    elementn1View.setText(nitrogen[2]);
                    elementn2View.setText(nitrogen[3]);
                    elementn3View.setText(nitrogen[4]);
                    elementn4View.setText(nitrogen[5]);
                    elementp0View.setText(phosphorus[1]);
                    elementp1View.setText(phosphorus[2]);
                    elementp2View.setText(phosphorus[3]);
                    elementp3View.setText(phosphorus[4]);
                    elementp4View.setText(phosphorus[5]);
                    elementk0View.setText(potassium[1]);
                    elementk1View.setText(potassium[2]);
                    elementk2View.setText(potassium[3]);
                    elementk3View.setText(potassium[4]);
                    elementk4View.setText(potassium[5]);
                    proView.setText(fertilityInfoPojo.getBili());
            }
        }
    }
    public  class SaveHistoryTask extends  AsyncTask<Void,Void,String>{

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
        }

        @Override
        protected String doInBackground(Void... params) {
            String elementParam1=null;
            String elementParam2=null;
            if (fertilityInfoPojo!=null) {
                String[] farmyard = fertilityInfoPojo.getElementO().split(",");
                String[] nitrogen = fertilityInfoPojo.getElementN().split(",");
                String[] phosphorus = fertilityInfoPojo.getElementP().split(",");
                String[] potassium = fertilityInfoPojo.getElementK().split(",");
                elementParam1= "&farmyard="+farmyard[1]+"&nitrogen="+nitrogen[1]+"&phosphorus="+phosphorus[1]+"&potassium="+potassium[1];
                elementParam2="&elemento1="+farmyard[2]+"&elementn1="+nitrogen[2]+"&elementp1="+phosphorus[2]+"&elementk1="+potassium[2]+"&elemento2="+farmyard[3]+"&elementn2="+nitrogen[3]+"&elementp2="+phosphorus[3]+"&elementk2="+potassium[3]
                        +"&elemento3="+farmyard[4]+"&elementn3="+nitrogen[4]+"&elementp3="+phosphorus[4]+"&elementk3="+potassium[4]  +"&elemento4="+farmyard[5]+"&elementn4="+nitrogen[5]+"&elementp4="+phosphorus[5]+"&elementk4="+potassium[5];
            }
            DeviceUuidFactory deviceUuidFactory = new DeviceUuidFactory(FertilizateDetailActivity.this);
            String title = fertilityName+"施肥配方";
            String farmyard = fertilityInfoPojo.getElementO().split(",")[1];
            String nitrogen =fertilityInfoPojo.getElementN().split(",")[1];
            String phosphorus = fertilityInfoPojo.getElementP().split(",")[1];
            String potassium = fertilityInfoPojo.getElementK().split(",")[1];
            String params2 ="title="+title+"&address="+address+"&no="+landNum+elementParam1+"&amount="+amount+"&mobileId="+ deviceUuidFactory.getDeviceUuid().toString()+elementParam2+"&telphone="+telphone+"&proportion="+fertilityInfoPojo.getBili()+"description="+fertilityDetail;
            ServiceUtil.sendPostRequest(URL2,params2);
            return null;
        }
    }

}
