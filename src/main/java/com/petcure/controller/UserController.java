package com.petcure.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.petcure.dao.PgTransactionDao;
import com.petcure.dto.CommanApiResponse;
import com.petcure.dto.UserLoginRequest;
import com.petcure.dto.UserLoginResponse;
import com.petcure.dto.UserRoleResponse;
import com.petcure.dto.UserWalletUpdateResponse;
import com.petcure.dto.UsersResponseDto;
import com.petcure.entity.PgTransaction;
import com.petcure.entity.User;
import com.petcure.exception.UserNotFoundException;
import com.petcure.pg.Notes;
import com.petcure.pg.Prefill;
import com.petcure.pg.RazorPayPaymentRequest;
import com.petcure.pg.RazorPayPaymentResponse;
import com.petcure.pg.Theme;
import com.petcure.service.CustomUserDetailsService;
import com.petcure.service.UserService;
import com.petcure.utility.Constants.PaymentGatewayTxnStatus;
import com.petcure.utility.Constants.PaymentGatewayTxnType;
import com.petcure.utility.Constants.ResponseCode;
import com.petcure.utility.Constants.Sex;
import com.petcure.utility.Constants.UserRole;
import com.petcure.utility.Constants.UserStatus;
import com.petcure.utility.JwtUtil;
import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;

import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("api/user/")
@CrossOrigin(origins = "http://localhost:3000")
public class UserController {

	Logger LOG = LoggerFactory.getLogger(UserController.class);

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private UserService userService;

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private CustomUserDetailsService customUserDetailsService;

	@Autowired
	private JwtUtil jwtUtil;

	@Autowired
	private PgTransactionDao pgTransactionDao;

	@Value("${com.petcure.paymentGateway.razorpay.key}")
	private String razorPayKey;

	@Value("${com.petcure.paymentGateway.razorpay.secret}")
	private String razorPaySecret;

	@Autowired
	private ObjectMapper objectMapper;

	@GetMapping("roles")
	@ApiOperation(value = "Api to get all user roles")
	public ResponseEntity<?> getAllUsers() {

		UserRoleResponse response = new UserRoleResponse();
		List<String> roles = new ArrayList<>();

		for (UserRole role : UserRole.values()) {
			roles.add(role.value());
		}

		if (roles.isEmpty()) {
			response.setResponseCode(ResponseCode.FAILED.value());
			response.setResponseMessage("Failed to Fetch User Roles");
			return new ResponseEntity(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		else {
			response.setRoles(roles);
			response.setResponseCode(ResponseCode.SUCCESS.value());
			response.setResponseMessage("User Roles Fetched success");
			return new ResponseEntity(response, HttpStatus.OK);
		}

	}

	@GetMapping("gender")
	@ApiOperation(value = "Api to get all user gender")
	public ResponseEntity<?> getAllUserGender() {

		UserRoleResponse response = new UserRoleResponse();
		List<String> genders = new ArrayList<>();

		for (Sex gender : Sex.values()) {
			genders.add(gender.value());
		}

		if (genders.isEmpty()) {
			response.setResponseCode(ResponseCode.FAILED.value());
			response.setResponseMessage("Failed to Fetch User Genders");
			return new ResponseEntity(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		else {
			response.setGenders(genders);
			response.setResponseCode(ResponseCode.SUCCESS.value());
			response.setResponseMessage("User Genders Fetched success");
			return new ResponseEntity(response, HttpStatus.OK);
		}

	}

	@PostMapping("register")
	@ApiOperation(value = "Api to register any User")
	public ResponseEntity<?> register(@RequestBody User user) {
		LOG.info("Recieved request for User  register");

		CommanApiResponse response = new CommanApiResponse();
		String encodedPassword = passwordEncoder.encode(user.getPassword());

		user.setPassword(encodedPassword);
		user.setStatus(UserStatus.ACTIVE.value());
		user.setWalletAmount(BigDecimal.ZERO);

		User registerUser = userService.registerUser(user);

		if (registerUser != null) {
			response.setResponseCode(ResponseCode.SUCCESS.value());
			response.setResponseMessage(user.getRole() + " User Registered Successfully");
			return new ResponseEntity(response, HttpStatus.OK);
		}

		else {
			response.setResponseCode(ResponseCode.FAILED.value());
			response.setResponseMessage("Failed to Register " + user.getRole() + " User");
			return new ResponseEntity(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PostMapping("login")
	@ApiOperation(value = "Api to login any User")
	public ResponseEntity<?> login(@RequestBody UserLoginRequest userLoginRequest) {
		LOG.info("Recieved request for User Login");

		String jwtToken = null;
		UserLoginResponse useLoginResponse = new UserLoginResponse();
		User user = null;
		try {
			authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userLoginRequest.getEmailId(),
					userLoginRequest.getPassword()));
		} catch (Exception ex) {
			LOG.error("Autthentication Failed!!!");
			useLoginResponse.setResponseCode(ResponseCode.FAILED.value());
			useLoginResponse.setResponseMessage("Failed to Login as " + userLoginRequest.getEmailId());
			return new ResponseEntity(useLoginResponse, HttpStatus.BAD_REQUEST);
		}

		UserDetails userDetails = customUserDetailsService.loadUserByUsername(userLoginRequest.getEmailId());

		user = userService.getUserByEmailId(userLoginRequest.getEmailId());

		if (user.getStatus() != UserStatus.ACTIVE.value()) {
			useLoginResponse.setResponseCode(ResponseCode.FAILED.value());
			useLoginResponse.setResponseMessage("User is Inactive");
			return new ResponseEntity(useLoginResponse, HttpStatus.BAD_REQUEST);
		}

		for (GrantedAuthority grantedAuthory : userDetails.getAuthorities()) {
			if (grantedAuthory.getAuthority().equals(userLoginRequest.getRole())) {
				jwtToken = jwtUtil.generateToken(userDetails.getUsername());
			}
		}

		// user is authenticated
		if (jwtToken != null) {
			useLoginResponse = User.toUserLoginResponse(user);

			useLoginResponse.setResponseCode(ResponseCode.SUCCESS.value());
			useLoginResponse.setResponseMessage(user.getFirstName() + " logged in Successful");
			useLoginResponse.setJwtToken(jwtToken);
			return new ResponseEntity(useLoginResponse, HttpStatus.OK);

		}

		else {

			useLoginResponse.setResponseCode(ResponseCode.FAILED.value());
			useLoginResponse.setResponseMessage("Failed to Login as " + userLoginRequest.getEmailId());
			return new ResponseEntity(useLoginResponse, HttpStatus.BAD_REQUEST);
		}
	}

	@GetMapping("id")
	@ApiOperation(value = "Api to fetch the User using user Id")
	public ResponseEntity<?> fetchUser(@RequestParam("userId") int userId) {

		UsersResponseDto response = new UsersResponseDto();

		User user = userService.getUserById(userId);

		if (user == null) {
			throw new UserNotFoundException();
		}

		response.setUser(user);
		response.setResponseCode(ResponseCode.SUCCESS.value());
		response.setResponseMessage("User Fetched Successfully");

		return new ResponseEntity(response, HttpStatus.OK);
	}

	@GetMapping("delete/id")
	@ApiOperation(value = "Api to delete user by using user id")
	public ResponseEntity<?> deleteUser(@RequestParam("userId") int userId) {

		System.out.println("request came for USER DELETE By ID");

		CommanApiResponse response = new CommanApiResponse();

		User user = null;
		user = userService.getUserById(userId);

		if (user == null) {
			throw new UserNotFoundException();
		}

		user.setStatus(UserStatus.DELETED.value());

		userService.registerUser(user);

		response.setResponseCode(ResponseCode.SUCCESS.value());
		response.setResponseMessage("User Deleted Successfully");

		return new ResponseEntity(response, HttpStatus.OK);
	}

	@PutMapping("update/wallet")
	public ResponseEntity<UserWalletUpdateResponse> createRazorPayOrder(@RequestBody User user)
			throws RazorpayException {

		UserWalletUpdateResponse response = new UserWalletUpdateResponse();

		if (user == null) {
			response.setResponseMessage("Invalid Input");
			response.setResponseCode(ResponseCode.FAILED.value());
			return new ResponseEntity<UserWalletUpdateResponse>(response, HttpStatus.BAD_REQUEST);
		}

		if (user.getId() == 0 || user.getWalletAmount() == null
				|| user.getWalletAmount().compareTo(BigDecimal.ZERO) <= 0) {
			response.setResponseMessage("No Users Found");
			response.setResponseCode(ResponseCode.FAILED.value());
			return new ResponseEntity<UserWalletUpdateResponse>(response, HttpStatus.BAD_REQUEST);
		}

		User customer = this.userService.getUserById(user.getId());

		if (customer == null || !customer.getRole().equals(UserRole.PET_OWNER.value())) {
			response.setResponseMessage("Customer Not Found");
			response.setResponseCode(ResponseCode.FAILED.value());
			return new ResponseEntity<UserWalletUpdateResponse>(response, HttpStatus.BAD_REQUEST);
		}

		BigDecimal existingWalletAmount = customer.getWalletAmount();

		// write payment gateway code here

		// key : rzp_test_9C5DF9gbJINYTA
		// secret: WYqJeY6CJD1iw7cDZFv1eWl0

		String receiptId = generateUniqueRefId();

		RazorpayClient razorpay = new RazorpayClient(razorPayKey, razorPaySecret);

		JSONObject orderRequest = new JSONObject();
		orderRequest.put("amount", convertRupeesToPaisa(user.getWalletAmount()));
		orderRequest.put("currency", "INR");
		orderRequest.put("receipt", receiptId);
		JSONObject notes = new JSONObject();
		notes.put("note", "Credit in Wallet - Pet Cure");
		orderRequest.put("notes", notes);

		Order order = razorpay.orders.create(orderRequest);

		if (order == null) {
			LOG.error("Null Response from RazorPay for creation of");
			response.setResponseMessage("Failed to update the Wallet");
			response.setResponseCode(ResponseCode.FAILED.value());
			return new ResponseEntity<UserWalletUpdateResponse>(response, HttpStatus.BAD_REQUEST);
		}

		LOG.info(order.toString()); // printing the response which we got from RazorPay

		String orderId = order.get("id");

		PgTransaction createOrder = new PgTransaction();
		createOrder.setAmount(user.getWalletAmount());
		createOrder.setReceiptId(receiptId);
		createOrder.setRequestTime(receiptId);
		createOrder.setType(PaymentGatewayTxnType.CREATE_ORDER.value());
		createOrder.setUser(customer);
		createOrder.setOrderId(orderId); // fetching order id which is created at Razor Pay which we got in response

		if (order.get("status").equals("created")) {
			createOrder.setStatus(PaymentGatewayTxnStatus.SUCCESS.value());
		} else {
			createOrder.setStatus(PaymentGatewayTxnStatus.FAILED.value());
		}

		PgTransaction saveCreateOrderTxn = pgTransactionDao.save(createOrder);

		if (saveCreateOrderTxn == null) {
			LOG.error("Failed to save Payment Gateway CReate Order entry in DB");
		}

		PgTransaction payment = new PgTransaction();
		payment.setAmount(user.getWalletAmount());
		payment.setReceiptId(receiptId);
		payment.setRequestTime(receiptId);
		payment.setType(PaymentGatewayTxnType.PAYMENT.value());
		payment.setUser(customer);
		payment.setOrderId(orderId); // fetching order id which is created at Razor Pay which we got in response
		payment.setStatus(PaymentGatewayTxnStatus.FAILED.value());
		// from callback api we will actual response from RazorPay, initially keeping it
		// FAILED, once get success response from PG,
		// we will update it

		PgTransaction savePaymentTxn = this.pgTransactionDao.save(payment);

		if (savePaymentTxn == null) {
			LOG.error("Failed to save Payment Gateway Payment entry in DB");
		}

		// Creating RazorPayPaymentRequest to send to Frontend

		RazorPayPaymentRequest razorPayPaymentRequest = new RazorPayPaymentRequest();
		razorPayPaymentRequest.setAmount(convertRupeesToPaisa(user.getWalletAmount()));
		// razorPayPaymentRequest.setCallbackUrl("http://localhost:8080/pg/razorPay/callBack/response");
		razorPayPaymentRequest.setCurrency("INR");
		razorPayPaymentRequest.setDescription("Credit in Wallet - Pet Cure");
		razorPayPaymentRequest.setImage(
				"https://png.pngtree.com/png-vector/20211023/ourmid/pngtree-pet-care-logo-design-png-image_4002854.png");
		razorPayPaymentRequest.setKey(razorPayKey);
		razorPayPaymentRequest.setName("Pet Cure");

		Notes note = new Notes();
		note.setAddress("Dummy Address");

		razorPayPaymentRequest.setNotes(note);
		razorPayPaymentRequest.setOrderId(orderId);

		Prefill prefill = new Prefill();
		prefill.setContact(customer.getContact());
		prefill.setEmail(customer.getEmailId());
		prefill.setName(customer.getFirstName() + " " + customer.getLastName());

		razorPayPaymentRequest.setPrefill(prefill);

		Theme theme = new Theme();
		theme.setColor("#8A001D");

		razorPayPaymentRequest.setTheme(theme);

		try {
			String jsonRequest = objectMapper.writeValueAsString(razorPayPaymentRequest);
			System.out.println("*****************");
			System.out.println(jsonRequest);
			System.out.println("*****************");
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

//		customer.setWalletAmount(existingWalletAmount.add(request.getWalletAmount()));
//
//		User updatedCustomer = this.userService.updateUser(customer);
//
//		if (updatedCustomer == null) {
//			response.setResponseMessage("Failed to update the Wallet");
//			response.setSuccess(false);
//			return new ResponseEntity<UserWalletUpdateResponse>(response, HttpStatus.BAD_REQUEST);
//		}

		response.setRazorPayRequest(razorPayPaymentRequest);
		response.setResponseMessage("Payment Order Created Successful!!!");
		response.setResponseCode(ResponseCode.SUCCESS.value());

		return new ResponseEntity<UserWalletUpdateResponse>(response, HttpStatus.OK);
	}

	private int convertRupeesToPaisa(BigDecimal rupees) {
		// Multiply the rupees by 100 to get the equivalent in paisa
		BigDecimal paisa = rupees.multiply(new BigDecimal(100));
		return paisa.intValue();
	}

	// for razor pay receipt id
	private String generateUniqueRefId() {
		// Get current timestamp in milliseconds
		long currentTimeMillis = System.currentTimeMillis();

		// Generate a 6-digit UUID (random number)
		String randomDigits = UUID.randomUUID().toString().substring(0, 6);

		// Concatenate timestamp and random digits
		String uniqueRefId = currentTimeMillis + "-" + randomDigits;

		return uniqueRefId;
	}

	@PutMapping("razorpPay/response")
	public ResponseEntity<CommanApiResponse> updateUserWallet(@RequestBody RazorPayPaymentResponse razorPayResponse)
			throws RazorpayException {

		LOG.info("razor pay response came from frontend");

		CommanApiResponse response = new CommanApiResponse();

		if (razorPayResponse == null || razorPayResponse.getRazorpayOrderId() == null) {
			response.setResponseMessage("Invalid Input response");
			response.setResponseCode(ResponseCode.SUCCESS.value());
			return new ResponseEntity<CommanApiResponse>(response, HttpStatus.BAD_REQUEST);
		}

		PgTransaction paymentTransaction = this.pgTransactionDao
				.findByTypeAndOrderId(PaymentGatewayTxnType.PAYMENT.value(), razorPayResponse.getRazorpayOrderId());

		User customer = paymentTransaction.getUser();
		BigDecimal existingBalance = customer.getWalletAmount();

		BigDecimal walletBalanceToAdd = paymentTransaction.getAmount();

		String razorPayRawResponse = "";
		try {
			razorPayRawResponse = objectMapper.writeValueAsString(razorPayResponse);
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		paymentTransaction.setRawResponse(razorPayRawResponse);

		if (razorPayResponse.getError() == null) {
			paymentTransaction.setStatus(PaymentGatewayTxnStatus.SUCCESS.value());

			customer.setWalletAmount(existingBalance.add(walletBalanceToAdd));

			User updatedCustomer = this.userService.updateUser(customer);

			if (updatedCustomer == null) {
				LOG.error("Failed to update the wallet for order id: " + razorPayResponse.getRazorpayOrderId());
			} else {
				LOG.info("Wallet Updated Successful");
			}

		} else {
			paymentTransaction.setStatus(PaymentGatewayTxnStatus.FAILED.value());
		}

		PgTransaction updatedTransaction = this.pgTransactionDao.save(paymentTransaction);

		if (updatedTransaction.getStatus().equals(PaymentGatewayTxnStatus.FAILED.value())) {
			response.setResponseMessage("Failed to update the User Wallet");
			response.setResponseCode(ResponseCode.SUCCESS.value());

			return new ResponseEntity<CommanApiResponse>(response, HttpStatus.OK);
		} else {
			response.setResponseMessage("User Wallet Updated Successful");
			response.setResponseCode(ResponseCode.SUCCESS.value());

			return new ResponseEntity<CommanApiResponse>(response, HttpStatus.OK);
		}

	}

	@PostMapping("profile/update")
	@ApiOperation(value = "Api to update the profile")
	public ResponseEntity<?> profileUpdate(@RequestBody User request) {

		CommanApiResponse response = new CommanApiResponse();

		if (request == null || request.getId() == 0) {
			response.setResponseCode(ResponseCode.SUCCESS.value());
			response.setResponseMessage("Failed to update the User profile");
			return new ResponseEntity(response, HttpStatus.OK);
		}

		User existingUser = this.userService.getUserById(request.getId());

		existingUser.setAge(request.getAge());
		existingUser.setFirstName(request.getFirstName());
		existingUser.setLastName(request.getLastName());
		existingUser.setCity(request.getCity());
		existingUser.setContact(request.getContact());
		existingUser.setStreet(request.getStreet());
		existingUser.setPincode(request.getPincode());

		User registerUser = userService.updateUser(existingUser);

		if (registerUser != null) {
			response.setResponseCode(ResponseCode.SUCCESS.value());
			response.setResponseMessage("Profile Updated Successful!!!");
			return new ResponseEntity(response, HttpStatus.OK);
		}

		else {
			response.setResponseCode(ResponseCode.FAILED.value());
			response.setResponseMessage("Failed to update the profile!!!");
			return new ResponseEntity(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

}
