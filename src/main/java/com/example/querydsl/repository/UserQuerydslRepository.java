package com.example.querydsl.repository;

import com.example.querydsl.domain.QUser;
import com.example.querydsl.domain.User;
import com.example.querydsl.dto.UserSearchDto;
import com.querydsl.core.types.dsl.BooleanExpression;
import org.springframework.stereotype.Repository;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;

import static com.example.querydsl.domain.QUser.user;

@Repository
public class UserQuerydslRepository extends Querydsl4RepositorySupport {
	public UserQuerydslRepository() {
		super(User.class);
	}

	public List<User> searchUsers(UserSearchDto searchDto) {
		return selectFrom(user)
				.where(
						userNameLike(searchDto.getName()),
						userAgeGoe(searchDto.getAgeMin()),
						userRegDateGoe(searchDto.getUserRegDateMin()),
						teamNameLike(searchDto.getTeamName())
				)
				.orderBy(user.id.asc())
				.fetch();
	}

	private BooleanExpression userNameLike(String userName) {
		return StringUtils.isEmpty(userName) ? null : user.name.contains(userName);
	}
	private BooleanExpression userAgeGoe(Integer ageMin) {
		return ObjectUtils.isEmpty(ageMin) ? null : user.age.goe(ageMin);
	}
	private BooleanExpression userRegDateGoe(LocalDateTime regDateMin) {
		return ObjectUtils.isEmpty(regDateMin) ? null : user.regDate.goe(regDateMin);
	}
	private BooleanExpression teamNameLike(String teamName) {
		return StringUtils.isEmpty(teamName) ? null : user.team.name.contains(teamName);
	}

}
