package com.zhkj.sfb.pojo;

import java.io.Serializable;

/**
 * 村、社区
 * Created by frank on 2017-05-04.
 */

public class VillagePojo implements Serializable {

    private Integer  id;
    private String name;
    private Integer streetId;

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setId(Integer id) {
        this.id = id;
    }
    public void  setStreetId(Integer streetId){
        this.streetId = streetId;
    }
    public Integer getStreetId(){
        return streetId;
    }


}

