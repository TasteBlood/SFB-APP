package com.zhkj.sfb.common;

import com.google.gson.Gson;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class OutJsonUtil {
	/** 
     * 将java对象转换成json字符串 
     */  
    public static <T> T json2Bean(String jsonString, Class<T> cls) {
        T t = null;  
        try {  
            Gson gson = new Gson();
            t = gson.fromJson(jsonString, cls);  
        } catch (Exception e) {
            e.printStackTrace();  
        }  
        return t;  
    }  
  
    /** 
     * 将对象转换成json数据 
     *  
     * @param o 
     * @return 
     */  
    public static <T> String toJson(Object o) {
        Gson gson = new Gson();
        String jsonStr = gson.toJson(o);
        return jsonStr;  
    }  
  
    /** 
     * 将json数据转换成List列表返回 
     *  
     * @param jsonString 
     * @param type 
     * @return 
     */  
    public static <T> List<T> json2List(String jsonString, Type type) {
        List<T> list = new ArrayList<T>();
        try {  
            Gson gson = new Gson();
            list = gson.fromJson(jsonString, type);  
        } catch (Exception e) {
        }  
        return list;  
    }  
  
     public static void main(String[] args) {
   /*  String jsonStr = "[{\"name\":\"ice\",\"age\":20}]";  
     List<UserPojo> list = json2List(jsonStr,  
     new TypeToken<List<UserPojo>>() {  
     }.getType());  
     for (UserPojo user : list)  
     System.out.println( user.getName()+""); */ 
     }  

}
