package com.zhkj.sfb.pojo;

import java.io.Serializable;

/**
 * Created by frank on 2017-05-04.
 */

public class StreetPojo implements Serializable {
    private Integer id;
    private String name;
    private Integer areaId;

    public Integer getId() {
        return id;
    }

    public Integer getAreaId() {
        return areaId;
    }

    public String getName() {
        return name;
    }

    public void setAreaId(Integer areaId) {
        this.areaId = areaId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
