package com.prosilion.scdecisionmatrix.model.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

/**
 * Extendable/customizable user, indirectly bound to Spring Security Authentication
 * and Authorization user via join service/table:
 * @see com.prosilion.scdecisionmatrix.service.AppUserAuthUserService
 */
@Getter
@Setter
@Entity
@Table(name="appuser")
public class AppUser {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
}
