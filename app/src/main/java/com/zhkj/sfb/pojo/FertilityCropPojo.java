package com.zhkj.sfb.pojo;

import java.io.Serializable;

/**
 * Created by frank on 2017-05-05.
 */

public class FertilityCropPojo implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = -2593905693644684507L;
	private Integer  id;
	private String name;

	private String yield1;
	private String yield2;
	private String yield3;
	private String description;

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public FertilityCropPojo(Integer id, String yield3, String yield2, String yield1, String name) {
		this.id = id;
		this.yield3 = yield3;
		this.yield2 = yield2;
		this.yield1 = yield1;
		this.name = name;
	}

	public Integer  getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public String getYield1() {
		return yield1;
	}

	public String getYield2() {
		return yield2;
	}

	public String getYield3() {
		return yield3;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setYield1(String yield1) {
		this.yield1 = yield1;
	}

	public void setYield2(String yield2) {
		this.yield2 = yield2;
	}

	public void setYield3(String yield3) {
		this.yield3 = yield3;
	}

	@Override
	public String toString() {
		return name;
	}
}
