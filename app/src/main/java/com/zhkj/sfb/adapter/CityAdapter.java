package com.zhkj.sfb.adapter;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.reflect.TypeToken;
import com.zhkj.sfb.CityActivity;
import com.zhkj.sfb.R;
import com.zhkj.sfb.common.CommonBean;
import com.zhkj.sfb.common.OutJsonUtil;
import com.zhkj.sfb.common.ServiceUtil;
import com.zhkj.sfb.pojo.AreaPojo;
import com.zhkj.sfb.pojo.CityPojo;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/9/29.
 */
public class CityAdapter extends RecyclerView.Adapter<CityAdapter.ViewHolder> {
    List<CityPojo> cityPojos = null;
    private OnItemClickListener mListener;
    private  Context mContext;
    private RecyclerView areaView;
    private List<AreaPojo> areaPojos;
    private static String URL="http://shifei.yungoux.com/zhkj/getArea.do";
    private Integer cityId;
    private int flag;

    public CityAdapter(Context context,List<CityPojo> cityPojos,RecyclerView areaView,int flag)
    {
        this.cityPojos = cityPojos;
        this.mContext = context;
        this.areaView = areaView;
        this.flag = flag;
    }



    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i)
    {
        final View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.city_cardview,viewGroup,false);
        itemView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(mListener != null)
                    mListener.onItemClick(v, (String) itemView.getTag());
            }

        });
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, int i)
    {
        String name = cityPojos.get(i).getName();
        Integer id =cityPojos.get(i).getId();
        viewHolder.bindData(name, id);
        viewHolder.itemView.setTag(i);
        viewHolder.nameView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String id =  viewHolder.idView.getText().toString();
                cityId = Integer.parseInt(id);
                new Thread(){
                    @Override
                    public void run() {
                        super.run();
                        new AreaTask().execute();
                    }
                }.start();
            }
        });
    }

    @Override
    public int getItemCount()
    {
        return cityPojos.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder
    {
        private TextView nameView;
        private TextView idView;
        public ViewHolder(View itemView)
        {
            super(itemView);
            nameView = (TextView) itemView.findViewById(R.id.city_name);
            idView = (TextView) itemView.findViewById(R.id.city_id);
        }

        public void bindData(String name,Integer id)
        {
            nameView.setText(name);
            idView.setText(id.toString());
        }
    }

    public interface OnItemClickListener
    {
        public void onItemClick(View view, String data);
    }

    public void setOnItemClickListener(OnItemClickListener listener)
    {
        this.mListener = listener;
    }

    public class AreaTask extends AsyncTask<Void, Void, String> {
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if(areaPojos!=null && areaPojos.size()>0){
                areaView.setLayoutManager(new LinearLayoutManager(mContext));
                AreaAdapter areaAdapter = new AreaAdapter(mContext,areaPojos,flag);
                areaView.setHasFixedSize(true);
                areaView.setAdapter(areaAdapter);
            }else{
                Toast toast = Toast.makeText(mContext, "没有县级信息", Toast.LENGTH_SHORT);
                toast.show();
            }
        }

        @Override
        protected String doInBackground(Void... param) {
            try {
                String params ="cityId="+cityId;
                String result = ServiceUtil.sendPostRequest(URL,params);
                CommonBean commonBeans = OutJsonUtil.json2Bean(result,CommonBean.class);
                String infos = OutJsonUtil.toJson(commonBeans.getInfo());
                if(commonBeans.getStatus()==200){
                    Type type = new TypeToken<ArrayList<AreaPojo>>() {}.getType();
                    areaPojos = OutJsonUtil.json2List(infos,type);
                }
                return  result;
            }catch (Exception e){
                e.printStackTrace();
            }
            return null;
        }
    }
}
