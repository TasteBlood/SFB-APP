package com.zhkj.sfb.pojo;
import java.io.Serializable;
/**
*3690技术提供*/
public class ElementPojo implements Serializable{
	private static final long serialVersionUID = 1L;
	/**
	
	*/
	private Integer id;
	/**
	元素名称
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