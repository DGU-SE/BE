package com.twofullmoon.howmuchmarket.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "User")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User {

	@Id
	@Column(length = 36, nullable = false)
	private String id;

	@Column(nullable = false, length = 45)
	private String pw;

	@Column(nullable = false, length = 45)
	private String name;

	@Column(name = "account_number", nullable = false, length = 45)
	private String accountNumber;

	@ManyToOne
	@JoinColumn(name = "Location_id", nullable = false)
	private Location location;
}
