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

import com.petcure.dao.PetDao;
import com.petcure.dto.CommanApiResponse;
import com.petcure.dto.PetResponse;
import com.petcure.entity.Location;
import com.petcure.entity.Pet;
import com.petcure.entity.User;
import com.petcure.service.UserService;
import com.petcure.utility.Constants.ActiveStatus;
import com.petcure.utility.Constants.ResponseCode;

@RestController
@RequestMapping("api/owner/pet")
@CrossOrigin(origins = "http://localhost:3000")
public class PetController {

	@Autowired
	private UserService userService;

	@Autowired
	private PetDao petDao;

	@PostMapping("/add")
	public ResponseEntity<CommanApiResponse> addPet(@RequestBody Pet pet) {

		CommanApiResponse response = new CommanApiResponse();

		if (pet == null) {
			response.setResponseMessage("missing input");
			response.setResponseCode(ResponseCode.SUCCESS.value());

			return new ResponseEntity<CommanApiResponse>(response, HttpStatus.BAD_REQUEST);
		}

		User owner = this.userService.getUserById(pet.getOwnerId());
		pet.setStatus(ActiveStatus.ACTIVE.value());
		pet.setUser(owner);
		Pet savedPet = this.petDao.save(pet);

		if (savedPet == null) {
			response.setResponseMessage("missing input");
			response.setResponseCode(ResponseCode.SUCCESS.value());

			return new ResponseEntity<CommanApiResponse>(response, HttpStatus.BAD_REQUEST);
		}

		response.setResponseMessage("Pet Added Successful");
		response.setResponseCode(ResponseCode.SUCCESS.value());

		return new ResponseEntity<CommanApiResponse>(response, HttpStatus.OK);

	}

	@PutMapping("/update")
	public ResponseEntity<CommanApiResponse> updatePet(@RequestBody Pet pet) {

		CommanApiResponse response = new CommanApiResponse();

		if (pet == null) {
			response.setResponseMessage("missing input");
			response.setResponseCode(ResponseCode.SUCCESS.value());

			return new ResponseEntity<CommanApiResponse>(response, HttpStatus.BAD_REQUEST);
		}

		if (pet.getId() == 0) {
			response.setResponseMessage("missing pet Id");
			response.setResponseCode(ResponseCode.SUCCESS.value());

			return new ResponseEntity<CommanApiResponse>(response, HttpStatus.BAD_REQUEST);
		}
		
		Pet existingPet = this.petDao.findById(pet.getId()).get();

		pet.setStatus(ActiveStatus.ACTIVE.value());
		pet.setOwnerId(existingPet.getOwnerId());
		Pet savedPet = this.petDao.save(pet);

		if (savedPet == null) {
			response.setResponseMessage("missing pet Id");
			response.setResponseCode(ResponseCode.SUCCESS.value());

			return new ResponseEntity<CommanApiResponse>(response, HttpStatus.BAD_REQUEST);
		}

		response.setResponseMessage("Pet Updated Successful");
		response.setResponseCode(ResponseCode.SUCCESS.value());

		return new ResponseEntity<CommanApiResponse>(response, HttpStatus.OK);

	}

	@GetMapping("/fetch/all")
	public ResponseEntity<PetResponse> fetchAllPet(@RequestParam("userId") Integer userId) {

		PetResponse response = new PetResponse();

		List<Pet> pets = new ArrayList<>();

		User user = this.userService.getUserById(userId);

		pets = this.petDao.findByUserAndStatus(user, ActiveStatus.ACTIVE.value());

		if (CollectionUtils.isEmpty(pets)) {
			response.setResponseMessage("No Categories found");
			response.setResponseCode(ResponseCode.SUCCESS.value());

			return new ResponseEntity<PetResponse>(response, HttpStatus.OK);
		}

		response.setPets(pets);
		response.setResponseMessage("Pet fetched successful");
		response.setResponseCode(ResponseCode.SUCCESS.value());

		return new ResponseEntity<PetResponse>(response, HttpStatus.OK);
	}

	@DeleteMapping("/delete")
	public ResponseEntity<CommanApiResponse> deletePet(@RequestParam("petId") int petId) {

		CommanApiResponse response = new CommanApiResponse();

		if (petId == 0) {
			response.setResponseMessage("missing location Id");
			response.setResponseCode(ResponseCode.FAILED.value());

			return new ResponseEntity<CommanApiResponse>(response, HttpStatus.BAD_REQUEST);
		}

		Pet pet = this.petDao.findById(petId).get();

		if (pet == null) {
			response.setResponseMessage("pet not found");
			response.setResponseCode(ResponseCode.FAILED.value());

			return new ResponseEntity<CommanApiResponse>(response, HttpStatus.BAD_REQUEST);
		}

		pet.setStatus(ActiveStatus.DEACTIVATED.value());
		Pet updatedPet = this.petDao.save(pet);

		if (updatedPet == null) {
			response.setResponseMessage("Failed to  delete the pet");
			response.setResponseCode(ResponseCode.FAILED.value());

			return new ResponseEntity<CommanApiResponse>(response, HttpStatus.BAD_REQUEST);
		}

		response.setResponseMessage("Pet Deleted Successful");
		response.setResponseCode(ResponseCode.SUCCESS.value());

		return new ResponseEntity<CommanApiResponse>(response, HttpStatus.OK);

	}

}
