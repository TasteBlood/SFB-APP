package com.zhkj.sfb.pojo;

import java.io.Serializable;

public class FarmerPojo implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer id;
	private Integer villageId;
	private String streetName;
	private String villageName;
	private String farmerName;

	public String getFarmerName() {
		return farmerName;
	}

	public void setFarmerName(String farmerName) {
		this.farmerName = farmerName;
	}

	public Integer getId() {
		return id;
	}

	public Integer getVillageId() {
		return villageId;
	}

	public String getStreetName() {
		return streetName;
	}

	public String getVillageName() {
		return villageName;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public void setVillageId(Integer villageId) {
		this.villageId = villageId;
	}

	public void setStreetName(String streetName) {
		this.streetName = streetName;
	}

	public void setVillageName(String villageName) {
		this.villageName = villageName;
	}

}
