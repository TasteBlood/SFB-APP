package com.zhkj.sfb;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;
import com.zhkj.sfb.common.BasetActivity;
import com.zhkj.sfb.common.ServiceUtil;
import com.zhkj.sfb.common.ToastUtils;
import com.zhkj.sfb.common.UpdateStatus;
import com.zhkj.sfb.common.UpdateVersionUtil;
import com.zhkj.sfb.pojo.VersionInfo;

import java.io.InputStream;

public class AboutActivity extends BasetActivity {
    private String resultData;
    private ImageView imageView;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        TextView theme = (TextView) findViewById(R.id.theme);
        imageView = (ImageView) findViewById(R.id.about_image);
   //      Bitmap bitmap = getHttpBitmap("http://images.csdn.net/20130609/zhuanti.jpg");
        theme.setText("关于我们");
        new Thread() {
            @Override
            public void run() {
                super.run();
                new DownloadImageTask(imageView).execute();
            }
        }.start();
        new Thread() {
            @Override
            public void run() {
                super.run();
                new JsonTask().execute();
            }
        }.start();
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    public void toHelp(View v) {
        Intent intent = new Intent(AboutActivity.this, AboutHelpActivity.class);
        startActivity(intent);
    }

    public void toRelief(View v) {
        Intent intent = new Intent(AboutActivity.this, AboutReliefActivity.class);
        startActivity(intent);
    }

    public void toInc(View v) {
        Intent intent = new Intent(AboutActivity.this, AboutIncActivity.class);
        startActivity(intent);
    }

    public void toHistory(View v) {
        Intent intent = new Intent(AboutActivity.this, HistoryActivity.class);
        startActivity(intent);
    }

    public void toDownload(View v) {

        //访问服务器 试检测是否有新版本发布
        UpdateVersionUtil.checkVersion(AboutActivity.this, resultData, new UpdateVersionUtil.UpdateListener() {

            @Override
            public void onUpdateReturned(int updateStatus, VersionInfo versionInfo) {
                //判断回调过来的版本检测状态
                switch (updateStatus) {
                    case UpdateStatus.YES:
                        //弹出更新提示
                        UpdateVersionUtil.showDialog(AboutActivity.this, versionInfo);
                        break;
                    case UpdateStatus.NO:
                        //没有新版本
                        ToastUtils.showToast(getApplicationContext(), "已经是最新版本了!");
                        break;
                    case UpdateStatus.NOWIFI:
                        //当前是非wifi网络
                        ToastUtils.showToast(getApplicationContext(), "只有在wifi下更新！");
                        //							DialogUtils.showDialog(MainActivity.this, "温馨提示","当前非wifi网络,下载会消耗手机流量!", "确定", "取消",new DialogOnClickListenner() {
                        //								@Override
                        //								public void btnConfirmClick(Dialog dialog) {
                        //									dialog.dismiss();
                        //									//点击确定之后弹出更新对话框
                        //									UpdateVersionUtil.showDialog(SystemActivity.this,versionInfo);
                        //								}
                        //
                        //								@Override
                        //								public void btnCancelClick(Dialog dialog) {
                        //									dialog.dismiss();
                        //								}
                        //							});
                        break;
                    case UpdateStatus.ERROR:
                        //检测失败
                        ToastUtils.showToast(getApplicationContext(), "检测失败，请稍后重试！");
                        break;
                    case UpdateStatus.TIMEOUT:
                        //链接超时
                        ToastUtils.showToast(getApplicationContext(), "链接超时，请检查网络设置!");
                        break;
                }
            }
        });
    }

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("About Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        client.disconnect();
    }

    public class JsonTask extends AsyncTask<Void, Void, String> {
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
        }

        @Override
        protected String doInBackground(Void... param) {
            try {
                resultData = ServiceUtil.SendGET("http://json-shifei.yungoux.com/json.json");
                return resultData;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public DownloadImageTask(ImageView imageView) {
            this.bmImage = imageView;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = "http://qn-shifei.yungoux.com/erweima.png";
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
        }
    }
}

