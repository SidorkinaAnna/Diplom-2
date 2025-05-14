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

public class RegistrationTest {
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
    @DisplayName("Registration user")
    @Description("Success registration user")
    public void successReg(){
        RequestDTO requestDTO = new RequestDTO("eva@kot.com", "123qwe", "eva");
        Response response = registration(requestDTO);
        response.then()
                .assertThat()
                .statusCode(200)
                .body("success", equalTo(true))
                .body("user.email", equalTo("eva@kot.com"))
                .body("user.name", equalTo("eva"));
        token = response.jsonPath().getString("accessToken");
    }

    @Test
    @DisplayName("Registration user exist")
    @Description("Unsuccessful registration user exist")
    public void userIsAlreadyExist(){
        RequestDTO requestDTO = new RequestDTO("eva@kot.com", "123qwe", "eva");
        Response response = registration(requestDTO);
        response.then()
                .assertThat()
                .statusCode(200)
                .body("success", equalTo(true))
                .body("user.email", equalTo("eva@kot.com"))
                .body("user.name", equalTo("eva"));
        token = response.jsonPath().getString("accessToken");
        Response secondResponse = registration(requestDTO);
        secondResponse.then()
                .assertThat()
                .statusCode(403)
                .body("success", equalTo(false))
                .body("message", equalTo("User already exists"));
    }

    @Test
    @DisplayName("Registration user without email")
    @Description("Unsuccessful registration user without email")
    public void unsuccessfulRegWithoutEmail(){
        RequestDTO requestDTO = new RequestDTO("", "123qwe", "eva");
        Response response = registration(requestDTO);
        response.then()
                .assertThat()
                .statusCode(403)
                .body("success", equalTo(false))
                .body("message", equalTo("Email, password and name are required fields"));
    }

    @Test
    @DisplayName("Registration user without passport")
    @Description("Unsuccessful registration user without passport")
    public void unsuccessfulRegWithoutPassword(){
        RequestDTO requestDTO = new RequestDTO("eva@kot.com", "", "eva");
        Response response = registration(requestDTO);
        response.then()
                .assertThat()
                .statusCode(403)
                .body("success", equalTo(false))
                .body("message", equalTo("Email, password and name are required fields"));
    }

}
