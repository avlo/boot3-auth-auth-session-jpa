package com.prosilion.scdecisionmatrix.model.entity.security;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.bind.Name;
import org.springframework.ldap.odm.annotations.Attribute;
import org.springframework.ldap.odm.annotations.Entry;

//@Getter
//@Setter
//@Entry(base="ou=groups", objectClasses = {
//		"top",
//		"inetOrgPerson", "person", "organizationalPerson",
//		"simpleSecurityObject"})
public class LdapPerson {
	@JsonIgnore
	@Id
	private Name id;
	@JsonProperty("userName")
	private @Attribute(name = "uid") String uid;
	@JsonProperty("firstName")
	private @Attribute(name = "cn") String firstName;
	@JsonIgnore
	private @Attribute(name = "displayname") String displayName;
	@JsonProperty("lastName")
	private  @Attribute(name = "sn") String lastName;

	public LdapPerson(String uid, String firstName, String displayName, String lastName) {
		this.uid = uid;
		this.firstName = firstName;
		this.displayName = displayName;
		this.lastName = lastName;
	}

	public LdapPerson(String userName, String firstName, String lastName) {
		this.uid = userName;
		this.firstName = firstName;
		this.lastName = lastName;
	}
}

