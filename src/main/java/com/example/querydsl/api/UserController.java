package com.example.querydsl.api;

import com.example.querydsl.dto.UserSearchDto;
import com.example.querydsl.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {
	private final UserService userService;

	@GetMapping
	public ResponseEntity<?> searchUsers(@RequestBody UserSearchDto searchDto) {
		return ResponseEntity.ok(userService.searchUsers(searchDto));
	}

}
