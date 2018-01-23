package com.zhkj.sfb.pojo;

import java.io.Serializable;

public class FertilityInfoPojo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8875728081221359192L;
	private String elementO;
	private String elementN;
	private String elementP;
	private String elementK;
	private String bili;

	public String getBili() {
		return bili;
	}
	public String getElementO() {
		return elementO;
	}

	public String getElementN() {
		return elementN;
	}

	public String getElementP() {
		return elementP;
	}

	public String getElementK() {
		return elementK;
	}
	public void setBili(String bili) {
		this.bili = bili;
	}

	public void setElementO(String elementO) {
		this.elementO = elementO;
	}

	public void setElementN(String elementN) {
		this.elementN = elementN;
	}

	public void setElementP(String elementP) {
		this.elementP = elementP;
	}

	public void setElementK(String elementK) {
		this.elementK = elementK;
	}

}
