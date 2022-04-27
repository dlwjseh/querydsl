package com.example.querydsl.example;

import com.example.querydsl.domain.*;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import javax.persistence.EntityManager;
import java.util.List;

import static com.example.querydsl.domain.QPosition.position;
import static com.example.querydsl.domain.QUser.user;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.NONE;

/**
 * 연관관계가 있는 조인
 */
@DataJpaTest
@AutoConfigureTestDatabase(replace = NONE)
public class RelatedJoinTest {

	@Autowired
	EntityManager em;

	private final String teamName = "서비스 플랫폼";

	@BeforeEach
	public void setTestData() {
		Team platformTeam = new Team(teamName);
		em.persist(platformTeam);
		em.persist(new User("이전도", 36, platformTeam));
		em.persist(new User("황윤호", 28, platformTeam));
	}

	/**
	 * 예시 쿼리 :
	 * SELECT
	 *     u.id, u.name, u.age, u.team_id,
	 *     t.name as team_name
	 * FROM user u
	 * INNER JOIN team t ON u.team_id = t.id
	 */

	@Test
	@DisplayName("조인: String-Sql")
	void joinString() {
		String sql = "";
		sql += "SELECT u.id, u.name, u.age, u.team_id, t.name as team_name FROM user u ";
		sql += "INNER JOIN team t ON u.team_id = t.id ";
		List results = em.createNativeQuery(sql).getResultList(); // List<Object[]>
		assertThat(results).hasSize(2);
	}

	@Test
	@DisplayName("조인: JPQL")
	void joinJPQL() {
		List<User> users = em.createQuery("SELECT u FROM User u INNER JOIN FETCH u.team t", User.class).getResultList();
		assertThat(users).hasSize(2);
		assertThat(users).allMatch(u -> u.getTeam().getName().equals(teamName));
	}

	@Test
	@DisplayName("조인: Querydsl")
	void joinQuerydsl() {
		JPAQueryFactory queryFactory = new JPAQueryFactory(em);
		List<User> users = null;
		assertThat(users).hasSize(2);
		assertThat(users).allMatch(u -> u.getTeam().getName().equals(teamName));
	}
	
	@Test
	@DisplayName("조인 연습")
	void joinQuerydslPractice() {
		JPAQueryFactory queryFactory = new JPAQueryFactory(em);

		Position teamLeader = new Position("팀장");
		Position employee = new Position("사원");
		em.persist(teamLeader);
		em.persist(employee);
		em.persist(new User("송민수", 40, teamLeader));
		em.persist(new User("유채린", 27, employee));

		// TODO : 유저의 직책을 LEFT JOIN 해서 조회 (※ setTestData 에서 등록한 이전도, 황윤호도 같이 조회되어야 한다.)
		List<User> users = null;
		assertThat(users).hasSize(4);
	}

}
