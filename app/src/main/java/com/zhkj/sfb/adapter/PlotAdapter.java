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
import com.zhkj.sfb.CropActivity;
import com.zhkj.sfb.FarmerInfoActivity;
import com.zhkj.sfb.R;
import com.zhkj.sfb.common.CommonBean;
import com.zhkj.sfb.common.OutJsonUtil;
import com.zhkj.sfb.common.ServiceUtil;
import com.zhkj.sfb.pojo.CropPojo;
import com.zhkj.sfb.pojo.FertilityCropPojo;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
/**
 * 获取地块信息
 * Created by frank on 2017-05-08.
 */
public class PlotAdapter extends RecyclerView.Adapter<PlotAdapter.ViewHolder>  {
    List<CropPojo> cropPojos=null;
   private OnItemClickListener mListener;
    private Context mContext;

    private List<FertilityCropPojo> fertilityCropPojos;
    private String landNum;
    private String farmerName;
    private String elementO;
    private String elementP;
    private String elementN;
    private String elementK;
    private  ArrayList<String> stringArrayList;
    private String telphone;
    private Integer areaId;
    public PlotAdapter(Context context,List<CropPojo> cropPojos,String telphone,Integer areaId){
        this.cropPojos = cropPojos;
        this.mContext= context;
        this.telphone =telphone;
        this.areaId =areaId;
    }
    @Override
    public PlotAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        final View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.plot_cardview,viewGroup,false);
        itemView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(mListener != null)
                    mListener.onItemClick(v, (String) itemView.getTag());
            }
        });
        return new PlotAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final PlotAdapter.ViewHolder viewHolder, int i) {
        landNum = cropPojos.get(i).getLandNum();
         farmerName= cropPojos.get(i).getFarmerName();
        elementO =cropPojos.get(i).getElementO();
        elementN =cropPojos.get(i).getElementN();
        elementP =cropPojos.get(i).getElementP();
        elementK =cropPojos.get(i).getElementK();
        Integer  id  = cropPojos.get(i).getId();
        viewHolder.bindData(landNum,id);
        viewHolder.itemView.setTag(i);
        //点击地块编号，设置监听事件
        viewHolder.nameView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent();
                    stringArrayList = new ArrayList<String>();
                    stringArrayList.add(landNum);
                    stringArrayList.add(farmerName);
                    stringArrayList.add(elementO);
                    stringArrayList.add(elementN);
                    stringArrayList.add(elementP);
                    stringArrayList.add(elementK);
                    stringArrayList.add(telphone);
                    stringArrayList.add(areaId+"");
                    intent.putStringArrayListExtra("dataInfo",stringArrayList);
                    intent.setClass(mContext, CropActivity.class);
                    mContext.startActivity(intent);
                }
        });
    }
    @Override
    public int getItemCount() {
        return cropPojos.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder
    {
        private TextView nameView;
        private TextView idView;
        public ViewHolder(View itemView)
        {
            super(itemView);
            nameView = (TextView) itemView.findViewById(R.id.plot_name);
            idView = (TextView) itemView.findViewById(R.id.plot_id);
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

    public void setOnItemClickListener(PlotAdapter.OnItemClickListener listener)
    {
        this.mListener = listener;
    }
}
