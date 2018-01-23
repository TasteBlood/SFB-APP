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
import com.zhkj.sfb.R;
import com.zhkj.sfb.common.CommonBean;
import com.zhkj.sfb.common.OutJsonUtil;
import com.zhkj.sfb.common.ServiceUtil;
import com.zhkj.sfb.pojo.CropPojo;
import com.zhkj.sfb.pojo.FarmerPojo;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
/**
 * Created by frank on 2017-05-05.
 */

public class FarmerAdapter extends RecyclerView.Adapter<FarmerAdapter.ViewHolder> {
    private OnItemClickListener mListener;
    List<FarmerPojo> farmerPojos = null;
    private Context mContext;
    private RecyclerView plotView;
    private Integer farmerId;
    private List<CropPojo> cropPojos;
    private String telphone;
    private Integer areaId;
    private static String URL="http://shifei.yungoux.com/zhkj/getPlot.do";
    public FarmerAdapter(Context context, List<FarmerPojo> farmerPojos,RecyclerView plotView,String telphone,Integer areaId){
        this.farmerPojos = farmerPojos;
        this.mContext= context;
        this.plotView = plotView;
        this.telphone=telphone;
        this.areaId =areaId;

    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        final View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.farmer_info_cardview,viewGroup,false);
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
    public void onBindViewHolder(final ViewHolder holder, int i) {
             String farmerName = farmerPojos.get(i).getFarmerName();
             Integer id= farmerPojos.get(i).getId();
             holder.bindData(farmerName,id);
             holder.itemView.setTag(i);
        //点击地块编号，设置监听事件
        holder.nameView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String id =  holder.idView.getText().toString();
                farmerId = Integer.parseInt(id);
                plotView.setVisibility(View.GONE);
           new Thread(){
             @Override
             public void run() {
                 super.run();
                 new plotTask().execute();
             }
           }.start();}});
    }
    @Override
    public int getItemCount() {
        return farmerPojos.size();
    }
    /**
     * 查询地块信息
     */
public class  plotTask extends AsyncTask<Void,Void,String>{
        @Override
        protected String doInBackground(Void... params) {
            try {
                cropPojos = new ArrayList<CropPojo>();
                String str="farmerId="+farmerId;
                String result = ServiceUtil.sendPostRequest(URL,str);
                CommonBean commonBeans = OutJsonUtil.json2Bean(result,CommonBean.class);
                String infos = OutJsonUtil.toJson(commonBeans.getInfo());
                if(commonBeans.getStatus()==200){
                    Type type = new TypeToken<ArrayList<CropPojo>>() {}.getType();
                    cropPojos = OutJsonUtil.json2List(infos,type);
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
            if (cropPojos!=null && cropPojos.size()>0) {
                plotView.setVisibility(View.VISIBLE);
                plotView.setLayoutManager(new LinearLayoutManager(mContext));
                PlotAdapter plotAdapter = new PlotAdapter(mContext,cropPojos,telphone,areaId);
                plotView.setHasFixedSize(true);
                plotView.setAdapter(plotAdapter);
            }else{
                Toast toast = Toast.makeText(mContext, "没有地块信息", Toast.LENGTH_SHORT);
                toast.show();
            }
        }
    }
    public static class ViewHolder extends RecyclerView.ViewHolder
    {
        private TextView nameView;
        private TextView idView;
        public ViewHolder(View itemView)
        {
            super(itemView);
            nameView = (TextView) itemView.findViewById(R.id.farmer_info_name);
          idView = (TextView) itemView.findViewById(R.id.farmer_info_id);
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

    public void setOnItemClickListener(FarmerAdapter.OnItemClickListener listener)
    {
        this.mListener = listener;
    }

}
