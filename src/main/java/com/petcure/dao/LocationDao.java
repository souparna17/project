package com.petcure.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.petcure.entity.Location;

@Repository
public interface LocationDao extends JpaRepository<Location, Integer> {

	List<Location> findByStatus(String status);

}
