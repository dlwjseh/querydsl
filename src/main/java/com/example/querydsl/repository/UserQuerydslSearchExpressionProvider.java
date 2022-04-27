package com.example.querydsl.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import static com.example.querydsl.domain.QUser.user;

@Component
public class UserQuerydslSearchExpressionProvider {

	/**
	 * 유저명 LIKE 검색
	 * name = '%?%'
	 * @param userName 유저명
	 */
	public BooleanExpression userNameLike(String userName) {
		return StringUtils.isEmpty(userName) ? null : user.name.contains(userName);
	}

}
