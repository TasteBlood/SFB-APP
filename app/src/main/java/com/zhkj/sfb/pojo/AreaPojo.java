package com.zhkj.sfb.pojo;
import java.io.Serializable;
/**
*3690技术提供*/
public class AreaPojo implements Serializable{
	private static final long serialVersionUID = 1L;
	/**
	
	*/
	private Integer id;
	/**
	
	*/
	private String name;
	/**
	
	*/
	private Integer cityId;
	private String cityName;
	private String gisUrl;
    private String telephone;

	public String getTelephone() {
		return telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	public String getGisUrl() {
		return gisUrl;
	}

	public void setGisUrl(String gisUrl) {
		this.gisUrl = gisUrl;
	}

	public String getCityName() {
		return cityName;
	}

	public void setCityName(String cityName) {
		this.cityName = cityName;
	}

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
	public Integer getCityId() {
		return cityId;
	}
	public void setCityId(Integer cityId) {
		this.cityId = cityId;
	}
	public Integer getProvinceId() {
		return provinceId;
	}
	public void setProvinceId(Integer provinceId) {
		this.provinceId = provinceId;
	}
	
}