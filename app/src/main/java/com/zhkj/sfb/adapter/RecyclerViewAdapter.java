package com.zhkj.sfb.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.zhkj.sfb.R;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    private Context mContext;
    public RecyclerViewAdapter(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view =
                LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_card_main, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int i) {
        Log.d("===========i===",i+"");
        //设置WebView属性，能够执行Javascript脚本
        holder.webView.getSettings().setJavaScriptEnabled(true);
        //设置打开的页面地址
        if(i==0){
            holder.webView.loadUrl("http://shifei.yungoux.com/res/html/rules/weats.html");
        }else if (1== 1){
            holder.webView.loadUrl("http://shifei.yungoux.com/res/html/rules/corn.html");
        }else {
            holder.webView.loadUrl("http://shifei.yungoux.com/res/html/rules/cotton.html");
        }
        holder.webView.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                // TODO Auto-generated method stub
                //返回值是true的时候控制去WebView打开，为false调用系统浏览器或第三方浏览器
                view.loadUrl(url);
                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return 11;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        private WebView webView;

        public ViewHolder(View view) {
            super(view);
            webView = (WebView)view.findViewById(R.id.rule_webview);
//            //设置WebView属性，能够执行Javascript脚本
//            webView.getSettings().setJavaScriptEnabled(true);
//            //设置打开的页面地址
//            if(getAdapterPosition()==0){
//                webView.loadUrl("http://shifei.yungoux.com/res/html/rules/weats.html");
//            }else if (getAdapterPosition()== 1){
//                webView.loadUrl("http://shifei.yungoux.com/res/html/rules/corn.html");
//            }else {
//                webView.loadUrl("http://shifei.yungoux.com/res/html/rules/cotton.html");
//            }
//            webView.setWebViewClient(new WebViewClient(){
//                @Override
//                public boolean shouldOverrideUrlLoading(WebView view, String url) {
//                    // TODO Auto-generated method stub
//                    //返回值是true的时候控制去WebView打开，为false调用系统浏览器或第三方浏览器
//                    view.loadUrl(url);
//                    return true;
//                }
//            });
            mView = view;
        }
    }
}
