package com.example.querydsl.example;

import com.example.querydsl.domain.Team;
import com.example.querydsl.domain.User;
import com.example.querydsl.dto.UserSearchDto;
import com.example.querydsl.repository.UserQuerydslSearchExpressionProvider;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import java.time.format.DateTimeFormatter;
import java.util.List;

import static com.example.querydsl.domain.QTeam.team;
import static com.example.querydsl.domain.QUser.user;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.NONE;

/**
 * 동적 검색
 */
@DataJpaTest
@Import({UserQuerydslSearchExpressionProvider.class})
@AutoConfigureTestDatabase(replace = NONE)
public class DynamicSearchTest {

	@Autowired
	EntityManager em;
	@Autowired
	UserQuerydslSearchExpressionProvider expressionProvider;

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

	@Test
	@DisplayName("검색: String-Sql")
	void searchString() {
		UserSearchDto search = new UserSearchDto("전도", null, null, "서비스");

		String sql = "" +
				"SELECT " +
				"    u.* " +
				"FROM user u " +
				"INNER JOIN team t ON u.team_id = t.id " +
				"WHERE 1=1 ";

		if (StringUtils.hasText(search.getName())) {
			sql += "AND u.name LIKE :userName ";
		}
		if (search.getAgeMin() != null) {
			sql += "AND u.age >= :ageMin ";
		}
		if (search.getUserRegDateMin() != null) {
			sql += "AND u.reg_date >= :regDateMin ";
		}
		if (StringUtils.hasText(search.getTeamName())) {
			sql += "AND t.name LIKE :teamName ";
		}

		Query query = em.createNativeQuery(sql, User.class);

		if (StringUtils.hasText(search.getName())) {
			query.setParameter("userName", "%" + search.getName() + "%");
		}
		if (search.getAgeMin() != null) {
			query.setParameter("ageMin", search.getAgeMin());
		}
		if (search.getUserRegDateMin() != null) {
			String regDateMin = search.getUserRegDateMin().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
			query.setParameter("regDateMin", regDateMin);
		}
		if (StringUtils.hasText(search.getTeamName())) {
			query.setParameter("teamName", "%" + search.getTeamName() + "%");
		}

		List<User> users = query.getResultList();
		assertThat(users).hasSize(1);
		assertThat(users.get(0).getName()).isEqualTo("이전도");
	}

	@Test @Disabled
	@DisplayName("검색: Querydsl")
	void dtoSearchQuerydsl() {
		JPAQueryFactory queryFactory = new JPAQueryFactory(em);
		List<User> users = queryFactory
				.selectFrom(user)
				.innerJoin(user.team, team)
				.fetch();
	}
	public BooleanExpression userNameLike(String userName) {
		return StringUtils.isEmpty(userName) ? null : user.name.contains(userName);
	}

	@Test
	@DisplayName("검색 연습")
	void dtoSearchQuerydslPractice() {
		/**
		 * TODO : 아래 쿼리를 구현
		 * SELECT
		 *     u.*
		 * FROM user u
		 * INNER JOIN team t ON u.team_id = team.id
		 * WHERE u.age < 30
		 * AND t.name LIKE '서비스%'
		 * ORDER BY u.age
		 */
	}

}
