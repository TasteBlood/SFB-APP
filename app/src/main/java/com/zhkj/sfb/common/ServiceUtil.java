package com.zhkj.sfb.common;

import android.util.Log;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/9/21.
 */
public class ServiceUtil {
    /**
     * 从指定的URL中获取数组
     * @param urlPath
     * @return
     * @throws Exception
     */
    public static String readParse(String urlPath) throws Exception {
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        byte[] data = new byte[1024];
        int len = 0;
        URL url = new URL(urlPath);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        InputStream inStream = conn.getInputStream();
        while ((len = inStream.read(data)) != -1) {
            outStream.write(data, 0, len);
        }
        inStream.close();
        return new String(outStream.toByteArray());//通过out.Stream.toByteArray获取到写的数据
    }
    /**
     * 通过HttpURLConnection模拟post表单提交
     *
     * @param path
     * @param params 例如"name=zhangsan&age=21"
     * @return
     * @throws Exception
     */
    public static String sendPostRequest(String path, String params){
        try {
            ByteArrayOutputStream outStream = new ByteArrayOutputStream();
            URL url = new URL(path);
            int len = 0;
            byte[] data = new byte[1024];
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");// 提交模式
            // conn.setConnectTimeout(10000);//连接超时 单位毫秒
            // conn.setReadTimeout(2000);//读取超时 单位毫秒
            conn.setDoOutput(true);// 是否输入参数
            byte[] bypes = params.toString().getBytes();
            conn.getOutputStream().write(bypes);// 输入参数
            InputStream inStream=conn.getInputStream();
            while ((len = inStream.read(data)) != -1) {
                outStream.write(data, 0, len);
            }
            inStream.close();
            return new String(outStream.toByteArray());//通过out.Stream.toByteArray获取到写的数据
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }
    public static String SendGET(String url){
        String result="";//访问返回结果
        BufferedReader read=null;//读取访问结果

        try {
            //创建url
            URL realurl=new URL(url);
            //打开连接
            URLConnection connection=realurl.openConnection();
            // 设置通用的请求属性
            connection.setRequestProperty("accept", "*/*");
            connection.setRequestProperty("connection", "Keep-Alive");
//            connection.setRequestProperty("user-agent",
//                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            //建立连接
            connection.connect();
            // 获取所有响应头字段
            Map<String, List<String>> map = connection.getHeaderFields();
            // 遍历所有的响应头字段，获取到cookies等
            for (String key : map.keySet()) {
                System.out.println(key + "--->" + map.get(key));
            }
            // 定义 BufferedReader输入流来读取URL的响应
            read = new BufferedReader(new InputStreamReader(
                    connection.getInputStream(),"UTF-8"));
            String line;//循环读取
            while ((line = read.readLine()) != null) {
                result += line;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally{
            if(read!=null){//关闭流
                try {
                    read.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return result;
    }


    public static String uploadFile(String uploadUrl, File uploadFile){
        String end ="\r\n";
        String twoHyphens ="--";
        String boundary ="*****";
        try
        {
            URL url =new URL(uploadUrl);
            HttpURLConnection con=(HttpURLConnection)url.openConnection();
          /* 允许Input、Output，不使用Cache */
            con.setDoInput(true);
            con.setDoOutput(true);
            con.setUseCaches(false);
          /* 设置传送的method=POST */
            con.setRequestMethod("POST");
          /* setRequestProperty */
            con.setRequestProperty("Connection", "Keep-Alive");
            con.setRequestProperty("Charset", "UTF-8");
            con.setRequestProperty("Content-Type",
                    "multipart/form-data;boundary="+boundary);
          /* 设置DataOutputStream */
            DataOutputStream ds =
                    new DataOutputStream(con.getOutputStream());
            ds.writeBytes(twoHyphens + boundary + end);
            ds.writeBytes("Content-Disposition: form-data; "+ "name=\"url\";filename=\""+ uploadFile.getName() +"\""+ end);
            ds.writeBytes(end);
          /* 取得文件的FileInputStream */
            FileInputStream fStream =new FileInputStream(uploadFile);
          /* 设置每次写入1024bytes */
            int bufferSize =1024;
            byte[] buffer =new byte[bufferSize];
            int length =-1;
          /* 从文件读取数据至缓冲区 */
            while((length = fStream.read(buffer)) !=-1)
            {
            /* 将资料写入DataOutputStream中 */
                ds.write(buffer, 0, length);
            }
            ds.writeBytes(end);
            ds.writeBytes(twoHyphens + boundary + twoHyphens + end);
          /* close streams */
            fStream.close();
            ds.flush();
          /* 取得Response内容 */
            InputStream is = con.getInputStream();
            int ch;
            StringBuffer b =new StringBuffer();
            while( ( ch = is.read() ) !=-1 )
            {
                b.append( (char)ch );

            }

          /* 关闭DataOutputStream */
            ds.close();
            return b.toString();
        } catch(Exception e){
            Log.d("错误======",e.toString());
            e.printStackTrace();
            return null;
        }
    }
}
