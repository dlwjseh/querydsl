package com.example.querydsl.example;

import com.example.querydsl.domain.User;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import javax.persistence.EntityManager;
import java.math.BigInteger;
import java.util.List;
import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.NONE;

/**
 * 결과 조회
 */
@DataJpaTest
@AutoConfigureTestDatabase(replace = NONE)
public class ResultTest {

	@Autowired
	EntityManager em;

	@BeforeEach
	public void setTestData() {
		em.persist(new User("이전도", 36));
		em.persist(new User("황윤호", 28));
	}

	@Test
	@DisplayName("결과값 조회: String-Sql")
	void resultString() {
		// 리스트
		List<User> users = em.createNativeQuery("SELECT * FROM user", User.class).getResultList();
		assertThat(users).hasSize(2);

		// 한건
		String name = "이전도";
		User user = (User) em.createNativeQuery("SELECT * FROM user WHERE name=:name", User.class)
				.setParameter("name", name).getSingleResult();
		assertThat(user.getName()).isEqualTo(name);

		// 카운트
		BigInteger totalCount = (BigInteger) em.createNativeQuery("SELECT COUNT(*) FROM user").getSingleResult();
		assertThat(totalCount).isEqualTo(2);
	}

	@Test
	@DisplayName("결과값 조회: JPQL")
	void resultJPQL() {
		// 리스트
		List<User> users = em.createQuery("SELECT u FROM User u", User.class).getResultList();
		assertThat(users).hasSize(2);

		// 한건
		String name = "이전도";
		User user = em.createQuery("SELECT u FROM User u WHERE u.name = :name", User.class)
				.setParameter("name", name).getSingleResult();
		assertThat(user.getName()).isEqualTo(name);

		// 카운트
		Long totalCount = em.createQuery("SELECT COUNT(u) FROM User u", Long.class).getSingleResult();
		assertThat(totalCount).isEqualTo(2L);
	}

	@Test
	@DisplayName("결과값 조회: Querydsl")
	void resultQuerydsl() {
		JPAQueryFactory queryFactory = new JPAQueryFactory(em);
		// 리스트
		List<User> users = null;
		assertThat(users).hasSize(2);

		// 한건
		String name = "이전도";
		User resultUser = null;
		assertThat(Objects.requireNonNull(resultUser).getName()).isEqualTo(name);

		// 카운트
		Long totalCount = null;
		assertThat(totalCount).isEqualTo(2L);
	}

}
