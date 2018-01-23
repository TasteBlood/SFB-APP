package com.zhkj.sfb.common;

/**
 * Created by Administrator on 2016/9/21.
 */
public class WeatherBean {
    private Object result;
    private Integer resultcode;
    private String reason;

    public Object getResult() {
        return result;
    }

    public void setResult(Object result) {
        this.result = result;
    }

    public Integer getResultcode() {
        return resultcode;
    }

    public void setResultcode(Integer resultcode) {
        this.resultcode = resultcode;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}
