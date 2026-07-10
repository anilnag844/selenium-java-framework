package com.anilnag.selenium.api;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.notNullValue;

public class UsersApiTest {

    @BeforeClass
    public void setBaseUri() {
        RestAssured.baseURI = "https://jsonplaceholder.typicode.com";
    }

    @Test(description = "GET /users returns a non-empty list of users")
    public void getUsersReturnsList() {
        given()
                .when().get("/users")
                .then()
                .statusCode(200)
                .body("size()", greaterThan(0));
    }

    @Test(description = "GET /users/1 returns the expected user shape")
    public void getSingleUserReturnsExpectedFields() {
        given()
                .when().get("/users/1")
                .then()
                .statusCode(200)
                .body("id", equalTo(1))
                .body("email", notNullValue())
                .body("address.city", notNullValue());
    }

    @Test(description = "POST /posts creates a resource and echoes the payload")
    public void createPostEchoesPayload() {
        String body = "{\"title\":\"qa notes\",\"body\":\"selenium framework demo\",\"userId\":1}";

        Response response = given()
                .contentType("application/json")
                .body(body)
                .when().post("/posts");

        response.then()
                .statusCode(201)
                .body("title", equalTo("qa notes"));
    }
}
