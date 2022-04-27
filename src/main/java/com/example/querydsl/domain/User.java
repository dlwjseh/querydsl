package com.example.querydsl.domain;

import lombok.Getter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Entity
public class User {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private Integer age;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id")
    private Team team;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "position_id")
    private Position position;

    private LocalDateTime regDate;

    protected User() {}

    public User(String name, Integer age) {
        this.name = name;
        this.age = age;
        this.regDate = LocalDateTime.now();
    }

    public User(String name, Integer age, Team team) {
        this(name, age);
        this.team = team;
    }

    public User(String name, Integer age, Position position) {
        this(name, age);
        this.position = position;
    }

    public User(String name, Integer age, Team team, LocalDateTime regDate) {
        this(name, age, team);
        this.regDate = regDate;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", age=" + age +
                ", regDate=" + regDate +
                '}';
    }
}
