package com.guuidea.component.ip.domain;

import java.io.Serializable;

public class IpRange implements Comparable<IpRange>, Serializable {
	private static final long serialVersionUID = 1382115218788929292L;
	private long ipStartNum;
	private long ipEndNum;
	private int provinceId = -1;
	private int cityId = -1;
	private int countryId = -1;
	private String countryName;
	private String countryCode;
	private String startIp;
	private String endIp;
	

	public String getCountryCode() {
		return countryCode;
	}

	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}

	public String getStartIp() {
		return startIp;
	}

	public void setStartIp(String startIp) {
		this.startIp = startIp;
	}

	public String getEndIp() {
		return endIp;
	}

	public void setEndIp(String endIp) {
		this.endIp = endIp;
	}

	public String getCountryName() {
		return countryName;
	}

	public void setCountryName(String countryName) {
		this.countryName = countryName;
	}

	public IpRange(long _ipStartNum, long _ipEndNum) {
		this.ipStartNum = Math.min(_ipStartNum, _ipEndNum);
		this.ipEndNum = Math.max(_ipStartNum, _ipEndNum);
	}

	public IpRange() {
	}

	public long getIpStartNum() {
		return this.ipStartNum;
	}

	public void setIpStartNum(long ipStartNum) {
		this.ipStartNum = ipStartNum;
	}

	public long getIpEndNum() {
		return this.ipEndNum;
	}

	public void setIpEndNum(long ipEndNum) {
		this.ipEndNum = ipEndNum;
	}

	public int getProvinceId() {
		return this.provinceId;
	}

	public void setProvinceId(int provinceId) {
		this.provinceId = provinceId;
	}

	public int getCityId() {
		return this.cityId;
	}

	public void setCityId(int cityId) {
		this.cityId = cityId;
	}

	public int getCountryId() {
		return this.countryId;
	}

	public void setCountryId(int countryId) {
		this.countryId = countryId;
	}

	boolean isInclude(long ip) {
		return (ip >= this.ipStartNum) && (ip <= this.ipEndNum);
	}

	public int compareTo(IpRange o) {
		if (this.ipStartNum > o.ipEndNum) {
			return 1;
		}
		if (this.ipEndNum < o.ipStartNum) {
			return -1;
		}
		if ((this.ipStartNum == o.ipStartNum) && (this.ipEndNum == o.ipEndNum)) {
			return 0;
		}
		if ((o.ipStartNum == o.ipEndNum) && (isInclude(o.ipStartNum))) {
			return 0;
		}
		throw new RuntimeException("invliad IpRange compare.");
	}
}
