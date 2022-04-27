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

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.NONE;

/**
 * 간단한 검색
 */
@DataJpaTest
@AutoConfigureTestDatabase(replace = NONE)
public class SimpleSearchTest {

	@Autowired
	EntityManager em;

	private final String platformTeamName = "서비스 플랫폼";
	private final String solutionTeamName = "서비스 솔루션";

	@BeforeEach
	public void setTestData() {
		Team platformTeam = new Team(platformTeamName);
		Team solutionTeam = new Team(solutionTeamName);
		em.persist(platformTeam);
		em.persist(solutionTeam);
		em.persist(new User("이전도", 36, platformTeam));
		em.persist(new User("황윤호", 28, platformTeam));
		em.persist(new User("최종원", 30, solutionTeam));
		em.persist(new User("임보라", 30, solutionTeam));
	}

	/**
	 * 예시 쿼리 :
	 * SELECT
	 *     u.*
	 * FROM user u
	 * INNER JOIN team t ON u.team_id = t.id
	 * WHERE u.age > 30 AND u.name LIKE '이%'
	 *     AND t.name = '서비스 플랫폼'
	 * ORDER BY age DESC
	 */

	@Test
	@DisplayName("검색: String-Sql")
	void searchString() {
		String sql = "";
		sql += "" +
				"SELECT " +
				"    u.* " +
				"FROM user u " +
				"INNER JOIN team t ON u.team_id = t.id " +
				"WHERE u.age > 30 AND u.name LIKE '이%' " +
				"    AND t.name = :teamName " +
				"ORDER BY age DESC";
		List<User> users = em.createNativeQuery(sql, User.class)
				.setParameter("teamName", platformTeamName)
				.getResultList();
		assertThat(users).hasSize(1);
		assertThat(users.get(0).getName()).isEqualTo("이전도");
	}

	@Test
	@DisplayName("검색: JPQL")
	void searchJPQL() {
		String sql = "" +
				"SELECT " +
				"    u " +
				"FROM User u " +
				"INNER JOIN u.team t " +
				"WHERE u.age > 30 AND u.name LIKE '이%' " +
				"    AND t.name = :teamName " +
				"ORDER BY u.age DESC";
		List<User> users = em.createQuery(sql, User.class)
				.setParameter("teamName", platformTeamName)
				.getResultList();
		assertThat(users).hasSize(1);
		assertThat(users.get(0).getName()).isEqualTo("이전도");
	}

	@Test
	@DisplayName("검색: Querydsl")
	void searchQuerydsl() {
		JPAQueryFactory queryFactory = new JPAQueryFactory(em);
		List<User> users = null;
		assertThat(users).hasSize(1);
		assertThat(users.get(0).getName()).isEqualTo("이전도");
	}

}
