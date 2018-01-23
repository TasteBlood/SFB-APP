package com.zhkj.sfb;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.zhkj.sfb.common.BasetActivity;

public class FertilizerInfoActivity extends BasetActivity {
    private WebView webView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fertilizer_info);
        TextView theme = (TextView) findViewById(R.id.theme);
        theme.setText("肥料信息");
        Intent intent = getIntent();
        String areaId = intent.getStringExtra("areaId");
        //实例化WebView对象
        webView = (WebView)findViewById(R.id.fertilizer_webview);
        //设置WebView属性，能够执行Javascript脚本
        webView.getSettings().setJavaScriptEnabled(true);


        //设置打开的页面地址
        if(areaId.equals("") || areaId == null){
            webView.loadUrl("http://shifei.yungoux.com/res/html/249_contact.html");
        }else {
            webView.loadUrl("http://shifei.yungoux.com/res/html/"+areaId+"_contact.html");
        }
        webView.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                // TODO Auto-generated method stub
                //返回值是true的时候控制去WebView打开，为false调用系统浏览器或第三方浏览器
                if (url.startsWith("tel:")){
                    Intent intent = new Intent(Intent.ACTION_VIEW,
                            Uri.parse(url));
                    startActivity(intent);
                    return true;
                }
                view.loadUrl(url);
                return true;
            }
        });
    }
}
