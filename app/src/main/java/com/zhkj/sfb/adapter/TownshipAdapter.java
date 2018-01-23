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
import com.zhkj.sfb.pojo.TownshipPojo;
import com.zhkj.sfb.pojo.VillagePojo;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
/**
 * Created by frank on 2017-05-04.
 */
public class TownshipAdapter extends RecyclerView.Adapter<TownshipAdapter.ViewHolder> {
    List<StreetPojo> streetPojos =null;
    private TownshipAdapter.OnItemClickListener mListener;
    private  Context mContext;
    private List<VillagePojo> villagePojos ;
    private Integer townshipId;
    private static String URL="http://shifei.yungoux.com/zhkj/getVillage.do";
    private RecyclerView villageView;
    private String telphone;
    private Integer areaId;
    public TownshipAdapter(Context context, List<StreetPojo> streetPojos,RecyclerView villageView,String telphone) {
        this.mContext = context;
        this.streetPojos = streetPojos;
        this.villageView = villageView;
        this.telphone=telphone;
    }
/**
     * 绑定item视图
     * @param viewGroup
     * @param i
     * @return
     */

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        final View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.township_cardview,viewGroup,false);
        itemView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if (mListener!=null) {
                    mListener.onItemClick(v, (String) itemView.getTag());
                }
            }
        });
        return new ViewHolder(itemView);
    }


/**
     * 绑定数据和itemView
     * @param viewHolder
     * @param position
     */

    @Override
    public void onBindViewHolder(final TownshipAdapter.ViewHolder viewHolder, int position) {
        String name = streetPojos.get(position).getName();
        Integer id =streetPojos.get(position).getId();
        areaId = streetPojos.get(position).getAreaId();
        viewHolder.bindData(name, id);
        viewHolder.itemView.setTag(position);
        viewHolder.nameView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String id =  viewHolder.idView.getText().toString();
                townshipId = Integer.parseInt(id);
                villageView.setVisibility(View.GONE);
                new Thread(){
                    @Override
                    public void run() {
                        super.run();
                        new TownshipAdapter.VillageTask().execute();
                    }
                }.start();
            }
        });
    }

    @Override
    public int getItemCount() {
        return streetPojos.size();
    }

/**
* 查询村级数据
 */

    public class VillageTask extends AsyncTask<Void,Void,String>{


    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        if(villagePojos!=null && villagePojos.size()>0){
            villageView.setVisibility(View.VISIBLE);
            villageView.setLayoutManager(new LinearLayoutManager(mContext));
            VillageAdapter villageAdapter = new VillageAdapter(mContext,villagePojos,telphone,areaId);
            villageView.setHasFixedSize(true);
            villageView.setAdapter(villageAdapter);
        }else{
            Toast toast = Toast.makeText(mContext, "没有村级信息", Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    @Override
    protected String doInBackground(Void... params) {
        try {
            villagePojos = new ArrayList<VillagePojo>();
            String str = "streetId="+townshipId;
            String result = ServiceUtil.sendPostRequest(URL,str);
            CommonBean commonBeans = OutJsonUtil.json2Bean(result,CommonBean.class);
            String infos = OutJsonUtil.toJson(commonBeans.getInfo());
            if(commonBeans.getStatus()==200){
                Type type = new TypeToken<ArrayList<VillagePojo>>() {}.getType();
                villagePojos = OutJsonUtil.json2List(infos,type);
            }
            return  result;
        }catch (Exception e){
            e.printStackTrace();
        }


        return null;
    }
}



/**
     * 添加视图内容
     */

    public static class ViewHolder extends RecyclerView.ViewHolder
    {
        private TextView nameView;
        private TextView idView;
        public ViewHolder(View itemView)
        {
            super(itemView);
            nameView = (TextView) itemView.findViewById(R.id.township_name);
            idView = (TextView) itemView.findViewById(R.id.township_id);

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
}

