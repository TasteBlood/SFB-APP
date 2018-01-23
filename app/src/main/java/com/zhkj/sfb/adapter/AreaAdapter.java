package com.zhkj.sfb.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zhkj.sfb.CenterProfileActivity;
import com.zhkj.sfb.ExpertActivity;
import com.zhkj.sfb.FeatureActivity;
import com.zhkj.sfb.FertilizerInfoActivity;
import com.zhkj.sfb.R;
import com.zhkj.sfb.ServiceGuideActivity;
import com.zhkj.sfb.pojo.AreaPojo;
import com.zhkj.sfb.pojo.CityPojo;

import java.util.List;

/**
 * Created by Administrator on 2016/9/29.
 */
public class AreaAdapter extends RecyclerView.Adapter<AreaAdapter.ViewHolder> {
    List<AreaPojo> areaPojos = null;
    private OnItemClickListener mListener;
    private  Context mContext;
    private int flag;

    public AreaAdapter(Context context,List<AreaPojo> areaPojos,int flag)
    {
        this.areaPojos = areaPojos;
        this.mContext = context;
        this.flag = flag;
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
        viewHolder.bindData(name, id);
        viewHolder.itemView.setTag(i);
        viewHolder.nameView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String id =  viewHolder.idView.getText().toString();
                Intent intent = new Intent();
                intent.putExtra("areaId",id);
                if (flag == 1){
                    intent.setClass(mContext, FertilizerInfoActivity.class);
                }
                if(flag ==2){
                    intent.setClass(mContext, CenterProfileActivity.class);
                }
                if(flag ==3){
                    intent.setClass(mContext, ExpertActivity.class);
                }
                if(flag ==4){
                    intent.setClass(mContext, ServiceGuideActivity.class);
                }
                if(flag ==5){
                    intent.setClass(mContext, FeatureActivity.class);
                }
                mContext.startActivity(intent);
            }
        });
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
        public ViewHolder(View itemView)
        {
            super(itemView);
            nameView = (TextView) itemView.findViewById(R.id.area_name);
            idView = (TextView) itemView.findViewById(R.id.area_id);
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
