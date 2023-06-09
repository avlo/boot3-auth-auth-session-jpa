package com.prosilion.scdecisionmatrix.model.entity;

import java.io.Serializable;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

/**
 * Join table between AuthDetailsUser (used for auth & auth) and AppUser (used for
 * application specific business logic.
 */
@Data
@NoArgsConstructor
@Entity(name = "appuser_authuser")
public class AppUserAuthUser implements Serializable {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	private Integer appUserId;
	private String authUserName;

	public AppUserAuthUser(@NonNull Integer appUserId, @NonNull String authUserName) {
		this.appUserId = appUserId;
		this.authUserName = authUserName;
	}
}
