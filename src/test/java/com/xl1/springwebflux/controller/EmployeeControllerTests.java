package com.xl1.springwebflux.controller;

import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.test.web.reactive.server.WebTestClient.ResponseSpec;

import com.xl1.springwebflux.dto.EmployeeDto;
import com.xl1.springwebflux.service.EmployeeService;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

//to use spring test context framework we register SpringExtension for EmployeeControllerTests
@ExtendWith(SpringExtension.class)
//load only necessary beans to test mployeeController - This make JUnit test run faster
@WebFluxTest(controllers = EmployeeController.class)
public class EmployeeControllerTests {
	//to test Reactive REST API we use it to reacte HTTP request
	@Autowired
	private WebTestClient webTestClient;
	
	@MockBean
	private EmployeeService employeeService;
	
	private static EmployeeDto employeeDto;
	
	@BeforeEach
	public void setup() {
		employeeDto = EmployeeDto.builder()
		.firstName("Minpik")
		.lastName("minpiker")
		.email("minpik@xl1.net")
		.build();
	}
	

	@DisplayName("Junt SpringWebFlux controller test for saveEmployee")
	@Test
	public void givenEmployeeObject_whenSaveEmployee_thenReturnSavedEmployee() {
		//given - precondition or setup
		
		BDDMockito.given(employeeService.saveEmployee(ArgumentMatchers.any(EmployeeDto.class)))
		.willReturn(Mono.just(employeeDto));
		
		//when - action or the behaviour we are going to test
		ResponseSpec response = webTestClient.post().uri("/api/employees")
			.contentType(MediaType.APPLICATION_JSON)
			.accept(MediaType.APPLICATION_JSON)
			.body(Mono.just(employeeDto),EmployeeDto.class)
			.exchange();//exchange to make REST API call
		
		//then - verify the output
		response.expectStatus().isCreated()
				.expectBody().consumeWith(System.out::println)
				.jsonPath("$.firstName").isEqualTo(employeeDto.getFirstName())
				.jsonPath("$.firstName").isEqualTo(employeeDto.getFirstName())
				.jsonPath("$.lastName").isEqualTo(employeeDto.getLastName());
	}
	

	@DisplayName("Junt SpringWebFlux controller test for getEmployee")
	@Test
	public void givenEmployeeId_whenGetEmployee_thenReturnEmployeeObject() {
		//given - precondition or setup
		String employeeId = "123";
		
		BDDMockito.given(employeeService.getEmployee(employeeId)).willReturn(Mono.just(employeeDto));
		//when - action or the behaviour we are going to test
		ResponseSpec response = webTestClient.get().uri("/api/employees/{id}", Collections.singletonMap("id", employeeId)).exchange();
		//then - verify the output
		response.expectStatus().isOk()
				.expectBody()
				.consumeWith(System.out::println)
				.jsonPath("$.firstName").isEqualTo(employeeDto.getFirstName())
				.jsonPath("$.firstName").isEqualTo(employeeDto.getFirstName())
				.jsonPath("$.lastName").isEqualTo(employeeDto.getLastName());
	}
	

	@DisplayName("Junt SpringWebFlux controller test for getAllEmployee")
	@Test
	public void givenListOfEmployees_whenGetAllEmployees_thenReturnListOfEmployees() {
		//given - precondition or setup

		EmployeeDto employeeDto1 = EmployeeDto.builder()
				.firstName("you")
				.lastName("me")
				.email("youme@xl1.net")
				.build();
		List<EmployeeDto> employees = List.of(employeeDto,employeeDto1);
		
		BDDMockito.given(employeeService.getAllEmployees()).willReturn(Flux.fromIterable(employees));
		//when - action or the behaviour we are going to test
		ResponseSpec response = webTestClient.get().uri("/api/employees")
			.accept(MediaType.APPLICATION_JSON)
			.exchange();
		//then - verify the output
		response
			.expectStatus().isOk()
			.expectBodyList(EmployeeDto.class).hasSize(2)
			.consumeWith(System.out::println);
	}
	
	@DisplayName("Junt SpringWebFlux controller test for updateEmployee")
	@Test
	public void givenUpdatedEmplyee_whenUpdateEmployee_thenReturnUpdatedEMployeeObject() {
		//given - precondition or setup
		String employeeId = "123";
		
		BDDMockito.given(employeeService.updateEmployee(ArgumentMatchers.any(EmployeeDto.class), ArgumentMatchers.any(String.class))).willReturn(Mono.just(employeeDto));	
		//when - action or the behaviour we are going to test
		ResponseSpec response = webTestClient.put().uri("/api/employees/{id}",Collections.singletonMap("id", employeeId))
					.accept(MediaType.APPLICATION_JSON)
					.contentType(MediaType.APPLICATION_JSON)
					.body(Mono.just(employeeDto), EmployeeDto.class)
					.exchange();
		//then - verify the output
		response
			.expectStatus().isOk()
			.expectBody()
			.consumeWith(System.out::println)
			.jsonPath("$.firstName").isEqualTo(employeeDto.getFirstName())
			.jsonPath("$.firstName").isEqualTo(employeeDto.getFirstName())
			.jsonPath("$.lastName").isEqualTo(employeeDto.getLastName());
	}
	
	@DisplayName("Junt SpringWebFlux controller test for deleteEmployee")
	@Test
	public void givenEMployeeId_whenDeleteEmployee_thenReturnNothing() {
		//given - precondition or setup
		String employeeId = "123";

		BDDMockito.given(employeeService.deleteEmployee(employeeId)).willReturn(Mono.empty());
		//when - action or the behaviour we are going to test
		ResponseSpec response = webTestClient.delete().uri("/api/employees/{id}",Collections.singletonMap("id", employeeId))
				.accept(MediaType.APPLICATION_JSON)
				.exchange();
		//then - verify the output
		response
		.expectStatus().isNoContent();
	}
}
