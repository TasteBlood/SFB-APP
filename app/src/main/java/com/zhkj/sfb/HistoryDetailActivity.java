package com.zhkj.sfb;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.TextView;
import com.zhkj.sfb.common.BasetActivity;
import com.zhkj.sfb.common.CommonBean;
import com.zhkj.sfb.common.OutJsonUtil;
import com.zhkj.sfb.common.ServiceUtil;
import com.zhkj.sfb.pojo.HistoryPojo;

public class HistoryDetailActivity extends BasetActivity {
    private static String URL="http://shifei.yungoux.com/zhkj/getHistoryDetail.do";
    private int historyId;
    private HistoryPojo historyPojo;
    private TextView amountView,farmerView,NView,PView,KView;
    private TextView o1View,n1View,p1View,k1View, o2View,n2View,p2View,k2View, o3View,n3View,p3View,k3View,o4View,n4View,p4View,k4View;
    private TextView telView,bzView,proView,nameView;
    private String telephone;
    private String nzwDetail;
    private String nzwName;
    private String flag;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_detail);
        TextView theme = (TextView) findViewById(R.id.theme);
        theme.setText("配方详情");
        amountView = (TextView)findViewById(R.id.history_detail_amount);
        farmerView = (TextView)findViewById(R.id.history_detail_result11);
        NView = (TextView)findViewById(R.id.history_detail_result21);
        PView = (TextView)findViewById(R.id.history_detail_result31);
        KView = (TextView)findViewById(R.id.history_detail_result41);

        o1View = (TextView)findViewById(R.id.history_detail_result12);
        n1View = (TextView)findViewById(R.id.history_detail_result22);
        p1View = (TextView)findViewById(R.id.history_detail_result32);
        k1View = (TextView)findViewById(R.id.history_detail_result42);

        o2View = (TextView)findViewById(R.id.history_detail_result13);
        n2View = (TextView)findViewById(R.id.history_detail_result23);
        p2View = (TextView)findViewById(R.id.history_detail_result33);
        k2View = (TextView)findViewById(R.id.history_detail_result43);

        o3View = (TextView)findViewById(R.id.history_detail_result14);
        n3View = (TextView)findViewById(R.id.history_detail_result24);
        p3View = (TextView)findViewById(R.id.history_detail_result34);
        k3View = (TextView)findViewById(R.id.history_detail_result44);

        o4View = (TextView)findViewById(R.id.history_detail_result15);
        n4View = (TextView)findViewById(R.id.history_detail_result25);
        p4View = (TextView)findViewById(R.id.history_detail_result35);
        k4View = (TextView)findViewById(R.id.history_detail_result45);
        telView = (TextView) findViewById(R.id.tel_detail);
        bzView = (TextView) findViewById(R.id.beizhu);
        nameView = (TextView) findViewById(R.id.nzw_name_text1);
        proView =(TextView) findViewById(R.id.proportion_detail);
              Intent intent = getIntent();
              telephone=intent.getStringExtra("telephone");
              nzwDetail=intent.getStringExtra("nzwDetail");
               nzwName = intent.getStringExtra("nzwName");
                historyId= intent.getIntExtra("historyId",0);
                flag= intent.getStringExtra("flag");
        new Thread(){
            @Override
            public void run() {
                super.run();
                new HistoryTask().execute();
            }
        }.start();
    }
    public class HistoryTask extends AsyncTask<Void, Void, String> {
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if(historyPojo!=null){
                amountView.setText(historyPojo.getAmount().toString());
                farmerView.setText(historyPojo.getFarmyard()+"");
                NView.setText(historyPojo.getNitrogen()+"");
                PView.setText(historyPojo.getPhosphorus()+"");
                KView.setText(historyPojo.getPotassium()+"");
                o1View.setText(historyPojo.getElemento1());
                o2View.setText(historyPojo.getElemento2());
                o3View.setText(historyPojo.getElemento3());
                o4View.setText(historyPojo.getElemento4());
                p1View.setText(historyPojo.getElementp1());
                p2View.setText(historyPojo.getElementp2());
                p3View.setText(historyPojo.getElementp3());
                p4View.setText(historyPojo.getElementp4());
                n1View.setText(historyPojo.getElementn1());
                n2View.setText(historyPojo.getElementn2());
                n3View.setText(historyPojo.getElementn3());
                n4View.setText(historyPojo.getElementn4());
                k1View.setText(historyPojo.getElementk1());
                k2View.setText(historyPojo.getElementk2());
                k3View.setText(historyPojo.getElementk3());
                k4View.setText(historyPojo.getElementk4());
                proView.setText(historyPojo.getProportion());
                nameView.setText(nzwName);
                if (flag==null || flag.equals("")|| flag.equals("null")|| !flag.equals("localtion")) {
                    telephone = historyPojo.getTelphone();
                    String title = historyPojo.getTitle().replace("施肥配方", "");
                    nameView.setText(title);
                }
                if(null!=nzwDetail&&!"".equals(nzwDetail))
                    bzView.setText(nzwDetail);
                else
                    bzView.setText(historyPojo.getDescription());
                if (telephone==null || telephone.equals("") || telephone.equals("null")){
                    telView.setText("   施肥咨询热线:");
                }else{
                    telView.setText("   施肥咨询热线： "+telephone);
                }
                telView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!telephone.equals("")){
                            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:"+telephone));
                            startActivity(intent);
                        }
                    }
                });
            }
        }
        @Override
        protected String doInBackground(Void... param) {
            try {
                String params ="id="+historyId;
                String result = ServiceUtil.sendPostRequest(URL,params);
                CommonBean commonBeans = OutJsonUtil.json2Bean(result,CommonBean.class);
                if(commonBeans.getStatus()==200){
                    String infos = OutJsonUtil.toJson(commonBeans.getInfo());
                    historyPojo = OutJsonUtil.json2Bean(infos,HistoryPojo.class);
                }
                return  result;
            }catch (Exception e){
                e.printStackTrace();
            }
            return null;
        }
    }
}
