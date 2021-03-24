package com.rest.api.sample;

import com.rest.api.sample.helper.JsonReader;
import com.rest.api.sample.model.Product;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static io.restassured.RestAssured.*;
import static io.restassured.http.ContentType.JSON;
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
	@Order(1)
	void addOneProduct() throws Exception {
		RequestSpecification request = given();
		request.header("content-type", MediaType.APPLICATION_JSON_VALUE);
		request.body(p);
		Response response = request.post("/products").andReturn();
		assertEquals(HttpStatus.CREATED.value(), response.getStatusCode());

		p.setId(1L);
		request.body(p);

		JSONAssert.assertEquals(JsonReader.read("product-1.json"),response.getBody().asString(),true);

		response = request.post("/products").andReturn();
		assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatusCode());
	}


	@Test
	@Sql("classpath:insert-test-data.sql")
	@Order(2)
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
}
