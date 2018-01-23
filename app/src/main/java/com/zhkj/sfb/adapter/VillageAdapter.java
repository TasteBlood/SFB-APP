package com.zhkj.sfb.adapter;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.zhkj.sfb.CropActivity;
import com.zhkj.sfb.FarmerInfoActivity;
import com.zhkj.sfb.R;
import com.zhkj.sfb.pojo.VillagePojo;
import java.util.List;
/**
 * Created by frank on 2017-05-04.
 */
public class VillageAdapter extends RecyclerView.Adapter<VillageAdapter.ViewHolder> {
    List<VillagePojo> villagePojos= null;
    private OnItemClickListener mListener;
    private Context mContext;
    private Integer  villageId;
    private String telphone;
    private Integer areaId;
    public VillageAdapter(Context context, List<VillagePojo> villagePojos,String telphone,Integer areaId) {
        this.villagePojos = villagePojos;
        this.mContext= context;
        this.telphone=telphone;
        this.areaId =areaId;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        final View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.village_cardview,viewGroup,false);
        itemView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(mListener != null)
                    mListener.onItemClick(v, (String) itemView.getTag());
            }

        });
        return new VillageAdapter.ViewHolder(itemView);
    }
    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, int i) {

        String name = villagePojos.get(i).getName();
        Integer id =villagePojos.get(i).getId();
        viewHolder.bindData(name, id);
        viewHolder.itemView.setTag(i);
        viewHolder.nameView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String id =  (String)viewHolder.idView.getText();
                Intent intent = new Intent();
                intent.putExtra("villageId", id);
                intent.putExtra("telphone",telphone);
                intent.putExtra("areaId",areaId);
                intent.setClass(mContext, FarmerInfoActivity.class);
                mContext.startActivity(intent);
            }
        });
    }
    @Override
    public int getItemCount() {
        return villagePojos.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder
    {
        private TextView nameView;
        private TextView idView;
        public ViewHolder(View itemView)
        {
            super(itemView);
            nameView = (TextView) itemView.findViewById(R.id.village_name);
            idView = (TextView) itemView.findViewById(R.id.village_id);
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

    public void setOnItemClickListener(VillageAdapter.OnItemClickListener listener)
    {
        this.mListener = listener;
    }
}
