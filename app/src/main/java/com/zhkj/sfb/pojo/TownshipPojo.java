package com.zhkj.sfb.pojo;
import java.io.Serializable;
/**   街道/乡镇
 * */
public class TownshipPojo implements Serializable{
	private static final long serialVersionUID = 1L;
	/**
	行政单位
	*/
	private Integer id;
	/**
	
	*/
	private String name;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	
}