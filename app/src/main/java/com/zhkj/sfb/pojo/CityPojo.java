package com.zhkj.sfb.pojo;
import java.io.Serializable;
/**
*3690技术提供*/
public class CityPojo implements Serializable{
	private static final long serialVersionUID = 1L;
	/**
	
	*/
	private Integer id;
	/**
	
	*/
	private String name;
	/**
	
	*/
	private Integer provinceId;
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
	public Integer getProvinceId() {
		return provinceId;
	}
	public void setProvinceId(Integer provinceId) {
		this.provinceId = provinceId;
	}
	
}