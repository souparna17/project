package com.petcure.dto;

import java.util.ArrayList;
import java.util.List;

import com.petcure.entity.Location;

import lombok.Data;

@Data
public class LocationResponseDto extends CommanApiResponse {

	private List<Location> locations = new ArrayList<>();

	public List<Location> getLocations() {
		return locations;
	}

	public void setLocations(List<Location> locations) {
		this.locations = locations;
	}
	
	
	
}
