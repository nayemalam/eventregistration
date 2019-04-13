package ca.mcgill.ecse321.eventregistration.dto;

public class PayDto {

	private RegistrationDto registration;
	private ApplePayDto applePay;

	public PayDto() {
	}

	public PayDto(RegistrationDto registration, ApplePayDto applePay) {
		this.registration = registration;
		this.applePay = applePay;
	}
	
	public RegistrationDto getRegistration() {
		return registration;
	}
	
	public void setRegistration (RegistrationDto registration) {
		this.registration = registration;
	}

	public ApplePayDto getApplePay() {
		return applePay;
	}
	
	public void setApplePay (ApplePayDto applePay) {
		this.applePay = applePay;
	}

}
