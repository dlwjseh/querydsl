package com.example.querydsl.service;

import com.example.querydsl.domain.User;
import com.example.querydsl.dto.UserSearchDto;
import com.example.querydsl.repository.UserQuerydslRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
	private final UserQuerydslRepository userQuerydslRepository;

	public List<User> searchUsers(UserSearchDto searchDto) {
		return userQuerydslRepository.searchUsers(searchDto);
	}
}
