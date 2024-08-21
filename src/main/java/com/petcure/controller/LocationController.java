package com.petcure.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.petcure.dao.LocationDao;
import com.petcure.dto.CommanApiResponse;
import com.petcure.dto.LocationResponseDto;
import com.petcure.entity.Location;
import com.petcure.utility.Constants.ActiveStatus;
import com.petcure.utility.Constants.ResponseCode;

@RestController
@RequestMapping("api/location")
@CrossOrigin(origins = "http://localhost:3000")
public class LocationController {

	@Autowired
	private LocationDao locationDao;

	@PostMapping("/add")
	public ResponseEntity<CommanApiResponse> addLocation(@RequestBody Location location) {

		CommanApiResponse response = new CommanApiResponse();

		if (location == null) {
			response.setResponseMessage("missing input");
			response.setResponseCode(ResponseCode.FAILED.value());

			return new ResponseEntity<CommanApiResponse>(response, HttpStatus.BAD_REQUEST);
		}

		location.setStatus(ActiveStatus.ACTIVE.value());

		Location savedLocation = this.locationDao.save(location);

		if (savedLocation == null) {
			response.setResponseMessage("Failed to save the location");
			response.setResponseCode(ResponseCode.FAILED.value());

			return new ResponseEntity<CommanApiResponse>(response, HttpStatus.BAD_REQUEST);
		}

		response.setResponseMessage("Location Added Successful");
		response.setResponseCode(ResponseCode.FAILED.value());

		return new ResponseEntity<CommanApiResponse>(response, HttpStatus.OK);

	}

	@PutMapping("/update")
	public ResponseEntity<CommanApiResponse> updateLocation(@RequestBody Location location) {

		CommanApiResponse response = new CommanApiResponse();

		if (location == null) {
			response.setResponseMessage("missing input");
			response.setResponseCode(ResponseCode.FAILED.value());

			return new ResponseEntity<CommanApiResponse>(response, HttpStatus.BAD_REQUEST);
		}

		if (location.getId() == 0) {
			response.setResponseMessage("missing location Id");
			response.setResponseCode(ResponseCode.FAILED.value());

			return new ResponseEntity<CommanApiResponse>(response, HttpStatus.BAD_REQUEST);
		}

		location.setStatus(ActiveStatus.ACTIVE.value());
		Location savedLocation = this.locationDao.save(location);

		if (savedLocation == null) {
			response.setResponseMessage("Failed to update the location");
			response.setResponseCode(ResponseCode.FAILED.value());

			return new ResponseEntity<CommanApiResponse>(response, HttpStatus.BAD_REQUEST);
		}

		response.setResponseMessage("Location Updated Successful");
		response.setResponseCode(ResponseCode.SUCCESS.value());

		return new ResponseEntity<CommanApiResponse>(response, HttpStatus.OK);

	}

	@GetMapping("/fetch/all")
	public ResponseEntity<LocationResponseDto> fetchAllLocation() {

		LocationResponseDto response = new LocationResponseDto();

		List<Location> locations = new ArrayList<>();

		locations = this.locationDao.findByStatus(ActiveStatus.ACTIVE.value());

		if (CollectionUtils.isEmpty(locations)) {
			response.setResponseMessage("No Locations found");
			response.setResponseCode(ResponseCode.FAILED.value());

			return new ResponseEntity<LocationResponseDto>(response, HttpStatus.OK);
		}

		response.setLocations(locations);
		response.setResponseMessage("Location fetched successful");
		response.setResponseCode(ResponseCode.SUCCESS.value());

		return new ResponseEntity<LocationResponseDto>(response, HttpStatus.OK);
	}

	@DeleteMapping("/delete")
	public ResponseEntity<CommanApiResponse> deleteLocation(@RequestParam("locationId") int locationId) {

		CommanApiResponse response = new CommanApiResponse();

		if (locationId == 0) {
			response.setResponseMessage("missing location Id");
			response.setResponseCode(ResponseCode.FAILED.value());

			return new ResponseEntity<CommanApiResponse>(response, HttpStatus.BAD_REQUEST);
		}

		Location location = this.locationDao.findById(locationId).get();

		if (location == null) {
			response.setResponseMessage("location not found");
			response.setResponseCode(ResponseCode.FAILED.value());

			return new ResponseEntity<CommanApiResponse>(response, HttpStatus.BAD_REQUEST);
		}

		location.setStatus(ActiveStatus.DEACTIVATED.value());
		Location updatedLocation = this.locationDao.save(location);

		if (updatedLocation == null) {
			response.setResponseMessage("Failed to  delete the doctor");
			response.setResponseCode(ResponseCode.FAILED.value());

			return new ResponseEntity<CommanApiResponse>(response, HttpStatus.BAD_REQUEST);
		}

		response.setResponseMessage("Location Deleted Successful");
		response.setResponseCode(ResponseCode.SUCCESS.value());

		return new ResponseEntity<CommanApiResponse>(response, HttpStatus.OK);

	}

}
