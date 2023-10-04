package com.xl1.springwebflux.repository;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import com.xl1.springwebflux.entity.Employee;

public interface EmployeeRepository extends ReactiveMongoRepository<Employee, String>{

}
