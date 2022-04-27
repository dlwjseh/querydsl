package com.example.querydsl.repository;

import com.example.querydsl.domain.User;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

import static com.example.querydsl.domain.QUser.user;

@Repository
public class UserQuerydslRepository {
    private final EntityManager em;
    private final JPAQueryFactory queryFactory;
    private final UserQuerydslSearchExpressionProvider searchProvider;

    public UserQuerydslRepository(EntityManager em, UserQuerydslSearchExpressionProvider searchProvider) {
        this.em = em;
        this.queryFactory = new JPAQueryFactory(em);
        this.searchProvider = searchProvider;
    }

    public List<User> findAll() {
        return queryFactory
                .selectFrom(user)
                .fetch();
    }

}
