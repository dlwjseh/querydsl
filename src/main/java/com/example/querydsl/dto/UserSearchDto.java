package com.example.querydsl.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * 유저 검색용 Dto
 */
@Getter
@Setter
public class UserSearchDto {

	private String name;

	private Integer ageMin;

	private LocalDateTime userRegDateMin;

	private String teamName;

	public UserSearchDto(String name, Integer ageMin, LocalDateTime userRegDateMin, String teamName) {
		this.name = name;
		this.ageMin = ageMin;
		this.userRegDateMin = userRegDateMin;
		this.teamName = teamName;
	}
}
