package com.xl1.springwebflux.service.impl;

import org.springframework.stereotype.Service;

import com.xl1.springwebflux.dto.EmployeeDto;
import com.xl1.springwebflux.entity.Employee;
import com.xl1.springwebflux.mapper.EmployeeMapper;
import com.xl1.springwebflux.repository.EmployeeRepository;
import com.xl1.springwebflux.service.EmployeeService;

import lombok.AllArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@AllArgsConstructor
public class EmployeeServiceImpl implements EmployeeService{
	
	private EmployeeRepository employeeRepository;
	
	@Override
	public Mono<EmployeeDto> saveEmployee(EmployeeDto employeeDto) {
		// TODO Auto-generated method stub
		Employee employee = EmployeeMapper.mapToEmployee(employeeDto);
		Mono<Employee> savedEmployee = employeeRepository.save(employee);
		return savedEmployee.map((e) -> EmployeeMapper.mapToEmployeeDto(e));
	}

	@Override
	public Mono<EmployeeDto> getEmployee(String id) {
		Mono<Employee> savedEmployee = employeeRepository.findById(id);
		return savedEmployee.map((e) -> EmployeeMapper.mapToEmployeeDto(e));
	}

	@Override
	public Flux<EmployeeDto> getAllEmployees() {
		Flux<Employee> savedEmployees = employeeRepository.findAll();
		return savedEmployees
				.map((employee) -> EmployeeMapper.mapToEmployeeDto(employee))
				.switchIfEmpty(Flux.empty());
	}

	@Override
	public Mono<EmployeeDto> updateEmployee(EmployeeDto employeeDto, String employeeId) {
		Mono<Employee> savedEmployee = employeeRepository.findById(employeeId);
		Mono<Employee> updatedEmployee = savedEmployee.flatMap((existingEmployee) -> {
			existingEmployee.setFirstName(employeeDto.getFirstName());
			existingEmployee.setLastName(employeeDto.getLastName());
			existingEmployee.setEmail(employeeDto.getEmail());
			return employeeRepository.save(existingEmployee);
		});
		return updatedEmployee.map((e) -> EmployeeMapper.mapToEmployeeDto(e));
	}

	@Override
	public Mono<Void> deleteEmployee(String id) {
		return employeeRepository.deleteById(id);
	}

}
