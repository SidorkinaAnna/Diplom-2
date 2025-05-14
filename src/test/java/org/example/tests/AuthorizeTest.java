package org.example.tests;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.example.dto.RequestDTO;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.example.stepdefs.Steps.*;
import static org.hamcrest.Matchers.equalTo;

public class AuthorizeTest {
    private String token = "";

    @Before
    public void setUp() {
        RestAssured.baseURI = "https://stellarburgers.nomoreparties.site";
    }
    @After
    public void tearDown(){
        deleteUser(token);
    }

    @Test
    @DisplayName("Login real user")
    @Description("Success login real user")
    public void loginWithRealUser(){
        RequestDTO requestDTO = new RequestDTO("eva2@kot.com", "123qwe", "eva");
        token = registration(requestDTO).jsonPath().getString("accessToken");

        Response authResponse = authorization(new RequestDTO("eva2@kot.com", "123qwe"));

        authResponse.then()
                .assertThat()
                .statusCode(200)
                .body("success", equalTo(true))
                .body("user.email", equalTo("eva2@kot.com"))
                .body("user.name", equalTo("eva"));
    }

    @Test
    @DisplayName("Login unauthorized user")
    @Description("Unsuccessful login fake user")
    public void loginWithUnauthorizedUser(){
        RequestDTO requestAuth = new RequestDTO("eva23@kot.com", "12");
        Response authResponse = authorization(requestAuth);
        authResponse.then()
                .assertThat()
                .statusCode(401)
                .body("success", equalTo(false))
                .body("message", equalTo("email or password are incorrect"));
        }


}
