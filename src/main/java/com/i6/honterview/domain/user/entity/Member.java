package com.i6.honterview.domain.user.entity;

import java.time.LocalDateTime;

import com.i6.honterview.common.entity.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Builder;
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

	@Column(name = "nickname")
	private String nickname;

	@Column(name = "provider")
	@Enumerated(EnumType.STRING)
	private Provider provider;

	@Column(name = "last_login_at")
	private LocalDateTime lastLoginAt;

	@Enumerated(EnumType.STRING)
	@Column(name = "role")
	private Role role;

	@Builder
	public Member(String email, String nickname, Provider provider, Role role) {
		this.email = email;
		this.nickname = nickname;
		this.provider = provider;
		this.role = role;
	}

	public void updateLastLoginAt() {
		this.lastLoginAt = LocalDateTime.now();
	}
}
