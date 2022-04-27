package com.example.querydsl.example;

import com.example.querydsl.domain.Team;
import com.example.querydsl.domain.User;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import javax.persistence.EntityManager;
import java.util.List;

import static com.example.querydsl.domain.QUser.user;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.NONE;

/**
 * 기본쿼리
 */
@DataJpaTest
@AutoConfigureTestDatabase(replace = NONE)
public class BasicQueryTest {

    @Autowired
    EntityManager em;

    @BeforeEach
    public void setTestData() {
        em.persist(new User("이전도", 36));
        em.persist(new User("황윤호", 28));
    }

    /**
     * 예시 쿼리 :
     * SELECT * FROM user
     */

    @Test
    @DisplayName("기본쿼리: String-Sql")
    @SuppressWarnings("unchecked")
    void basicString() {
        String query = "SELECT * FROM user";
        List<User> users = em.createNativeQuery(query, User.class)
                .getResultList();
        assertThat(users).hasSize(2);
    }

    @Test
    @DisplayName("기본쿼리: JPQL")
    void basicJpql() {
        String query = "SELECT u FROM User u";
        List<User> users = em.createQuery(query, User.class)
                .getResultList();
        assertThat(users).hasSize(2);
    }

    @Test
    @DisplayName("기본쿼리: Querydsl")
    void basicQuerydsl() {
        JPAQueryFactory queryFactory = new JPAQueryFactory(em);
        List<User> users = null;
        assertThat(users).hasSize(2);
    }

    @Test
    @DisplayName("기본쿼리 연습")
    void basicQuerydslPractice() {
        em.persist(new Team("서비스 플랫폼"));
        em.persist(new Team("서비스 솔루션"));
        em.flush();
        em.clear();

        // TODO : 팀 목록을 불러오기(SELECT * FROM team)

    }

}
