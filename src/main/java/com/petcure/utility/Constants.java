package com.petcure.utility;

public class Constants {
	
	public enum UserRole {
		ADMIN("admin"),
		PET_OWNER("owner"),
		DOCTOR("doctor");
		
		
		private String role;

	    private UserRole(String role) {
	      this.role = role;
	    }

	    public String value() {
	      return this.role;
	    }    
	}
	
	public enum ActiveStatus {
		ACTIVE("Active"), DEACTIVATED("Deactivated");

		private String status;

		private ActiveStatus(String status) {
			this.status = status;
		}

		public String value() {
			return this.status;
		}
	}
	
	public enum Sex {
		MALE("Male"),
		FEMALE("Female");
		
		
		private String sex;

	    private Sex(String sex) {
	      this.sex = sex;
	    }

	    public String value() {
	      return this.sex;
	    }    
	}
	
	public enum AppointmentStatus {
		BOOKED("Booked"),
		CANCEL("Cancel"),
		TREATMENT_DONE("Treatment Done"),
		TREATMENT_PENDING("Treatment Pending");
		
		private String status;

	    private AppointmentStatus(String status) {
	      this.status = status;
	    }

	    public String value() {
	      return this.status;
	    }    
	}
	
	public enum ResponseCode {
		SUCCESS(0),
		FAILED(1);
		
		private int code;

	    private ResponseCode(int code) {
	      this.code = code;
	    }

	    public int value() {
	      return this.code;
	    }    
	}
	
	public enum BloodGroup {
		A_POSITIVE("A+"),
		A_NEGATIVE("A-"),
		B_POSITIVE("B+"),
		B_NEGATIVE("B-"),
		O_POSITIVE("O+"),
		O_NEGATIVE("O-"),
		AB_POSITIVE("AB+"),
		AB_NEGATIVE("AB-");
		
		private String type;

	    private BloodGroup(String type) {
	      this.type = type;
	    }

	    public String value() {
	      return this.type;
	    }

	    public boolean equals(String type) {
	      return this.type.equals(type.toUpperCase());
	    }
	}
	
    public enum DoctorSpecialist {
		
    	ANIMAL_BEHAVIORISTS("Animal Behaviorists"),
        CARDIOLOGISTS("Cardiologists"),
        DERMATOLOGISTS("Dermatologists"),
        EMERGENCY_MEDICINE("Emergency Medicine"),
        INTERNAL_MEDICINE("Internal Medicine"),
        ONCOLOGISTS("Oncologists"),
        SURGEONS("Surgeons");
		
		private String type;

	    private DoctorSpecialist(String type) {
	      this.type = type;
	    }

	    public String value() {
	      return this.type;
	    }

	    public boolean equals(String type) {
	      return this.type.equals(type.toUpperCase());
	    }
	}
    
    public enum TimeSlot {
		NINE_TO_TEN_AM("09:00 - 10:00 am"),
		TEN_TO_ELEVEN_AM("10:00 - 11:00 am"),
		ELEVEN_TO_TWELLVE_AM("11:00 - 12:00 am"),
		TWELVE_TO_ONE_PM("12:00 - 01:00 pm"),
		ONE_TO_TWO_PM("01:00 - 02:00 pm"),
		TWO_TO_THREE_PM("02:00 - 03:00 pm"),
		THREE_TO_FOUR_PM("03:00 - 04:00 pm"),
		FOUR_TO_FIVE_PM("04:00 - 05:00 pm"),
		FIVE_TO_SIX_PM("05:00 - 06:00 pm"),
		SIX_TO_SEVEN_PM("06:00 - 07:00 pm"),
		SEVEN_TO_EIGHT_PM("07:00 - 08:00 pm"),
		EIGHT_TO_NINE_PM("08:00 - 09:00 pm"),
		NINE_TO_TEN_PM("09:00 - 10:00 pm");
		
		private String time;

	    private TimeSlot(String time) {
	      this.time = time;
	    }

	    public String value() {
	      return this.time;
	    }
	     
	}
    
    public enum UserStatus {
		ACTIVE(0),
		DELETED(1);
		
		private int status;

	    private UserStatus(int status) {
	      this.status = status;
	    }

	    public int value() {
	      return this.status;
	    }    
	}
    
    public enum PaymentGatewayTxnType {
		CREATE_ORDER("Create Order"), PAYMENT("Payment");

		private String type;

		private PaymentGatewayTxnType(String type) {
			this.type = type;
		}

		public String value() {
			return this.type;
		}
	}

	public enum PaymentGatewayTxnStatus {
		SUCCESS("Success"), FAILED("Failed");

		private String type;

		private PaymentGatewayTxnStatus(String type) {
			this.type = type;
		}

		public String value() {
			return this.type;
		}
	}

	
}
