package com.petcure.controller;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.petcure.dto.CommanApiResponse;
import com.petcure.dto.DoctorRegisterDto;
import com.petcure.entity.User;
import com.petcure.service.UserService;
import com.petcure.utility.Constants.DoctorSpecialist;
import com.petcure.utility.Constants.ResponseCode;
import com.petcure.utility.Constants.UserRole;
import com.petcure.utility.Constants.UserStatus;
import com.petcure.utility.StorageService;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("api/doctor/")
@CrossOrigin(origins = "http://localhost:3000")
public class DoctorController {

	Logger LOG = LoggerFactory.getLogger(DoctorController.class);

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private UserService userService;

	@Autowired
	private StorageService storageService;
	
	@Autowired
	JavaMailSender jms;

	@PostMapping("register")
	@ApiOperation(value = "Api to register doctor")
	public ResponseEntity<?> registerDoctor(DoctorRegisterDto doctorRegisterDto) {
		LOG.info("Recieved request for doctor register");

		CommanApiResponse response = new CommanApiResponse();

		User user = DoctorRegisterDto.toEntity(doctorRegisterDto);
		
//		Email Details
		Date day = new Date();
		String details = "Password for your account\n" +
                "Username: " + user.getEmailId() + "\n" +
                "Password: " + user.getPassword() + "\n\n" +
                "Registration Successful on " + day;


		String image = storageService.store(doctorRegisterDto.getImage());

		user.setDoctorImage(image);

		String encodedPassword = passwordEncoder.encode(user.getPassword());

		user.setPassword(encodedPassword);
		user.setStatus(UserStatus.ACTIVE.value());
		user.setWalletAmount(BigDecimal.ZERO);

		User registerUser = userService.registerUser(user);

		if (registerUser != null) {
			
			SimpleMailMessage smm = new SimpleMailMessage();
			smm.setFrom("adityavyavahare54@gmail.com");
			smm.setTo(user.getEmailId());
			System.out.println("--**$$" + user.getEmailId());
			smm.setSubject("Registration Mail");

			
			
			System.out.println(details);

			// Set the combined details as the email content
			smm.setText(details);

			// Send the email
			jms.send(smm);
			
			response.setResponseCode(ResponseCode.SUCCESS.value());
			response.setResponseMessage(user.getRole() + " Doctor Registered Successfully");
			return new ResponseEntity(response, HttpStatus.OK);
		}

		else {
			response.setResponseCode(ResponseCode.FAILED.value());
			response.setResponseMessage("Failed to Register Doctor");
			return new ResponseEntity(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("all")
	public ResponseEntity<?> getAllDoctor() {
		LOG.info("recieved request for getting ALL Doctor!!!");

		List<User> doctors = this.userService.getAllUserByRole(UserRole.DOCTOR.value());

		LOG.info("response sent!!!");
		return ResponseEntity.ok(doctors);
	}

	@GetMapping("location-wise")
	public ResponseEntity<?> getAllDoctorByLocation(@RequestParam("locationId") Integer locationId) {
		LOG.info("recieved request for getting ALL Doctor using location!!!");

		List<User> doctors = this.userService.getAllUserByLocation(UserRole.DOCTOR.value(), locationId,
				UserStatus.ACTIVE.value());

		LOG.info("response sent!!!");
		return ResponseEntity.ok(doctors);
	}

	@GetMapping(value = "/{doctorImageName}", produces = "image/*")
	@ApiOperation(value = "Api to fetch doctor image by using image name")
	public void fetchProductImage(@PathVariable("doctorImageName") String doctorImageName, HttpServletResponse resp) {
		LOG.info("request came for fetching doctor pic");
		LOG.info("Loading file: " + doctorImageName);
		Resource resource = storageService.load(doctorImageName);
		if (resource != null) {
			try (InputStream in = resource.getInputStream()) {
				ServletOutputStream out = resp.getOutputStream();
				FileCopyUtils.copy(in, out);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		LOG.info("response sent!");
	}

	@GetMapping("/specialist/all")
	public ResponseEntity<?> getAllSpecialist() {

		LOG.info("Received the request for getting as Specialist");

		List<String> specialists = new ArrayList<>();

		for (DoctorSpecialist s : DoctorSpecialist.values()) {
			specialists.add(s.value());
		}

		LOG.info("Response sent!!!");

		return new ResponseEntity(specialists, HttpStatus.OK);
	}

}
