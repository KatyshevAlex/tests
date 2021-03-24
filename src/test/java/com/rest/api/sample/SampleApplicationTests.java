package com.rest.api.sample;

import com.rest.api.sample.model.Product;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static io.restassured.RestAssured.*;
import static io.restassured.http.ContentType.JSON;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class SampleApplicationTests {


	Product p;

	@BeforeEach
	void setUp(){
		p = new Product();
		p.setName("M-50");
		p.setCategory("Mouse");
		p.setRetailPrice(10.5);
		p.setAvailability(true);
	}

	@Test
	void addOneProduct() {
		with()
				.body(p)
				.header("Content-Type","application/json" )
				.header("Accept","application/json" )
				.when()
				.request("POST", "/products")
				.then()
				.contentType(JSON)
				.statusCode(201)
				.assertThat()
				.body("name", equalTo(p.getName()));

		p.setId(1L);

		with()
				.body(p)
				.header("Content-Type","application/json" )
				.header("Accept","application/json" )
				.when()
				.request("POST", "/products")
				.then()
				.contentType(JSON)
				.statusCode(400);
	}


	@Test
	@Sql("classpath:insert-test-data.sql")
	void getAllProducts() {
		//test that getList without parameters will return list of 3
		when().get("/products/")
				.then()
				.contentType(JSON)
				.assertThat()
				.body("size()",is(3));

		//test that getList with category Keyboard will return list of 2
		given()
				.param("category", "Keyboard")
				.when()
				.get("/products")
				.then()
				.contentType(JSON)
				.assertThat()
				.body("size()",is(2));

		//test that getList with <category Keyboard> & <availability false> will return list of 1
		given()
				.param("category", "Keyboard")
				.param("availability", "false")
				.when()
				.get("/products")
				.then()
				.contentType(JSON)
				.assertThat()
				.body("size()",is(1));
	}

	@Test
	void createProduct() {
		RequestSpecification request = given();
		request.header("content-type", MediaType.APPLICATION_JSON_VALUE);
		request.body(new Product());
		Response response = request.post("/products").andReturn();
		assertEquals(HttpStatus.CREATED.value(), response.getStatusCode());
//		response.body()
//		String location = response.getHeader("location");
//		assertTrue(String.format("%s should end with /contacts/5", location), location.endsWith("/contacts/5"));
	}
}
