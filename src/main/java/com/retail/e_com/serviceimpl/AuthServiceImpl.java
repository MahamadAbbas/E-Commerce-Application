package com.retail.e_com.serviceimpl;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Random;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.retail.e_com.cache.CacheStore;
import com.retail.e_com.enums.UserRole;
import com.retail.e_com.exception.AuthenticationException;
import com.retail.e_com.exception.OtpExpiredException;
import com.retail.e_com.exception.OtpIncorrectException;
import com.retail.e_com.exception.RegistrationSessionExpired;
import com.retail.e_com.exception.UserAlreadyExistByEmailException;
import com.retail.e_com.exception.UserIsNotLoginException;
import com.retail.e_com.jwt.io.JwtService;
import com.retail.e_com.mail_service.MailService;
import com.retail.e_com.model.AccessToken;
import com.retail.e_com.model.Customer;
import com.retail.e_com.model.RefreshToken;
import com.retail.e_com.model.Seller;
import com.retail.e_com.model.User;
import com.retail.e_com.repository.AccessTokenRepo;
import com.retail.e_com.repository.RefreshTokenRepo;
import com.retail.e_com.repository.UserRepo;
import com.retail.e_com.request_dto.AuthRequest;
import com.retail.e_com.request_dto.OtpRequest;
import com.retail.e_com.request_dto.UserRequest;
import com.retail.e_com.response.dto.AuthResponse;
import com.retail.e_com.response.dto.userResponse;
import com.retail.e_com.service.AuthService;
import com.retail.e_com.utility.MessageModel;
import com.retail.e_com.utility.ResponseStructure;
import com.retail.e_com.utility.SimpleResponseStructure;

import jakarta.mail.MessagingException;

@Service
public class AuthServiceImpl implements AuthService {

	private UserRepo userRepo;
	private CacheStore<String> otpCache;
	private CacheStore<User> userCache;
	private ResponseStructure<userResponse> structure;
	private SimpleResponseStructure simpleResponseStructure;
	private MailService mailService;
	private AuthenticationManager authenticationManager;
	private JwtService jwtService;
	private PasswordEncoder passwordEncoder;
	private AccessTokenRepo accessTokenRepo;
	private RefreshTokenRepo refreshTokenRepo;
	private ResponseStructure<AuthResponse> authResponseStucture;

	@Value("${myapp.jwt.access.expiration}")
	private long accessExpiration;

	@Value("${myapp.jwt.refresh.expiration}")
	private long refreshExpiration;

	public AuthServiceImpl(UserRepo userRepo, CacheStore<String> otpCache, CacheStore<User> userCache,
			ResponseStructure<userResponse> structure, SimpleResponseStructure simpleResponseStructure,
			MailService mailService, AuthenticationManager authenticationManager, JwtService jwtService,
			PasswordEncoder passwordEncoder, AccessTokenRepo accessTokenRepo, RefreshTokenRepo refreshTokenRepo,
			ResponseStructure<AuthResponse> authResponseStucture) {
		super();
		this.userRepo = userRepo;
		this.otpCache = otpCache;
		this.userCache = userCache;
		this.structure = structure;
		this.simpleResponseStructure = simpleResponseStructure;
		this.mailService = mailService;
		this.authenticationManager = authenticationManager;
		this.jwtService = jwtService;
		this.passwordEncoder = passwordEncoder;
		this.accessTokenRepo = accessTokenRepo;
		this.refreshTokenRepo = refreshTokenRepo;
		this.authResponseStucture = authResponseStucture;
	}

	@Override
	public ResponseEntity<SimpleResponseStructure> registerUser(UserRequest userRequest) {
		if(userRepo.existsByEmail(userRequest.getEmail()))throw new UserAlreadyExistByEmailException("Invalid Email");
		User user = mapToChildEntity(userRequest);
		String otp = generateOTP();

		otpCache.add(user.getEmail(), otp);
		userCache.add(user.getEmail(), user);

		System.err.println(otp);

		//send mail with OTP
		try {
			sendOTP(user, otp);
		} catch (MessagingException e) {
			// TODO custom Exception
		}
		//return user data

		return ResponseEntity.ok(simpleResponseStructure.setStatus(HttpStatus.ACCEPTED.value())
				.setMessage("Verify OTP sent through mail to complete registration | "
						+ "OTP expires in 1 minute"));
	}

	private void sendOTP(User user, String otp) throws MessagingException {
		MessageModel model = MessageModel.builder()
				.to(user.getEmail())
				.subject("Verify your OTP")
				.text(
						"<p> Hi, <br>"
								+"Thanks for your intrest in E-Com,"
								+"please verify your mail Id using the OTP given below.</p>"
								+"<br>"
								+"<h1>"+otp+"</h1>"
								+"<br>"
								+"<p>Please ignore if its not you</p>"
								+"<br>"
								+"with best regards"
								+"<h3>E-Com</h3>"
						)
				.build();

		mailService.sendMailMessage(model);
	}

	@Override
	public ResponseEntity<ResponseStructure<userResponse>> verifyOTP(OtpRequest otpRequest) {

		if(otpCache.get(otpRequest.getEmail()) == null) throw new OtpExpiredException("otp expired"); 

		if(!otpCache.get(otpRequest.getEmail()).equals(otpRequest.getOtp())) throw new OtpIncorrectException("otp is wrong"); 
		User user = userCache.get(otpRequest.getEmail());

		if(user == null) throw new RegistrationSessionExpired("Registration Time is Expired");
		user.setEmailVerified(true);
		user = userRepo.save(user);

		return ResponseEntity.status(HttpStatus.CREATED)
				.body(structure.setBody(mapToUserResponse(user))
						.setMessage("OTP verification successfull")
						.setStatus(HttpStatus.CREATED.value()));
	}

	private userResponse mapToUserResponse(User user) {
		return userResponse.builder()
				.userId(user.getUserId())
				.userName(user.getUserName())
				.displayName(user.getDisplayName())
				.email(user.getEmail())
				.userRole(user.getUserRole())
				.isEmailVerified(user.isEmailVerified())
				.build();
	}

	private String generateOTP() {
		return String.valueOf(new Random().nextInt(100000, 999999));
	}

	private <T extends User> T mapToChildEntity(UserRequest userRequest) {
		UserRole role = userRequest.getUserRole();

		User user = null;
		switch(role) {
		case SELLER -> user = new Seller();
		case CUSTOMER -> user = new Customer();
		default -> throw new RuntimeException();
		}

		user.setDisplayName(userRequest.getName());
		user.setUserName(userRequest.getEmail().split("@gmail.com")[0]);
		user.setEmail(userRequest.getEmail());
		user.setPassword(passwordEncoder.encode(userRequest.getPassword()));
		user.setUserRole(role);
		user.setEmailVerified(false);

		return (T) user;
	}

	@Override
	public ResponseEntity<ResponseStructure<AuthResponse>> userLogin(AuthRequest authRequest) {
		String username = authRequest.getEmail().split("@")[0];

		System.out.println(username + " "+ authRequest.getPassword());
		Authentication authenticate = authenticationManager.authenticate(
				new  UsernamePasswordAuthenticationToken(username, authRequest.getPassword()));

		if(!authenticate.isAuthenticated()) throw new AuthenticationException("Authentication failed");

		SecurityContextHolder.getContext().setAuthentication(authenticate);

		//generate access and refresh token
		HttpHeaders headers = new HttpHeaders();

		userRepo.findByUserName(username).ifPresent(user ->{
			generateAccessToken(user, headers);
			generateRefreshToken(user, headers);

		});
		return ResponseEntity.ok().headers(headers).body(authResponseStucture.setMessage("login succesfully")
				.setStatus(HttpStatus.OK.value())
				.setBody(mapToAuthResponse(username,accessExpiration,refreshExpiration)));

		//add as cookie to the response
		//generate authResponse and return	
	}

	private AuthResponse mapToAuthResponse(String username, long accessExpiration, long refreshExpiration) {
		User user = userRepo.findByUserName(username).get();

		return AuthResponse.builder()
				.userId(user.getUserId())
				.username(user.getUserName())
				.userRole(user.getUserRole())
				.accessExpiration(accessExpiration)
				.refreshExpiration(refreshExpiration)
				.build();
	}

	private void generateRefreshToken(User user, HttpHeaders headers) {
		String token = jwtService.generateRefreshToken(user,user.getUserRole().name());
		headers.add(HttpHeaders.SET_COOKIE, configureCookie("rt", token, refreshExpiration));
		//store the token to the database

		RefreshToken refreshToken = new RefreshToken();
		refreshToken.setToken(token);
		refreshToken.setExpiration(LocalDateTime.now());
		refreshToken.setBlocked(false);

		refreshTokenRepo.save(refreshToken);
	}

	private void generateAccessToken(User user, HttpHeaders headers) {
		String token = jwtService.generateAccessToken(user,user.getUserRole().name());
		headers.add(HttpHeaders.SET_COOKIE, configureCookie("at", token, accessExpiration));

		AccessToken accessToken = new AccessToken();
		accessToken.setToken(token);
		accessToken.setExpiration(LocalDateTime.now());
		accessToken.setBlocked(false);

		accessTokenRepo.save(accessToken);
	}

	private String configureCookie(String name, String value, long maxAge) {
		return ResponseCookie.from(name, value)
				.domain("localhost")
				.path("/")
				.httpOnly(true)
				.secure(false)
				.maxAge(Duration.ofMillis(maxAge))
				.sameSite("Lax")
				.build().toString();
	}

	@Override
	public ResponseEntity<SimpleResponseStructure> userLogout(String accessToken, String refreshToken) {

		if(accessToken==null || refreshToken==null ) 
			throw new UserIsNotLoginException("Please LogIn");

		HttpHeaders headers = new HttpHeaders();

		accessTokenRepo.findByToken(accessToken).ifPresent(access ->{

			refreshTokenRepo.findByToken(refreshToken).ifPresent(refresh ->{

				access.setBlocked(true);
				accessTokenRepo.save(access);
				refresh.setBlocked(true);
				refreshTokenRepo.save(refresh);

				removeAccess("at",headers);
				removeAccess("rt",headers);
			});
		});
		return ResponseEntity.ok().headers(headers)
				.body(simpleResponseStructure.setMessage("LogOut Sucessfully...")
						.setStatus(HttpStatus.OK.value()));
	}

	private void removeAccess(String value, HttpHeaders headers) {
		headers.add(HttpHeaders.SET_COOKIE, removeCookie(value));
	}

	private String removeCookie(String name) {
		return ResponseCookie.from(name,"")
				.domain("localhost")
				.path("/")
				.httpOnly(true)
				.secure(false)
				.maxAge(0)
				.sameSite("Lax")
				.build().toString();
	}

	@Override
	public ResponseEntity<ResponseStructure<AuthResponse>> refreshLoginAndTokenRotation(String accessToken,
			String refreshToken) {
		System.out.println(accessToken);
		if(refreshToken == null)
			throw new UserIsNotLoginException("user not loged");

		if(accessToken != null)
			accessTokenRepo.findByToken(accessToken).ifPresent(token -> {
				token.setBlocked(true);
				accessTokenRepo.save(token);
			});

		Date date = jwtService.getIssuedDate(refreshToken);
		String username = jwtService.getUserName(refreshToken);

		HttpHeaders headers = new HttpHeaders();

		return userRepo.findByUserName(username).map(user -> {
			if(date.before(new Date())) {
				refreshTokenRepo.findByToken(refreshToken).ifPresent(token->{
					token.setBlocked(true);
					refreshTokenRepo.save(token);
				});
				generateRefreshToken(user, headers);
			}
			else
				headers.add(HttpHeaders.SET_COOKIE, configureCookie("rt", refreshToken, refreshExpiration));

			generateAccessToken(user, headers);
			return ResponseEntity.ok().headers(headers)
					.body(authResponseStucture.setStatus(HttpStatus.ACCEPTED.value())
							.setBody(mapToAuthResponse(user.getUserName(), accessExpiration, refreshExpiration))
							.setMessage("token Success"));
		}).get();
	}
}
