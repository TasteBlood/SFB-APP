package com.zhkj.sfb;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.RemoteViews;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.HttpHandler;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.zhkj.sfb.common.ApkUtils;
import com.zhkj.sfb.common.PreferenceUtils;
import com.zhkj.sfb.common.SDCardUtils;
import com.zhkj.sfb.common.UpdateVersionUtil;

import java.io.File;

/**
 *
 *	下载新版本的服务类
 */
public class UpdateVersionService extends Service {

	
	private NotificationManager nm;
	private Notification notification;
	//标题标识
	private int titleId = 0;
	//安装文件
	private File updateFile;
	
	private static HttpHandler<File> httpHandler;
	private HttpUtils httpUtils;
	
	private long initTotal = 0;//文件的总长度
	
	@Override
	public void onCreate() {
		super.onCreate();
		
		httpUtils = new HttpUtils();
		updateFile = new File(SDCardUtils.getRootDirectory()+"/updateVersion/SFB-APP.apk");
		nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
		notification = new Notification();
		notification.icon = R.mipmap.logo;
		notification.tickerText = "开始下载";
		notification.when = System.currentTimeMillis();
		notification.contentView = new RemoteViews(getPackageName(), R.layout.notifycation);
		
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		
//		VersionInfo versionInfo = (VersionInfo) intent.getSerializableExtra("versionInfo");
//		String url = versionInfo.getDownloadUrl();
		Bundle bundle = intent.getExtras();
		String url = bundle.getString("downloadUrl");
		Log.d("下载地址：",url);
		PreferenceUtils.setString(UpdateVersionService.this, "apkDownloadurl", url);
		
		nm.notify(titleId, notification);
		downLoadFile(url);
		return super.onStartCommand(intent, flags, startId);
	}

	
	
	public void downLoadFile(String url){
	//	Log.d("客户端文件",updateFile.getAbsolutePath());
	//	Log.d("SD卡存储路径",SDCardUtils.getRootDirectory()+"");
	//	Log.d("SD卡是否可用",SDCardUtils.isAvailable()+"");
	//	Log.d("SD卡状态",SDCardUtils.getState()+"");
		httpHandler = httpUtils.download(url,updateFile.getAbsolutePath(), true, false, new RequestCallBack<File>() {
			
			@Override
			public void onSuccess(ResponseInfo<File> response) {
				// 更改文字
                notification.contentView.setTextViewText(R.id.msg, "下载完成!点击安装");
//                notification.contentView.setViewVisibility(R.id.btnStartStop, View.GONE);
//                notification.contentView.setViewVisibility(R.id.btnCancel,View.GONE);
                // 发送消息
                nm.notify(0, notification);
                stopSelf();
                //收起通知栏
                UpdateVersionUtil.collapseStatusBar(UpdateVersionService.this);
                //自动安装新版本
                Intent installIntent = ApkUtils.getInstallIntent(updateFile);
                startActivity(installIntent);
                
			}
			
			@Override
			public void onFailure(HttpException error, String msg) {
				//网络连接错误
				if(error.getExceptionCode() == 0 ){
					// 更改文字
	                notification.contentView.setTextViewText(R.id.msg, "网络异常,请检查网络设置！如网络已设置，请打开存储权限");
				}else if(error.getExceptionCode() == 416){//文件已经下载完毕
					// 更改文字
	                notification.contentView.setTextViewText(R.id.msg, "丝路施肥");
	                // 更改文字
	                notification.contentView.setTextViewText(R.id.bartext, "检测到新版本已经下载完成，点击即安装!");
	                // 隐藏进度条
	                notification.contentView.setViewVisibility(R.id.progressBar1, View.GONE);
	                
	                Intent intent = ApkUtils.getInstallIntent(updateFile);
	                PendingIntent pendingIntent = PendingIntent.getActivity(UpdateVersionService.this, 0, intent, 0);
	                notification.flags = Notification.FLAG_AUTO_CANCEL;//点击通知栏之后 消失
	                notification.contentIntent  = pendingIntent;//启动指定意图
				}
                // 发送消息
                nm.notify(0, notification);
			}

			@Override
			public void onLoading(long total, long current, boolean isUploading) {
				if(initTotal == 0){//说明第一次开始下载
					initTotal = total;
				}
				
				if(initTotal != total){//说明下载过程中暂停过，文件的总长度出现问题  就把初始的文件的长度赋值给他重新计算已经下载的比例
					total = initTotal;
				}
				
				long l = current*100/total;
				notification.contentView.setTextViewText(R.id.msg, "正在下载：丝路施肥");
				// 更改文字
                notification.contentView.setTextViewText(R.id.bartext, l+ "%");
                // 更改进度条
                notification.contentView.setProgressBar(R.id.progressBar1, 100,(int)l, false);
                // 发送消息
                nm.notify(0, notification);
                
//              Intent intent = new Intent();
//				intent.setAction("cancel");
//				PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, intent, 0);
//				notification.contentView.setOnClickPendingIntent(R.id.btnStartStop, pendingIntent);
				
			}

			@Override
			public void onStart() {
				notification.contentView.setTextViewText(R.id.msg, "开始下载：丝路施肥");
				nm.notify(titleId, notification);
			}
			
		});
	}
	
	
	public static HttpHandler<File> getHandler(){
		return httpHandler;
	}
	
	
	@Override
	public void onDestroy() {
		//下载完成时，清楚该通知，自动安装
		nm.cancel(titleId);
		System.out.println("UpdateVersionService----onDestroy");
//		try {
//			GdmsaecApplication.db.deleteAll(VersionInfo.class);
//		} catch (DbException e) {
//			e.printStackTrace();
//		}
		super.onDestroy();
	}
	
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}
	
	
}
