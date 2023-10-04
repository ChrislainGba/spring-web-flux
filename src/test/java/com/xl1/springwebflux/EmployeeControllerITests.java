package com.xl1.springwebflux;

import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.test.web.reactive.server.WebTestClient.ResponseSpec;

import com.xl1.springwebflux.dto.EmployeeDto;
import com.xl1.springwebflux.repository.EmployeeRepository;
import com.xl1.springwebflux.service.EmployeeService;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

//start the server and listening to random port
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class EmployeeControllerITests {
	
	@Autowired
	private EmployeeService employeeService;
	@Autowired
	private WebTestClient webTestClient;
	
	@Autowired
	private EmployeeRepository employeeRepository;
	
	private static EmployeeDto employeeDto;
	
	@BeforeEach
	public void setup() {
		System.out.println("Before each test");
		employeeRepository.deleteAll().subscribe();
		
		employeeDto = EmployeeDto.builder()
		.firstName("You")
		.lastName("me")
		.email("youme@xl1.net")
		.build();
	}
	

	@DisplayName("Junt SpringWebFlux controller IT for saveEmployee")
	@Test
	public void testSaveEmployee() {
		//given - precondition or setup
		
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
		

	@DisplayName("Junt SpringWebFlux controller IT for getEmployee")
	@Test
	public void testGetEmployee() {
		//given - precondition or setup
		String employeeId = "651d303007ce0e4e6ab064f0";
		EmployeeDto savedEmployeeDto = employeeService.saveEmployee(employeeDto).block();
		//when - action or the behaviour we are going to test
		ResponseSpec response = webTestClient.get().uri("/api/employees/{id}", Collections.singletonMap("id", savedEmployeeDto.getId())).exchange();
		//then - verify the output
		response.expectStatus().isOk()
				.expectBody()
				.consumeWith(System.out::println)
				.jsonPath("$.firstName").isEqualTo(employeeDto.getFirstName())
				.jsonPath("$.firstName").isEqualTo(employeeDto.getFirstName())
				.jsonPath("$.lastName").isEqualTo(employeeDto.getLastName());
	}
		

	@DisplayName("Junt SpringWebFlux controller IT for getAllEmployee")
	@Test
	public void testGetAllEmployees() {
		//given - precondition or setup
		employeeService.saveEmployee(employeeDto).block();
		//when - action or the behaviour we are going to test
		ResponseSpec response = webTestClient.get().uri("/api/employees")
			.accept(MediaType.APPLICATION_JSON)
			.exchange();
		//then - verify the output
		response
			.expectStatus().isOk()
			.expectBodyList(EmployeeDto.class)
			.consumeWith(System.out::println);
	}
	
	@DisplayName("Junt SpringWebFlux controller IT for updateEmployee")
	@Test
	public void testUpdateEmployee() {
		//given - precondition or setup
		EmployeeDto savedEmployeeDto = employeeService.saveEmployee(employeeDto).block();
		
		EmployeeDto updatedEmployeeDto = EmployeeDto.builder()
				.firstName("Trach")
				.lastName("Tracher")
				.email("trach@xl1.net")
				.build();
		
		//when - action or the behaviour we are going to test
		ResponseSpec response = webTestClient.put().uri("/api/employees/{id}",Collections.singletonMap("id", savedEmployeeDto.getId()))
					.accept(MediaType.APPLICATION_JSON)
					.contentType(MediaType.APPLICATION_JSON)
					.body(Mono.just(updatedEmployeeDto), EmployeeDto.class)
					.exchange();
		//then - verify the output
		response
			.expectStatus().isOk()
			.expectBody()
			.consumeWith(System.out::println)
			.jsonPath("$.firstName").isEqualTo(updatedEmployeeDto.getFirstName())
			.jsonPath("$.firstName").isEqualTo(updatedEmployeeDto.getFirstName())
			.jsonPath("$.lastName").isEqualTo(updatedEmployeeDto.getLastName());
	}
	
	@DisplayName("Junt SpringWebFlux controller IT for deleteEmployee")
	@Test
	public void testDeleteEmployee() {
		//given - precondition or setup
		EmployeeDto savedEmployeeDto = employeeService.saveEmployee(employeeDto).block();

		//when - action or the behaviour we are going to test
		ResponseSpec response = webTestClient.delete().uri("/api/employees/{id}",Collections.singletonMap("id", savedEmployeeDto.getId()))
				.accept(MediaType.APPLICATION_JSON)
				.exchange();
		//then - verify the output
		response
		.expectStatus().isNoContent();
	}
}
