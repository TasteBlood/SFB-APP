package com.zhkj.sfb.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.zhkj.sfb.R;
import com.zhkj.sfb.pojo.IndexClassPojo;

import java.util.List;

/**
 * Created by Administrator on 2016/12/13.
 */

public class IndexClassAdapter extends BaseAdapter{
    private List<IndexClassPojo> indexClassPojos;
    private Context mContext;
    public IndexClassAdapter(Context mContext, List<IndexClassPojo> indexClassPojos)
    {
        this.indexClassPojos = indexClassPojos;
        this.mContext = mContext;
    }
    public int getCount()
    {
        return indexClassPojos.size();
    }
    public Object getItem(int item)
    {
        return item;
    }
    public long getItemId(int id)
    {
        return id;
    }
    public View getView(int position, View convertView, ViewGroup parent)
    {
        Holder holder;
        if(convertView==null)
        {
            holder = new Holder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.main_item,null);
            holder.nameView = (TextView) convertView.findViewById(R.id.main_item_name);
            holder.imgView = (ImageView) convertView.findViewById(R.id.main_item_img);
            convertView.setTag(holder);
        }else{
            holder = (Holder)convertView.getTag();
        }
        String name = indexClassPojos.get(position).getName();
        holder.imgView.setImageResource(indexClassPojos.get(position).getImg());
        holder.nameView.setText(name);
        return convertView;
    }
    class Holder{
        public TextView nameView;
        public ImageView imgView;
    }
}
