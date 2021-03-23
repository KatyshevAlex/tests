package com.rest.api.sample;

import com.rest.api.sample.model.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static io.restassured.RestAssured.*;
import static io.restassured.http.ContentType.JSON;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

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
	void getAllProducts() {
		//save 3 products
		with()
				.body(p)
				.header("Content-Type","application/json" )
				.header("Accept","application/json" )
				.when()
				.request("POST", "/products");

		p.setCategory("Keyboard");
		with()
				.body(p)
				.header("Content-Type","application/json" )
				.header("Accept","application/json" )
				.when()
				.request("POST", "/products");

		p.setAvailability(false);
		with()
				.body(p)
				.header("Content-Type","application/json" )
				.header("Accept","application/json" )
				.when()
				.request("POST", "/products");

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
