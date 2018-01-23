package com.zhkj.sfb.adapter;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.reflect.TypeToken;
import com.zhkj.sfb.CenterProfileActivity;
import com.zhkj.sfb.ExpertActivity;
import com.zhkj.sfb.FertilizerInfoActivity;
import com.zhkj.sfb.R;
import com.zhkj.sfb.ServiceGuideActivity;
import com.zhkj.sfb.common.CommonBean;
import com.zhkj.sfb.common.OutJsonUtil;
import com.zhkj.sfb.common.ServiceUtil;
import com.zhkj.sfb.pojo.AreaPojo;
import com.zhkj.sfb.pojo.StreetPojo;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
/**
 * Created by Administrator on 2016/9/29.
 */
public class FarmerAreaAdapter extends RecyclerView.Adapter<FarmerAreaAdapter.ViewHolder> {
    private static String URL="http://shifei.yungoux.com/zhkj/getStreet.do";
    List<AreaPojo> areaPojos = null;
    private OnItemClickListener mListener;
    private  Context mContext;
    private int flag;
    private Integer areaId;
    private List<StreetPojo> streetPojos;
    private RecyclerView townshipView;
    private RecyclerView villageView;
    private String telphone;
    public FarmerAreaAdapter(Context context, List<AreaPojo> areaPojos, RecyclerView townshipView, RecyclerView villageView, int flag)
    {
        this.areaPojos = areaPojos;
        this.mContext = context;
        this.flag = flag;
        this.townshipView=townshipView;
        this.villageView = villageView;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i)
    {
        final View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.area_cardview,viewGroup,false);
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
        String name = areaPojos.get(i).getName();
        Integer id =areaPojos.get(i).getId();
     String tel =areaPojos.get(i).getTelephone();
        viewHolder.bindData(name, id,tel);
        viewHolder.itemView.setTag(i);
        if (flag==10){
            viewHolder.nameView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String id =  viewHolder.idView.getText().toString();
                    areaId = Integer.parseInt(id);
                    telphone = viewHolder.tView.getText().toString();
                    villageView.setVisibility(View.GONE);
                    new Thread(){
                        @Override
                        public void run() {
                            super.run();
                            new TownshipTask().execute();
                        }
                    }.start();
                }
            });
        }else {
            viewHolder.nameView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String id = viewHolder.idView.getText().toString();
                    Intent intent = new Intent();
                    intent.putExtra("areaId", id);
                    if (flag == 1) {
                        intent.setClass(mContext, FertilizerInfoActivity.class);
                    }
                    if (flag == 2) {
                        intent.setClass(mContext, CenterProfileActivity.class);
                    }
                    if (flag == 3) {
                        intent.setClass(mContext, ExpertActivity.class);
                    }
                    if (flag == 4) {
                        intent.setClass(mContext, ServiceGuideActivity.class);
                    }
                    mContext.startActivity(intent);
                }
            });
        }
    }

    @Override
    public int getItemCount()
    {
        return areaPojos.size();
    }


    public static class ViewHolder extends RecyclerView.ViewHolder
    {
        private TextView nameView;
        private TextView idView;
        private TextView tView;
        public ViewHolder(View itemView)
        {
            super(itemView);
            nameView = (TextView) itemView.findViewById(R.id.area_name);
            idView = (TextView) itemView.findViewById(R.id.area_id);
            tView = (TextView) itemView.findViewById(R.id.area_telphone);
        }

        public void bindData(String name,Integer id,String tel)
        {
            nameView.setText(name);
            idView.setText(id.toString());
            tView.setText(tel);
        }
    }

    public class TownshipTask extends AsyncTask<Void, Void, String>{
        @Override
        protected String doInBackground(Void... params) {
            try {
                streetPojos= new ArrayList<StreetPojo>();
                String p ="areaId="+areaId;
                String result = ServiceUtil.sendPostRequest(URL,p);
                CommonBean commonBeans = OutJsonUtil.json2Bean(result,CommonBean.class);
                String infos = OutJsonUtil.toJson(commonBeans.getInfo());
                if(commonBeans.getStatus()==200){
                    Type type = new TypeToken<ArrayList<StreetPojo>>() {}.getType();
                    streetPojos = OutJsonUtil.json2List(infos,type);
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
            if(streetPojos!=null && streetPojos.size()>0){
                townshipView.setVisibility(View.VISIBLE);
                townshipView.setLayoutManager(new LinearLayoutManager(mContext));
              TownshipAdapter townshipAdapter = new TownshipAdapter(mContext,streetPojos,villageView,telphone);
              townshipView.setHasFixedSize(true);
               townshipView.setAdapter(townshipAdapter);

            }else{
                Toast toast = Toast.makeText(mContext, "没有乡镇信息", Toast.LENGTH_SHORT);
                toast.show();
            }
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
}
