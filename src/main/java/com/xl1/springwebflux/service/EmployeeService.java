package com.xl1.springwebflux.service;

import com.xl1.springwebflux.dto.EmployeeDto;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface EmployeeService {
	Mono<EmployeeDto> saveEmployee(EmployeeDto employeeDto);
	Mono<EmployeeDto> getEmployee(String id);
	Flux<EmployeeDto> getAllEmployees();
	Mono<EmployeeDto> updateEmployee(EmployeeDto employeeDto, String employeeId);
	Mono<Void> deleteEmployee(String id);
	
}
