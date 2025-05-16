package com.ffma.controller;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ffma.dto.JwtResponse;
import com.ffma.dto.LoginRequest;
import com.ffma.dto.RegisterRequest;
import com.ffma.entity.Role;
import com.ffma.entity.User;
import com.ffma.repo.RoleRepo;
import com.ffma.repo.UserRepo;
import com.ffma.security.JwtUtil;

@RestController
@RequestMapping("api/auth")
public class AuthController {
	
	@Autowired
	private AuthenticationManager authManager;
	@Autowired
	private JwtUtil jwtUtil;
	@Autowired
	private UserRepo userRepo;
	@Autowired
	private RoleRepo roleRepo;
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	
	@PostMapping("/login")
	public ResponseEntity<?> login (@RequestBody LoginRequest request){
		
		Authentication auth = authManager.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
		String token = jwtUtil.generateToken(request.getUsername());
		
		return ResponseEntity.ok(new JwtResponse(token));
		
	}
	
	@PostMapping("/register")
	public ResponseEntity<?> register(@RequestBody RegisterRequest request){
		
		if(userRepo.findByUsername(request.getUsername()).isPresent()){
			return ResponseEntity.badRequest().body("UserName Allready exists.");
		}
		Role userRole = roleRepo.findByName("USER").orElseThrow( ()-> new RuntimeException("Default Role Not Found"));
		
		User user = new User();
		user.setUsername(request.getUsername());
		user.setPassword(passwordEncoder.encode(request.getPassword()));
		user.setRoles(Set.of(userRole));
		userRepo.save(user);
		return ResponseEntity.ok("User Resistered Successfully");
		
		
		
	}
	
	
	
	

}
