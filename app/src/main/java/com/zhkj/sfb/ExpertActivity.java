package com.zhkj.sfb;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.zhkj.sfb.common.BasetActivity;

public class ExpertActivity extends BasetActivity {
    private WebView webView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expert);
        TextView theme = (TextView) findViewById(R.id.theme);
        theme.setText("专家简介");
        Intent intent = getIntent();
        String areaId = intent.getStringExtra("areaId");
        //实例化WebView对象
        webView = (WebView)findViewById(R.id.expert_webview);
        //设置WebView属性，能够执行Javascript脚本
        webView.getSettings().setJavaScriptEnabled(true);
        //设置打开的页面地址
        webView.loadUrl("http://shifei.yungoux.com/res/html/"+areaId+"_expert.html");
        webView.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                // TODO Auto-generated method stub
                //返回值是true的时候控制去WebView打开，为false调用系统浏览器或第三方浏览器
                view.loadUrl(url);
                return true;
            }
        });
    }
}
