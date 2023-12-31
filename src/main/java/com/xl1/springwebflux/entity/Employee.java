package com.xl1.springwebflux.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor @AllArgsConstructor
@Builder
@Document(collection = "employee")
public class Employee {
	@Id
	private String id;
	private String firstName;
	private String lastName;
	private String email;
}
