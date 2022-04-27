package com.example.querydsl.example;

import com.example.querydsl.domain.AgeClub;
import com.example.querydsl.domain.User;
import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import javax.persistence.EntityManager;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.NONE;

/**
 * 연관관계가 없는 조인
 */
@DataJpaTest
@AutoConfigureTestDatabase(replace = NONE)
public class UnRelatedJoinTest {

	@Autowired
	EntityManager em;

	private final String ageClubName = "클럽";

	@BeforeEach
	public void setTestData() {
		em.persist(new User("이전도", 36));
		em.persist(new User("황윤호", 28));
		em.persist(new AgeClub(ageClubName, 36));
	}

	/**
	 * 예시 쿼리 :
	 * SELECT
	 *     u.id, u.name, u.age,
	 *     a.name as age_club_name
	 * FROM user u
	 * INNER JOIN age_club a ON u.age = a.age
	 */

	@Test
	@DisplayName("조인: String-Sql")
	void joinString() {
		String sql = "";
		sql += "SELECT u.id, u.name, u.age, a.name as age_club_name FROM user u ";
		sql += "INNER JOIN age_club a ON u.age = a.age ";
		List<Object[]> results = em.createNativeQuery(sql).getResultList();
		assertThat(results).hasSize(1);
		assertThat(results.get(0)[3]).isEqualTo(ageClubName);
	}

	@Test
	@DisplayName("조인: JPQL")
	void joinJPQL() {
		String sql = "";
		sql += "SELECT u.id, u.name, u.age, a.name as ageClubName FROM User u ";
		sql += "INNER JOIN AgeClub a ON u.age = a.age";
		List<Object[]> results = em.createQuery(sql).getResultList();
		assertThat(results).hasSize(1);
		assertThat(results.get(0)[3]).isEqualTo(ageClubName);
	}

	@Test
	@DisplayName("조인: Querydsl")
	void joinQuerydsl() {
		JPAQueryFactory queryFactory = new JPAQueryFactory(em);
		List<Tuple> results = null;
		assertThat(results).hasSize(1);
		assertThat(results.get(0).get(3, String.class)).isEqualTo(ageClubName);
	}

}
