package com.zhkj.sfb;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.Poi;
import com.zhkj.sfb.adapter.IndexClassAdapter;
import com.zhkj.sfb.common.LocationApplication;
import com.zhkj.sfb.common.LocationService;
import com.zhkj.sfb.common.OutJsonUtil;
import com.zhkj.sfb.common.ServiceUtil;
import com.zhkj.sfb.common.WeatherBean;
import com.zhkj.sfb.common.WeatherResult;
import com.zhkj.sfb.common.WeatherSK;
import com.zhkj.sfb.common.WeatherToday;
import com.zhkj.sfb.pojo.IndexClassPojo;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private List<IndexClassPojo> indexClassPojos;
    private GridView classView;
    private final static String URL = "http://v.juhe.cn/weather/index?format=2&key=02090e323c04674ee40cc8e94e93138a";
    private LocationService locationService;
    private TextView districtView,dayView,weekView,weatherView,degreeCelsiusView,timeView,branchView,humidityView;
    private String district;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initClass();
        districtView = (TextView)findViewById(R.id.main_district);
        dayView = (TextView)findViewById(R.id.main_day);
        weekView = (TextView)findViewById(R.id.main_week);
        weatherView = (TextView)findViewById(R.id.main_weather);
        degreeCelsiusView = (TextView) findViewById(R.id.main_degree_celsius);
        timeView = (TextView)findViewById(R.id.main_time);
        branchView = (TextView)findViewById(R.id.main_branch);
        humidityView = (TextView)findViewById(R.id.main_humidity);

        classView = (GridView)findViewById(R.id.main_class);
        classView.setNumColumns(3);
        classView.setAdapter(new IndexClassAdapter(MainActivity.this,indexClassPojos));
        classView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if(i==0){
                    Intent intent = new Intent(MainActivity.this, LocationActivity.class);
                    startActivity(intent);
                }
                if(i==1){
                    Intent intent = new Intent(MainActivity.this, FarmerActivity.class);
                    startActivity(intent);
                }
                if(i==2){
                    Intent intent = new Intent(MainActivity.this,ElementActivity.class);
                    startActivity(intent);
                }
                if(i==3){
                    Intent intent = new Intent(MainActivity.this,CityActivity.class);
                    intent.putExtra("flag",1);
                    startActivity(intent);
                }
                if(i==4){
                    Intent intent = new Intent(MainActivity.this,TabLayoutActivity.class);
                    startActivity(intent);
                }

                if(i==5){
                    Intent intent = new Intent(MainActivity.this,ServiceActivity.class);
                    startActivity(intent);
                }
                if(i==6){
                    Intent intent = new Intent(MainActivity.this,OnlineActivity.class);
                    startActivity(intent);
                }
                if(i==7){
                    Intent intent = new Intent(MainActivity.this,ForumActivity.class);
                    startActivity(intent);
                }
                if(i==8){
                    Intent intent = new Intent(MainActivity.this,AboutActivity.class);
                    startActivity(intent);
                }
            }
        });

        locationService = ((LocationApplication) getApplication()).locationService;
        //获取locationservice实例，建议应用中只初始化1个location实例，然后使用，可以参考其他示例的activity，都是通过此种方式获取locationservice实例的
        locationService.registerListener(mListener);
        locationService.start();


    }
    public class WeatherTask extends AsyncTask<Void, Void, String> {
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            try {
                WeatherBean weatherBean = OutJsonUtil.json2Bean(result,WeatherBean.class);
                if (weatherBean.getResultcode() == 200){
                    String infos = OutJsonUtil.toJson(weatherBean.getResult());
                    Log.d("=====info",infos);
                    WeatherResult weatherResult = OutJsonUtil.json2Bean(infos, WeatherResult.class);
                    String skInfo = OutJsonUtil.toJson(weatherResult.getSk());
                    String todayInfo = OutJsonUtil.toJson(weatherResult.getToday());
                    WeatherSK weatherSK = OutJsonUtil.json2Bean(skInfo,WeatherSK.class);
                    WeatherToday weatherToday = OutJsonUtil.json2Bean(todayInfo,WeatherToday.class);
                    weatherView.setText(weatherToday.getWeather().toString());
                    degreeCelsiusView.setText(weatherSK.getTemp()+"℃");
                    branchView.setText(weatherSK.getWind_direction()+weatherSK.getWind_strength());
                    humidityView.setText("湿度"+weatherSK.getHumidity());
                    weekView.setText(weatherToday.getWeek().toString());
                    String date = dateFormat(getTomorrow(), "yyyy-MM-dd");
                    String[] dates = date.split("-");
                    SolarDate solarDate = new SolarDate(Integer.parseInt(dates[0]), Integer.parseInt(dates[1]), Integer.parseInt(dates[2]));
                    LunarDate ld = solarDate.toLunarDate();
                    timeView.setText(ld.toString());

                }

            }catch (Exception e){
                e.printStackTrace();
            }

        }

        @Override
        protected String doInBackground(Void... param) {

            String params = "&cityname="+district;
            try {
                String URL = "http://v.juhe.cn/weather/index?format=2&key=02090e323c04674ee40cc8e94e93138a&cityname="+district;
                String result = ServiceUtil.sendPostRequest(URL,"");
                return  result;
            }catch (Exception e){
                e.printStackTrace();
            }
            return null;
        }
    }

    /*****
     * 定位结果回调，重写onReceiveLocation方法，可以直接拷贝如下代码到自己工程中修改
     *
     */
    private BDLocationListener mListener = new BDLocationListener() {

        @Override
        public void onReceiveLocation(BDLocation location) {
            // TODO Auto-generated method stub
            if (null != location && location.getLocType() != BDLocation.TypeServerError) {
                StringBuffer sb = new StringBuffer(256);
                sb.append("time : ");
                /**
                 * 时间也可以使用systemClock.elapsedRealtime()方法 获取的是自从开机以来，每次回调的时间；
                 * location.getTime() 是指服务端出本次结果的时间，如果位置不发生变化，则时间不变
                 */
                sb.append(location.getTime());
                sb.append("\nlocType : ");// 定位类型
                sb.append(location.getLocType());
                sb.append("\nlocType description : ");// *****对应的定位类型说明*****
                //sb.append(location.getLocTypeDescription());
                sb.append("\nlatitude : ");// 纬度
                sb.append(location.getLatitude());
                sb.append("\nlontitude : ");// 经度
                sb.append(location.getLongitude());
                sb.append("\nradius : ");// 半径
                sb.append(location.getRadius());
                sb.append("\nCountryCode : ");// 国家码
                sb.append(location.getCountryCode());
                sb.append("\nCountry : ");// 国家名称
                sb.append(location.getCountry());
                sb.append("\ncitycode : ");// 城市编码
                sb.append(location.getCityCode());
                sb.append("\ncity : ");// 城市
                sb.append(location.getCity());
                sb.append("\nDistrict : ");// 区
                sb.append(location.getDistrict());
                sb.append("\nStreet : ");// 街道
                sb.append(location.getStreet());
                sb.append("\naddr : ");// 地址信息
                sb.append(location.getAddrStr());
                sb.append("\nUserIndoorState: ");// *****返回用户室内外判断结果*****
                //sb.append(location.getUserIndoorState());
                sb.append("\nDirection(not all devices have value): ");
                sb.append(location.getDirection());// 方向
                sb.append("\nlocationdescribe: ");
                sb.append(location.getLocationDescribe());// 位置语义化信息
                sb.append("\nPoi: ");// POI信息
                if (location.getPoiList() != null && !location.getPoiList().isEmpty()) {
                    for (int i = 0; i < location.getPoiList().size(); i++) {
                        Poi poi = (Poi) location.getPoiList().get(i);
                        sb.append(poi.getName() + ";");
                    }
                }
                if (location.getLocType() == BDLocation.TypeGpsLocation) {// GPS定位结果
                    sb.append("\nspeed : ");
                    sb.append(location.getSpeed());// 速度 单位：km/h
                    sb.append("\nsatellite : ");
                    sb.append(location.getSatelliteNumber());// 卫星数目
                    sb.append("\nheight : ");
                    sb.append(location.getAltitude());// 海拔高度 单位：米
                    sb.append("\ngps status : ");
                    //sb.append(location.getGpsAccuracyStatus());// *****gps质量判断*****
                    sb.append("\ndescribe : ");
                    sb.append("gps定位成功");
                } else if (location.getLocType() == BDLocation.TypeNetWorkLocation) {// 网络定位结果
                    // 运营商信息
                    if (location.hasAltitude()) {// *****如果有海拔高度*****
                        sb.append("\nheight : ");
                        sb.append(location.getAltitude());// 单位：米
                    }
                    sb.append("\noperationers : ");// 运营商信息
                    sb.append(location.getOperators());
                    sb.append("\ndescribe : ");
                    sb.append("网络定位成功");
                } else if (location.getLocType() == BDLocation.TypeOffLineLocation) {// 离线定位结果
                    sb.append("\ndescribe : ");
                    sb.append("离线定位成功，离线定位结果也是有效的");
                    //Toast.makeText(MainActivity.this,"网络未连接",Toast.LENGTH_LONG).show();
                    return;
                } else if (location.getLocType() == BDLocation.TypeServerError) {
                    sb.append("\ndescribe : ");
                    sb.append("服务端网络定位失败，可以反馈IMEI号和大体定位时间到loc-bugs@baidu.com，会有人追查原因");
                    Toast.makeText(MainActivity.this,"网络未连接",Toast.LENGTH_LONG).show();
                    return;
                } else if (location.getLocType() == BDLocation.TypeNetWorkException) {
                    sb.append("\ndescribe : ");
                    sb.append("网络不同导致定位失败，请检查网络是否通畅");
                    Toast.makeText(MainActivity.this,"网络未连接",Toast.LENGTH_LONG).show();
                    return;
                } else if (location.getLocType() == BDLocation.TypeCriteriaException) {
                    sb.append("\ndescribe : ");
                    sb.append("无法获取有效定位依据导致定位失败，一般是由于手机的原因，处于飞行模式下一般会造成这种结果，可以试着重启手机");
                    Toast.makeText(MainActivity.this,"网络未连接",Toast.LENGTH_LONG).show();
                    return;
                }
                Log.d("===========",sb.toString());
                district = location.getDistrict();
                if(district.contains("区")){
                    district = location.getCity();
                }
                districtView.setText(district+"");
                dayView.setText(location.getTime().substring(0,10));
                new Thread() {
                    @Override
                    public void run() {
                        // 你要执行的方法
                        new WeatherTask().execute();
                    }

                }.start();
            }
        }

    };

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
            Toast.makeText(MainActivity.this, "请打开GPS",
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
    public  Date getTomorrow() {
        Date date = new Date();
        long time = date.getTime() / 1000L + 86400L;
        date.setTime(time * 1000L);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        try {
            date = format.parse(format.format(date));
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        return date;
    }
    public  String dateFormat(Date date, String dateFormat) {
        if (date == null)
            return "";
        SimpleDateFormat format = new SimpleDateFormat(dateFormat);
        if (date != null)
            return format.format(date);

        return "";
    }
    public void initClass(){
        indexClassPojos = new ArrayList<IndexClassPojo>();
        IndexClassPojo indexClassPojo1 = new IndexClassPojo();
        indexClassPojo1.setImg(R.mipmap.icon_1);
        indexClassPojo1.setName("定位施肥");
        indexClassPojos.add(indexClassPojo1);

        IndexClassPojo indexClassPojo2 = new IndexClassPojo();
        indexClassPojo2.setImg(R.mipmap.icon_2);
        indexClassPojo2.setName("农户施肥");
        indexClassPojos.add(indexClassPojo2);

        IndexClassPojo indexClassPojo3 = new IndexClassPojo();
        indexClassPojo3.setImg(R.mipmap.icon_3);
        indexClassPojo3.setName("缺素诊断");
        indexClassPojos.add(indexClassPojo3);

        IndexClassPojo indexClassPojo4 = new IndexClassPojo();
        indexClassPojo4.setImg(R.mipmap.icon_4);
        indexClassPojo4.setName("肥料门店");
        indexClassPojos.add(indexClassPojo4);

        IndexClassPojo indexClassPojo5 = new IndexClassPojo();
        indexClassPojo5.setImg(R.mipmap.icon_5);
        indexClassPojo5.setName("施肥规程");
        indexClassPojos.add(indexClassPojo5);

        IndexClassPojo indexClassPojo6 = new IndexClassPojo();
        indexClassPojo6.setImg(R.mipmap.icon_6);
        indexClassPojo6.setName("农技推广");
        indexClassPojos.add(indexClassPojo6);

        IndexClassPojo indexClassPojo7 = new IndexClassPojo();
        indexClassPojo7.setImg(R.mipmap.icon_7);
        indexClassPojo7.setName("电商服务");
        indexClassPojos.add(indexClassPojo7);

        IndexClassPojo indexClassPojo8 = new IndexClassPojo();
        indexClassPojo8.setImg(R.mipmap.icon_8);
        indexClassPojo8.setName("施肥讲堂");
        indexClassPojos.add(indexClassPojo8);

        IndexClassPojo indexClassPojo9 = new IndexClassPojo();
        indexClassPojo9.setImg(R.mipmap.icon_9);
        indexClassPojo9.setName("关于我们");
        indexClassPojos.add(indexClassPojo9);
    }
}
