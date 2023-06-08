package com.prosilion.scdecisionmatrix.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AppUserDto {
	private Long id;
	private String firstName;
	private String password;
}