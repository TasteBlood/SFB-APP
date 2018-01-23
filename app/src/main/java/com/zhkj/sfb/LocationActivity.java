package com.zhkj.sfb;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.PolylineOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.model.LatLngBounds;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.district.DistrictResult;
import com.baidu.mapapi.search.district.DistrictSearch;
import com.baidu.mapapi.search.district.DistrictSearchOption;
import com.baidu.mapapi.search.district.OnGetDistricSearchResultListener;
import com.google.gson.reflect.TypeToken;
import com.zhkj.sfb.common.BasetActivity;
import com.zhkj.sfb.common.CommonBean;
import com.zhkj.sfb.common.DBWGCUtil;
import com.zhkj.sfb.common.DeviceUuidFactory;
import com.zhkj.sfb.common.OutJsonUtil;
import com.zhkj.sfb.common.ServiceUtil;
import com.zhkj.sfb.pojo.AreaPojo;
import com.zhkj.sfb.pojo.FertilityCropPojo;
import com.zhkj.sfb.pojo.FertilityInfoPojo;
import com.zhkj.sfb.pojo.JsonPojo;
import com.zhkj.sfb.pojo.TownshipPojo;
import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class LocationActivity extends BasetActivity implements OnGetDistricSearchResultListener,ImageView.OnClickListener{
    private MapView mapView;
    private BaiduMap baiduMap;
    private MyLocationConfiguration.LocationMode mCurrentMode;
    public MyLocationListenner myListener = new MyLocationListenner();
    // 定位相关
    LocationClient mLocClient;
    boolean isFirstLoc = true; // 是否首次定位

    private String URL ="http://118.190.91.122:6080/arcgis/rest/services/249MapService/MapServer/0/query?";
    private String URL1 ="http://shifei.yungoux.com/zhkj/getGisUrl.do";
    private String URL2 ="http://shifei.yungoux.com/zhkj/saveHistory.do";
    private static String URL_F="http://shifei.yungoux.com/zhkj/getFertility.do";
    private String gisUrl;
    private PopupWindow mPopupWindow;
    private Spinner spinner;
    private List<FertilityCropPojo> fertilityCropPojos;
    private String city;
    private Integer fertilityId;
    private String fertilityDetail;
    private String fertilityName;
    //获取经纬度
    double latitude=0;
    double longitude=0;
    private String searchName;
    private TownshipPojo townshipPojo;
    private DistrictSearch mDistrictSearch;
    private ImageView imageView;
    private EditText searchView;
    private TextView region1View,region2View,region3View,region4View,region5View,dataAmountView,zdgxView,telView;
    private String className;
    private double pRatio,kRatio;
    private FertilityInfoPojo fertilityInfoPojo;
    private AreaPojo  areaPojo;
    private Integer id;
   private  String district = "";
    private String tel;
    private String nzfDetail;
    private Integer areaId;
    private String gisAreaName;
    /**
     标题
     */
    private String title;
    /**
     地址
     */
    private String address;
    /**
     地块编号
     */
    private String no;
    /**
     农家肥
     */
    private double farmyard;
    /**
     尿素
     */
    private double nitrogen;
    /**
     磷酸二铵
     */
    private double phosphorus;
    /**
     硫酸钾
     */
    private double potassium;
    /**
     时间
     */
    private String createTime;
    /**
     * 用户手机标识
     */
    private String mobileId;
    /**
     * 目标产量
     */
    private Integer amount;
    private double p,k,matter,nd;
    private String name,soliNo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
     //   initGPS();
        SDKInitializer.initialize(getApplicationContext());
        setContentView(R.layout.activity_location);
        mDistrictSearch = DistrictSearch.newInstance();
        mDistrictSearch.setOnDistrictSearchListener(this);
        imageView = (ImageView)findViewById(R.id.search_action);
        imageView.setOnClickListener(this);
        mapView = (MapView)findViewById(R.id.bmapView);
        baiduMap = mapView.getMap();
        baiduMap.setMapType(BaiduMap.MAP_TYPE_SATELLITE);
        baiduMap.setMyLocationEnabled(true);
        telView = (TextView) findViewById(R.id.tel_detail);
        mCurrentMode = MyLocationConfiguration.LocationMode.FOLLOWING;
        baiduMap .setMyLocationConfigeration(new MyLocationConfiguration(
                        mCurrentMode, true, null));
        // 定位初始化
        ;
        mLocClient = new LocationClient(this);
        mLocClient.registerLocationListener(myListener);
        LocationClientOption option = new LocationClientOption();
        option.setOpenGps(true); // 打开gps

        option.setCoorType("bd09ll"); // 设置坐标类型
        //option.setScanSpan(1000);
        mLocClient.setLocOption(option);
        mLocClient.start();
        final BitmapDescriptor bitmap = BitmapDescriptorFactory.fromResource(R.mipmap.position);
        // 设置marker图标
        baiduMap.setOnMapClickListener(new BaiduMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                //获取经纬度
                latitude = latLng.latitude;
                longitude = latLng.longitude;
                System.out.println("latitude=" + latitude + ",longitude=" + longitude);
                //先清除图层
                baiduMap.clear();
                LatLng point = new LatLng(latitude, longitude);
                // 构建MarkerOption，用于在地图上添加Marker
                MarkerOptions options = new MarkerOptions().position(point)
                        .icon(bitmap);
                // 在地图上添加Marker，并显示
                baiduMap.addOverlay(options);
            }

            @Override
            public boolean onMapPoiClick(MapPoi mapPoi) {
                return false;
            }
        });
    }
    //显示gis返回的数据
    public void showPopupWin(View v){
        if(latitude==0.0 || longitude==0.0){
            Toast.makeText(LocationActivity.this,"请选择测量位置",Toast.LENGTH_LONG).show();
            return;
        }
        new Thread(){
            @Override
            public void run() {
                new GisTask().execute();
            }
        }.start();
    }

    public void toTell(View v){
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:12316"));
        startActivity(intent);
    }

    @Override
    public void onGetDistrictResult(DistrictResult districtResult) {
        baiduMap.clear();
        if (districtResult == null) {
            return;
        }
        if (districtResult.error == SearchResult.ERRORNO.NO_ERROR) {
            List<List<LatLng>> polyLines = districtResult.getPolylines();
            if (polyLines == null) {
                return;
            }
            LatLngBounds.Builder builder = new LatLngBounds.Builder();
            for (List<LatLng> polyline : polyLines) {
                OverlayOptions ooPolyline11 = new PolylineOptions().width(10)
                        .points(polyline).dottedLine(true);
                baiduMap.addOverlay(ooPolyline11);
                for (LatLng latLng : polyline) {
                    builder.include(latLng);
                }
            }
            baiduMap.setMapStatus(MapStatusUpdateFactory
                    .newLatLngBounds(builder.build()));

        }
    }
    @Override
    public void onClick(View v) {
        searchView = (EditText)findViewById(R.id.search_name);

        searchName = searchView.getText().toString();
        new Thread(){
            @Override
            public void run() {
                new SearchTask().execute();
    }
       }.start();

    }
    //异步搜索行政单位
    private class SearchTask extends  AsyncTask<Void,Void,String>{
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if(areaPojo!=null){
                city =areaPojo.getCityName();
                gisUrl = areaPojo.getGisUrl();
            }
            tel= areaPojo.getTelephone();
            if (city==null || city.equals("")) {
                city = "酒泉市";
            }
            if (searchView.getText() != null && !"".equals(searchView.getText()) ) {
                district = searchView.getText().toString();
            }
            mDistrictSearch.searchDistrict(new DistrictSearchOption().cityName(city).districtName(district));
        }
        @Override
        protected String doInBackground(Void... params) {
            try {
                String param = "name="+searchName;
                String results = ServiceUtil.sendPostRequest(URL1,param);
                CommonBean commonBeans = OutJsonUtil.json2Bean(results,CommonBean.class);
                String infos = OutJsonUtil.toJson(commonBeans.getInfo());
                if(commonBeans!=null && commonBeans.getStatus()==200){
                    areaPojo = OutJsonUtil.json2Bean(infos,AreaPojo.class);
                }
                return  results;
            }catch (Exception e){
                e.printStackTrace();
            }
            return null;
        }
    }
    //异步查询gis系统数据
    private class GisTask extends AsyncTask<Void, Void, String> {
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            Log.d("json======",result);
            String features=null;
            try {
                JsonPojo jsonPojo  = OutJsonUtil.json2Bean(result,JsonPojo.class);
                features = OutJsonUtil.toJson(jsonPojo.getFeatures());
                if (features.equals("") || features==null ||features.equals("null") ||  features.equals("[]")){
                    Toast.makeText(LocationActivity.this,"未查到数据",Toast.LENGTH_LONG).show();
                    return;
                }
                JSONArray jsonArray = new JSONArray(features);
                JSONObject jsonObject = (JSONObject)jsonArray.get(0);
                String attrs = jsonObject.getString("attributes");
                JSONObject jsonObject1 = new JSONObject(attrs);
                //设置contentView
                View contentView = LayoutInflater.from(LocationActivity.this).inflate(R.layout.data_popupwin, null);
                mPopupWindow = new PopupWindow(contentView,
                        WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT, true);
                mPopupWindow.setContentView(contentView);
                //mPopupWindow.setFocusable(true);
                TextView dataName = (TextView)contentView.findViewById(R.id.data_name);
                TextView dataNo = (TextView)contentView.findViewById(R.id.data_no);
                TextView result1 = (TextView)contentView.findViewById(R.id.result1);
                TextView result2 = (TextView)contentView.findViewById(R.id.result2);
                TextView result3 = (TextView)contentView.findViewById(R.id.result3);
                TextView result4 = (TextView)contentView.findViewById(R.id.result4);
                TextView result5 = (TextView)contentView.findViewById(R.id.result5);
                region1View = (TextView)contentView.findViewById(R.id.region1);
                region2View = (TextView)contentView.findViewById(R.id.region2);
                region3View = (TextView)contentView.findViewById(R.id.region3);
                region4View = (TextView)contentView.findViewById(R.id.region4);
                region5View = (TextView)contentView.findViewById(R.id.region5);
                spinner = (Spinner)contentView.findViewById(R.id.data_class);
                new Thread(){
                    @Override
                    public void run() {
                        super.run();
                        new FertilityTask().execute();
                    }
                }.start();
                dataAmountView = (TextView)contentView.findViewById(R.id.data_amount);
                TextView dataView = (TextView)contentView.findViewById(R.id.data_action);
                TextView zdgxView = (TextView) contentView.findViewById(R.id.zdgx);

                String nos = jsonObject1.getString("内部标识码");
                name = jsonObject1.getString("行政单位名");
                String nameDetail = jsonObject1.getString("乡镇名称");
                gisAreaName = jsonObject1.getString("县区名称");
                String type = jsonObject1.getString("质地构型");
                String ph = jsonObject1.getString("pH");
                matter = jsonObject1.getDouble("有机质");
                p = jsonObject1.getDouble("有效磷");
                k = jsonObject1.getDouble("速效钾");
                String soli = jsonObject1.getString("土壤名称");
                double n = jsonObject1.getDouble("全氮");
                 nd = jsonObject1.getDouble("碱解氮");
                soliNo = jsonObject1.getString("地块编号");
                dataName.setText(name);
                dataNo.setText(soliNo);
                result1.setText(matter+"");
                result2.setText(n+"");
                result3.setText(nd+"");
                result4.setText(p+"");
                result5.setText(k+"");
                zdgxView.setText(type);
                if(matter<15){
                    region1View.setText("<15 低");
                }
                if(matter>=15 && matter<25){
                    region1View.setText("15-25 中");
                }
                if(matter>=25){
                    region1View.setText(">25 高");
                }
                if(n<1){
                    region2View.setText("<1 低");
                }
                if(n>=1 &&n<1.5){
                    region2View.setText("<1-1.5 中");
                }
                if(n>=1.5){
                    region2View.setText(">1.5 高");
                }
                if(nd<150){
                    region3View.setText("<150 低");
                }
                if(nd>=150 && nd<250){
                    region3View.setText("150-250 中");
                }
                if(nd >=250){
                    region3View.setText(">250 高");
                }
                if(p <15){
                    region4View.setText("<15 低");
                }
                if(p >=15 && p <30){
                    region4View.setText("15-30 中");
                }
                if(p >=30){
                    region4View.setText(">30 高");
                }
                if(k <150){
                    region5View.setText("<150 低");
                }
                if(k >=150 && k <250){
                    region5View.setText("150-250 中");
                }
                if(k >=250){
                    region5View.setText(">250 高");
                }

                //设置各个控件的点击响
                dataView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        String data = dataAmountView.getText().toString();
                        if (data.equals("") || data==null){
                            Toast.makeText(LocationActivity.this,"请填写目标产量",Toast.LENGTH_LONG);
                            return;
                        }else{
                            amount = Integer.parseInt(data);
                        }
                      //  className = spinner.getSelectedItem().toString();
                        fertilityId =((FertilityCropPojo)spinner.getSelectedItem()).getId();
                        fertilityDetail= ((FertilityCropPojo)spinner.getSelectedItem()).getDescription();
                        fertilityName= ((FertilityCropPojo) spinner.getSelectedItem()).getName();
                        className =((FertilityCropPojo)spinner.getSelectedItem()).getName();
                        title = className+"施肥配方";
                        address = name;
                        no = soliNo;
                        new Thread(){
                            @Override
                            public void run() {
                                super.run();
                                Log.d("=========","连接服务器1");
                                new HistoryTask().execute();
                            }
                        }.start();
                    }
                });

                ImageView imageView= (ImageView) contentView.findViewById(R.id.data_close);
                imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mPopupWindow.dismiss();
                    }
                });
                spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
                //显示PopupWindow
                View rootview = LayoutInflater.from(LocationActivity.this).inflate(R.layout.activity_location, null);
                mPopupWindow.showAtLocation(rootview, Gravity.CENTER, 0, 0);
            }catch (Exception e){
                e.printStackTrace();
            }
        }

        @Override
        protected String doInBackground(Void... param) {
            try {

                String xy= DBWGCUtil.bd09towgs84(longitude,latitude);
                Log.d("======",longitude+"==="+latitude);
                String results=null;
                String params = "where=&text=&objectIds=&time=&geometry="+xy+"&geometryType=esriGeometryPoint&inSR=&spatialRel=esriSpatialRelIntersects&relationParam=&outFields=内部标识码,行政单位名,县区名称,乡镇名称,质地构型,pH,有机质,有效磷,速效钾,土壤名称,全氮,碱解氮,地块编号&returnGeometry=false&maxAllowableOffset=&geometryPrecision=&outSR=&returnIdsOnly=false&returnCountOnly=false&orderByFields=&groupByFieldsForStatistics=&outStatistics=&returnZ=false&returnM=false&gdbVersion=&returnDistinctValues=false&f=pjson";

                if (gisUrl!=null && !gisUrl.equals("")) {

                    results  = ServiceUtil.sendPostRequest(gisUrl, params);
                    Log.d("======11",gisUrl+":::"+params);
                }else{
                    Log.d("param2:",params);
                    results = ServiceUtil.sendPostRequest(URL, params);
                    Log.d("======22",URL+":::"+params);
                }
                return  results;
            }catch (Exception e){
                e.printStackTrace();
            }
            return null;
        }
    }
    public class HistoryTask extends AsyncTask<Void, Void, String> {
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (id>0){
                Intent intent = new Intent();
                intent.putExtra("historyId",id);
                intent.putExtra("telephone",tel);
                intent.putExtra("nzwDetail",fertilityDetail);
                intent.putExtra("nzwName",fertilityName);
                intent.putExtra("flag","location");
                intent.setClass(LocationActivity.this,HistoryDetailActivity.class);
                startActivity(intent);
            }
        }

        @Override
        protected String doInBackground(Void... param) {
            try {

                String furl = "http://shifei.yungoux.com/zhkj/getFertilityInfo.do";
                String str="cropType="+fertilityId+"&target="+amount+"&nAttach="+nd+"&pAttach="+p+"&kAttach="+k+"&yAttach="+matter+"&areaId="+areaId;
                String result1 = ServiceUtil.sendPostRequest(furl,str);
                CommonBean common = OutJsonUtil.json2Bean(result1,CommonBean.class);
                if(common.getStatus()==200){
                    String infos = OutJsonUtil.toJson(common.getInfo());
                    fertilityInfoPojo = OutJsonUtil.json2Bean(infos,FertilityInfoPojo.class);
                }
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
                DeviceUuidFactory deviceUuidFactory = new DeviceUuidFactory(LocationActivity.this);
                String params ="title="+title+"&address="+address+"&no="+no+elementParam1+"&amount="+amount+"&mobileId="+ deviceUuidFactory.getDeviceUuid().toString()+elementParam2+"&telphone="+tel+"&proportion="+fertilityInfoPojo.getBili()+"&description="+fertilityDetail;
                String result = ServiceUtil.sendPostRequest(URL2,params);
                CommonBean commonBeans = OutJsonUtil.json2Bean(result,CommonBean.class);
                if(commonBeans.getStatus()==200){
                    String infos = OutJsonUtil.toJson(commonBeans.getInfo());
                    id = OutJsonUtil.json2Bean(infos,Integer.class);
                }
                return  result;
            }catch (Exception e){
                e.printStackTrace();
            }
            return null;
        }
    }
    public  class FertilityTask extends  AsyncTask<Void,Void,String>{
        @Override
        protected String doInBackground(Void... params) {
            try {
                String areaparam = "name="+gisAreaName;
                String results = ServiceUtil.sendPostRequest(URL1,areaparam);
                CommonBean areacommonBeans = OutJsonUtil.json2Bean(results,CommonBean.class);
                String areainfos= OutJsonUtil.toJson(areacommonBeans.getInfo());
                if(areacommonBeans!=null && areacommonBeans.getStatus()==200){
                    areaPojo = OutJsonUtil.json2Bean(areainfos,AreaPojo.class);
                }
                if (areaPojo!=null && areaPojo.getId()!=null) {
                    areaId = areaPojo.getId();
                }
                fertilityCropPojos = new ArrayList<FertilityCropPojo>();
                String param = "areaId="+areaId;
                String result = ServiceUtil.sendPostRequest(URL_F,param);
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
                ArrayAdapter<FertilityCropPojo> adapter = new ArrayAdapter<FertilityCropPojo>(LocationActivity.this,
                        android.R.layout.simple_spinner_item, fertilityCropPojos);
                spinner.setAdapter(adapter);
            }
        }
    }
    /**
     * 定位SDK监听函数
     */
    public class MyLocationListenner implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location) {
            // map view 销毁后不在处理新接收的位置
            if (location == null || mapView == null) {
                return;
            }
            MyLocationData locData = new MyLocationData.Builder()
                    .accuracy(location.getRadius())
                    // 此处设置开发者获取到的方向信息，顺时针0-360
                    .direction(100).latitude(location.getLatitude())
                    .longitude(location.getLongitude()).build();
            baiduMap.setMyLocationData(locData);
            if (isFirstLoc) {
                isFirstLoc = false;
                LatLng ll = new LatLng(location.getLatitude(),
                        location.getLongitude());
                MapStatus.Builder builder = new MapStatus.Builder();
                builder.target(ll).zoom(18.0f);
                baiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));
            }
        }

        public void onReceivePoi(BDLocation poiLocation) {
        }
    }

    /**
     * 监听GPS
     */
    public  void initGPS() {
        Log.d("判断GPS是否打开","");
        LocationManager locationManager = (LocationManager) this
                .getSystemService(Context.LOCATION_SERVICE);
        // 判断GPS模块是否开启，如果没有则开启
        if (!locationManager
                .isProviderEnabled(android.location.LocationManager.GPS_PROVIDER)) {
            Toast.makeText(LocationActivity.this, "请打开GPS",
                    Toast.LENGTH_SHORT).show();
            AlertDialog.Builder dialog = new AlertDialog.Builder(this);
            dialog.setMessage("请打开GPS");
            dialog.setPositiveButton("确定",
                    new android.content.DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface arg0, int arg1) {
                            // 转到手机设置界面，用户设置GPS
                            Intent intent = new Intent(
                                    Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                            startActivityForResult(intent, 0); // 设置完成后返回到原来的界面

                        }
                    });
            dialog.setNeutralButton("取消", new android.content.DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface arg0, int arg1) {
                    arg0.dismiss();
                }
            } );
            dialog.show();
        } else {
        }
    }
    @Override
    protected void onPause() {
        mapView.onPause();
        super.onPause();
    }

    @Override
    protected void onResume() {
        mapView.onResume();
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        // 退出时销毁定位
        mLocClient.stop();
        // 关闭定位图层
        baiduMap.setMyLocationEnabled(false);
        mapView.onDestroy();
        mapView = null;
        mDistrictSearch.destroy();
        super.onDestroy();
    }
}
