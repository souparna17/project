package com.petcure.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.petcure.entity.Pet;
import com.petcure.entity.User;

@Repository
public interface PetDao extends JpaRepository<Pet, Integer> {

	List<Pet> findByUser(User user);

	List<Pet> findByUserAndStatus(User user, String value);
	
}
