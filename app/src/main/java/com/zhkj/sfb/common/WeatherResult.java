package com.zhkj.sfb.common;

/**
 * Created by Administrator on 2016/12/19.
 */

public class WeatherResult {
    private Object sk;
    private Object today;

    private Object future;

    public Object getSk() {
        return sk;
    }

    public void setSk(Object sk) {
        this.sk = sk;
    }

    public Object getToday() {
        return today;
    }

    public void setToday(Object today) {
        this.today = today;
    }

    public Object getFuture() {
        return future;
    }

    public void setFuture(Object future) {
        this.future = future;
    }
}
