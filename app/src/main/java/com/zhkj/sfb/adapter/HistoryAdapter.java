package com.zhkj.sfb.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zhkj.sfb.HistoryDetailActivity;
import com.zhkj.sfb.R;
import com.zhkj.sfb.pojo.HistoryPojo;

import java.util.List;

/**
 * Created by Administrator on 2017/3/8.
 */

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.ViewHolder> {
    private OnItemClickListener mListener;
    private List<HistoryPojo> historyPojos;
    private Context mContext;
    public HistoryAdapter(Context mContext, List<HistoryPojo> historyPojos)
    {
        this.historyPojos = historyPojos;
        this.mContext = mContext;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i)
    {
        final View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.cardview_history,viewGroup,false);
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
    public void onBindViewHolder(ViewHolder viewHolder, int i)
    {

        String title = historyPojos.get(i).getTitle();
        String time =historyPojos.get(i).getCreateTime();
        String address = historyPojos.get(i).getAddress();
        String no = historyPojos.get(i).getNo();
        final  Integer historyId = historyPojos.get(i).getId();
        viewHolder.bindData(title,time,address,no,historyId);
        viewHolder.historyLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent  = new Intent();
                intent.putExtra("historyId",historyId);
                intent.setClass(mContext, HistoryDetailActivity.class);
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount()
    {
        return historyPojos.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder
    {
        private TextView titleView,timeView,addressView,noView;
        private RelativeLayout historyLayout;
        public ViewHolder(View itemView)
        {
            super(itemView);
            titleView = (TextView) itemView.findViewById(R.id.history_name);
            timeView = (TextView) itemView.findViewById(R.id.history_time);
            addressView = (TextView)itemView.findViewById(R.id.history_address);
            noView = (TextView)itemView.findViewById(R.id.history_no);
            historyLayout = (RelativeLayout) itemView.findViewById(R.id.history_Layout);
        }

        public void bindData(String title, String time,String address,String no, Integer historyId)
        {
            titleView.setText(title);
            timeView.setText(time.substring(0,10));
            addressView.setText(address);
            noView.setText(no);
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
