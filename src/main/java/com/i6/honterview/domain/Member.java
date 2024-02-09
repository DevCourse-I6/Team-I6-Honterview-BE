package com.i6.honterview.domain;

import java.time.LocalDateTime;

import com.i6.honterview.domain.enums.Gender;
import com.i6.honterview.domain.enums.Provider;
import com.i6.honterview.domain.enums.Role;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "member")
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
public class Member extends BaseEntity {

	@Id
	@Column(name = "id", nullable = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "email", nullable = false)
	private String email;

	@Column(name = "name", nullable = false)
	private String name;

	@Column(name = "birth_date")
	private String birthDate;

	@Column(name = "gender")
	@Enumerated(EnumType.STRING)
	private Gender gender;

	@Column(name = "provider")
	@Enumerated(EnumType.STRING)
	private Provider provider;

	@Column(name = "last_login_at")
	private LocalDateTime lastLoginAt;

	@Enumerated(EnumType.STRING)
	@Column(name = "role")
	private Role role;

	// TODO : status

}
