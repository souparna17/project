package com.petcure.dto;

import java.util.ArrayList;
import java.util.List;

import com.petcure.entity.Pet;

public class PetResponse extends CommanApiResponse {

	private List<Pet> pets = new ArrayList<>();

	public List<Pet> getPets() {
		return pets;
	}

	public void setPets(List<Pet> pets) {
		this.pets = pets;
	}

}
