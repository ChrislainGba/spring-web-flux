package com.xl1.springwebflux.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.xl1.springwebflux.dto.EmployeeDto;
import com.xl1.springwebflux.service.EmployeeService;

import lombok.AllArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/employees")
@AllArgsConstructor
public class EmployeeController {
	
	private EmployeeService employeeService;
	//build reactive saveEmployee REST API
	//Mono return simgle Object and Flux return many
	@PostMapping
	@ResponseStatus(value = HttpStatus.CREATED)
	public Mono<EmployeeDto> saveEmployee(@RequestBody EmployeeDto employeeDto){
		return employeeService.saveEmployee(employeeDto);
	}
	@GetMapping("{id}")
	public Mono<EmployeeDto> getEmployeeById(@PathVariable String id){
		return employeeService.getEmployee(id);
	}
	
	@GetMapping
	public Flux<EmployeeDto> getAllEmployees(){
		return employeeService.getAllEmployees();
	}
	
	@PutMapping("{id}")
	@ResponseStatus(value = HttpStatus.OK)
	public Mono<EmployeeDto> updateEmployee(@RequestBody EmployeeDto employeeDto, @PathVariable("id") String id){
		return employeeService.updateEmployee(employeeDto, id);
	}
	
	@DeleteMapping("{id}")
	@ResponseStatus(value = HttpStatus.NO_CONTENT)
	public Mono<Void> deleteEmployee(@PathVariable("id") String id){
		return employeeService.deleteEmployee(id);
	}

}
