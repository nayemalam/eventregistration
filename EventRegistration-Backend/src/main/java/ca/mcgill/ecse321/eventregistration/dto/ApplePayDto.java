package ca.mcgill.ecse321.eventregistration.dto;

import java.util.List;

public class ApplePayDto {

	private String deviceId;
	private int amount;

	public ApplePayDto() {
	}

	public ApplePayDto(String deviceId, int amount) {
		this.deviceId = deviceId;
		this.amount = amount;
	}

	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	public int getAmount() {
		return amount;
	}

	public void setAmount(int amount) {
		this.amount = amount;
	}

}
