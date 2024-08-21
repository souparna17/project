package com.petcure.controller;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.petcure.dao.PetDao;
import com.petcure.dto.AppointmentResponseDto;
import com.petcure.dto.CommanApiResponse;
import com.petcure.dto.UpdateAppointmentRequest;
import com.petcure.entity.Appointment;
import com.petcure.entity.Pet;
import com.petcure.entity.User;
import com.petcure.exception.AppointmentNotFoundException;
import com.petcure.service.AppointmentService;
import com.petcure.service.UserService;
import com.petcure.utility.Constants.AppointmentStatus;
import com.petcure.utility.Constants.ResponseCode;

import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("api/appointment/")
@CrossOrigin(origins = "http://localhost:3000")
public class AppointmentController {

	Logger LOG = LoggerFactory.getLogger(AppointmentController.class);

	@Autowired
	private AppointmentService appointmentService;

	@Autowired
	private UserService userService;

	@Autowired
	private PetDao petDao;

	@PostMapping("patient/add")
	@ApiOperation(value = "Api to add patient appointment")
	public ResponseEntity<?> addAppointment(@RequestBody Appointment appointment) {
		LOG.info("Recieved request to add patient appointment");

		CommanApiResponse response = new CommanApiResponse();

		if (appointment == null) {
			response.setResponseCode(ResponseCode.FAILED.value());
			response.setResponseMessage("Failed to add patient appointment");
			return new ResponseEntity(response, HttpStatus.BAD_REQUEST);
		}

		if (appointment.getPatientId() == 0 || appointment.getDoctorId() == 0 || appointment.getPetId() == 0) {
			response.setResponseCode(ResponseCode.FAILED.value());
			response.setResponseMessage("Failed to add patient appointment");
			return new ResponseEntity(response, HttpStatus.BAD_REQUEST);
		}

		Pet pet = this.petDao.findById(appointment.getPetId()).get();

		List<Appointment> appointments = this.appointmentService.findByDoctorIdAndAppointmentDateAndAppointmentTime(
				appointment.getDoctorId(), appointment.getAppointmentDate(), appointment.getAppointmentTime());

		if (!CollectionUtils.isEmpty(appointments)) {
			response.setResponseCode(ResponseCode.FAILED.value());
			response.setResponseMessage("Time Slot already booked, please check for other slot!!!!");
			return new ResponseEntity(response, HttpStatus.BAD_REQUEST);
		}

		appointment.setPet(pet);
		appointment.setDate(LocalDate.now().toString());

		appointment.setStatus(AppointmentStatus.BOOKED.value());

		Appointment addedAppointment = appointmentService.addAppointment(appointment);

		if (addedAppointment != null) {
			response.setResponseCode(ResponseCode.SUCCESS.value());
			response.setResponseMessage("Appointment Added");
			return new ResponseEntity(response, HttpStatus.OK);
		}

		else {
			response.setResponseCode(ResponseCode.SUCCESS.value());
			response.setResponseMessage("Failed to add Appointment");
			return new ResponseEntity(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	@GetMapping("all")
	public ResponseEntity<?> getAllAppointments() {
		LOG.info("recieved request for getting ALL Appointments !!!");

		List<Appointment> appointments = this.appointmentService.getAllAppointment();

		List<AppointmentResponseDto> response = new ArrayList();

		for (Appointment appointment : appointments) {

			AppointmentResponseDto a = new AppointmentResponseDto();

			User patient = this.userService.getUserById(appointment.getPatientId());

			a.setAppointmentTime(appointment.getAppointmentTime());
			a.setPet(appointment.getPet());
			a.setPatientContact(patient.getContact());
			a.setPatientId(patient.getId());
			a.setPatientName(patient.getFirstName() + " " + patient.getLastName());

			User doctor = this.userService.getUserById(appointment.getDoctorId());
			a.setDoctorContact(doctor.getContact());
			a.setDoctorName(doctor.getFirstName() + " " + doctor.getLastName());
			a.setDoctorId(doctor.getId());
			a.setPrescription(appointment.getPrescription());

			a.setPrice(appointment.getPrice() != 0 ? String.valueOf(appointment.getPrice())
					: AppointmentStatus.TREATMENT_PENDING.value());
			a.setPrescription(appointment.getPrescription() != null ? String.valueOf(appointment.getPrescription())
					: AppointmentStatus.TREATMENT_PENDING.value());

			a.setStatus(appointment.getStatus());
			a.setProblem(appointment.getProblem());
			a.setDate(appointment.getDate());
			a.setAppointmentDate(appointment.getAppointmentDate());
			a.setId(appointment.getId());

			response.add(a);
		}

		LOG.info("response sent!!!");
		return ResponseEntity.ok(response);
	}

	@GetMapping("id")
	public ResponseEntity<?> getAllAppointments(@RequestParam("appointmentId") int appointmentId) {
		LOG.info("recieved request for getting  Appointment by id !!!");

		Appointment appointment = this.appointmentService.getAppointmentById(appointmentId);

		if (appointment == null) {
			throw new AppointmentNotFoundException();
		}

		AppointmentResponseDto a = new AppointmentResponseDto();

		User patient = this.userService.getUserById(appointment.getPatientId());
		a.setPet(appointment.getPet());
		a.setAppointmentTime(appointment.getAppointmentTime());

		a.setPatientContact(patient.getContact());
		a.setPatientId(patient.getId());
		a.setPatientName(patient.getFirstName() + " " + patient.getLastName());

		User doctor = this.userService.getUserById(appointment.getDoctorId());
		a.setDoctorContact(doctor.getContact());
		a.setDoctorName(doctor.getFirstName() + " " + doctor.getLastName());
		a.setDoctorId(doctor.getId());
		a.setPrescription(appointment.getPrescription());

		a.setPrice(appointment.getPrice() != 0 ? String.valueOf(appointment.getPrice())
				: AppointmentStatus.TREATMENT_PENDING.value());
		a.setPrescription(appointment.getPrescription() != null ? String.valueOf(appointment.getPrescription())
				: AppointmentStatus.TREATMENT_PENDING.value());

		a.setStatus(appointment.getStatus());
		a.setProblem(appointment.getProblem());
		a.setDate(appointment.getDate());
		a.setAppointmentDate(appointment.getAppointmentDate());
		a.setId(appointment.getId());

		LOG.info("response sent!!!");
		return ResponseEntity.ok(a);
	}

	@GetMapping("patient/id")
	public ResponseEntity<?> getAllAppointmentsByPatientId(@RequestParam("patientId") int patientId) {
		LOG.info("recieved request for getting ALL Appointments by patient Id !!!");

		List<Appointment> appointments = this.appointmentService.getAppointmentByPatientId(patientId);

		List<AppointmentResponseDto> response = new ArrayList();

		for (Appointment appointment : appointments) {

			AppointmentResponseDto a = new AppointmentResponseDto();

			User patient = this.userService.getUserById(appointment.getPatientId());
			a.setPet(appointment.getPet());
			a.setAppointmentTime(appointment.getAppointmentTime());

			a.setPatientContact(patient.getContact());
			a.setPatientId(patient.getId());
			a.setPatientName(patient.getFirstName() + " " + patient.getLastName());

			User doctor = this.userService.getUserById(appointment.getDoctorId());
			a.setDoctorContact(doctor.getContact());
			a.setDoctorName(doctor.getFirstName() + " " + doctor.getLastName());
			a.setDoctorId(doctor.getId());
			a.setPrescription(appointment.getPrescription());

			a.setPrice(appointment.getPrice() != 0 ? String.valueOf(appointment.getPrice())
					: AppointmentStatus.TREATMENT_PENDING.value());
			a.setPrescription(appointment.getPrescription() != null ? String.valueOf(appointment.getPrescription())
					: AppointmentStatus.TREATMENT_PENDING.value());

			a.setStatus(appointment.getStatus());
			a.setProblem(appointment.getProblem());
			a.setDate(appointment.getDate());
			a.setAppointmentDate(appointment.getAppointmentDate());
			a.setId(appointment.getId());

			response.add(a);

		}

		LOG.info("response sent!!!");
		return ResponseEntity.ok(response);
	}

	@GetMapping("doctor/id")
	public ResponseEntity<?> getAllAppointmentsByDoctorId(@RequestParam("doctorId") int doctorId) {
		LOG.info("recieved request for getting ALL Appointments by doctor Id !!!");

		List<Appointment> appointments = this.appointmentService.getAppointmentByDoctorId(doctorId);

		List<AppointmentResponseDto> response = new ArrayList();

		for (Appointment appointment : appointments) {

			AppointmentResponseDto a = new AppointmentResponseDto();

			User patient = this.userService.getUserById(appointment.getPatientId());
			a.setPet(appointment.getPet());
			a.setAppointmentTime(appointment.getAppointmentTime());

			a.setPatientContact(patient.getContact());
			a.setPatientId(patient.getId());
			a.setPatientName(patient.getFirstName() + " " + patient.getLastName());

			User doctor = this.userService.getUserById(appointment.getDoctorId());
			a.setDoctorContact(doctor.getContact());
			a.setDoctorName(doctor.getFirstName() + " " + doctor.getLastName());
			a.setDoctorId(doctor.getId());
			a.setPrescription(appointment.getPrescription());

			a.setPrice(appointment.getPrice() != 0 ? String.valueOf(appointment.getPrice())
					: AppointmentStatus.TREATMENT_PENDING.value());
			a.setPrescription(appointment.getPrescription() != null ? String.valueOf(appointment.getPrescription())
					: AppointmentStatus.TREATMENT_PENDING.value());

			a.setStatus(appointment.getStatus());
			a.setProblem(appointment.getProblem());
			a.setDate(appointment.getDate());
			a.setAppointmentDate(appointment.getAppointmentDate());
			a.setId(appointment.getId());

			response.add(a);

		}

		LOG.info("response sent!!!");
		return ResponseEntity.ok(response);
	}

	@PostMapping("doctor/update")
	@ApiOperation(value = "Api to assign appointment to doctor")
	public ResponseEntity<?> assignAppointmentToDoctor(UpdateAppointmentRequest request) {
		LOG.info("Recieved request to update appointment");

		CommanApiResponse response = new CommanApiResponse();

		if (request == null) {
			response.setResponseCode(ResponseCode.FAILED.value());
			response.setResponseMessage("Failed to assign appointment");
			return new ResponseEntity(response, HttpStatus.BAD_REQUEST);
		}

		if (request.getAppointmentId() == 0) {
			response.setResponseCode(ResponseCode.FAILED.value());
			response.setResponseMessage("Appointment not found");
			return new ResponseEntity(response, HttpStatus.BAD_REQUEST);
		}

		Appointment appointment = appointmentService.getAppointmentById(request.getAppointmentId());

		if (appointment == null) {
			throw new AppointmentNotFoundException();
		}

		appointment.setPrescription(request.getPrescription());
		appointment.setStatus(request.getStatus());

		if (request.getStatus().equals(AppointmentStatus.TREATMENT_DONE.value())) {
			appointment.setPrice(request.getPrice());
			

			User owner = this.userService.getUserById(appointment.getPatientId());

			if (owner.getWalletAmount().compareTo(new BigDecimal(appointment.getPrice())) < 0) {
				response.setResponseCode(ResponseCode.FAILED.value());
				response.setResponseMessage("Insufficent Funds in Pet Owner Wallet");
				return new ResponseEntity(response, HttpStatus.BAD_REQUEST);
			}

			User doctor = this.userService.getUserById(appointment.getDoctorId());
			doctor.setWalletAmount(doctor.getWalletAmount().add(new BigDecimal(request.getPrice())));
			owner.setWalletAmount(owner.getWalletAmount().subtract(new BigDecimal(request.getPrice())));
			this.userService.updateUser(doctor);
			this.userService.updateUser(owner);
		}

		Appointment updatedAppointment = this.appointmentService.addAppointment(appointment);

		if (updatedAppointment != null) {
			response.setResponseCode(ResponseCode.SUCCESS.value());
			response.setResponseMessage("Updated Treatment Status");
			return new ResponseEntity(response, HttpStatus.OK);
		}

		else {
			response.setResponseCode(ResponseCode.FAILED.value());
			response.setResponseMessage("Failed to update");
			return new ResponseEntity(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	@PostMapping("patient/update")
	@ApiOperation(value = "Api to update appointment patient")
	public ResponseEntity<?> udpateAppointmentStatus(@RequestBody UpdateAppointmentRequest request) {
		LOG.info("Recieved request to update appointment");

		CommanApiResponse response = new CommanApiResponse();

		if (request == null) {
			response.setResponseCode(ResponseCode.FAILED.value());
			response.setResponseMessage("Failed to assign appointment");
			return new ResponseEntity(response, HttpStatus.BAD_REQUEST);
		}

		if (request.getAppointmentId() == 0) {
			response.setResponseCode(ResponseCode.FAILED.value());
			response.setResponseMessage("Appointment not found");
			return new ResponseEntity(response, HttpStatus.BAD_REQUEST);
		}

		Appointment appointment = appointmentService.getAppointmentById(request.getAppointmentId());

		if (appointment == null) {
			throw new AppointmentNotFoundException();
		}

		appointment.setStatus(request.getStatus());

		Appointment updatedAppointment = this.appointmentService.addAppointment(appointment);

		if (updatedAppointment != null) {
			response.setResponseCode(ResponseCode.SUCCESS.value());
			response.setResponseMessage("Updated Treatment Status");
			return new ResponseEntity(response, HttpStatus.OK);
		}

		else {
			response.setResponseCode(ResponseCode.FAILED.value());
			response.setResponseMessage("Failed to update");
			return new ResponseEntity(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

}
