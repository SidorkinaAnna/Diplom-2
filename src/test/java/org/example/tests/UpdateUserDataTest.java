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

public class UpdateUserDataTest {
    private String token = "";

    @Before
    public void setUp() {
        RestAssured.baseURI = "https://stellarburgers.nomoreparties.site";
        RequestDTO requestDTO = new RequestDTO("eva2@kot.com", "123qwe", "eva");
        token = registration(requestDTO).jsonPath().getString("accessToken");
    }
    @After
    public void tearDown(){
        deleteUser(token);
    }

    @Test
    @DisplayName("Update email user")
    @Description("Success update email user")
    public void updateEmailUser(){

        Response updateEmailUser = updateDataUser(token, new RequestDTO("edit@mail.com", "123qwe", "eva"));

        updateEmailUser.then()
                .assertThat()
                .statusCode(200)
                .body("success", equalTo(true))
                .body("user.email", equalTo("edit@mail.com"))
                .body("user.name", equalTo("eva"));
    }

    @Test
    @DisplayName("Update name user")
    @Description("Success update name user")
    public void updateNameUser(){

        Response updateEmailUser = updateDataUser(token, new RequestDTO("eva2@kot.com", "123qwe", "edit"));

        updateEmailUser.then()
                .assertThat()
                .statusCode(200)
                .body("success", equalTo(true))
                .body("user.email", equalTo("eva2@kot.com"))
                .body("user.name", equalTo("edit"));
    }

    @Test
    @DisplayName("Update passport user")
    @Description("Success update passport user")
    public void updatePassportUser(){

        Response updateEmailUser = updateDataUser(token, new RequestDTO("eva2@kot.com", "newpass", "eva"));

        updateEmailUser.then()
                .assertThat()
                .statusCode(200)
                .body("success", equalTo(true))
                .body("user.email", equalTo("eva2@kot.com"))
                .body("user.name", equalTo("eva"));

        Response authResponse = authorization(new RequestDTO("eva2@kot.com", "newpass"));
        authResponse.then()
                .assertThat()
                .statusCode(200)
                .body("success", equalTo(true));
    }

    @Test
    @DisplayName("Update email user on exist email")
    @Description("Unsuccessful update email user on exist email")
    public void updateEmailOnExistEmail(){

        RequestDTO secondRequestDTO = new RequestDTO("emailAlreadyExist@mail.com", "444", "user");
        String secondToken = registration(secondRequestDTO).jsonPath().getString("accessToken");

        Response updateEmailOnExistEmail = updateDataUser(token,
                new RequestDTO("emailAlreadyExist@mail.com", "123qwe", "eva"));

        updateEmailOnExistEmail.then()
                .assertThat()
                .statusCode(403)
                .body("success", equalTo(false))
                .body("message", equalTo("User with such email already exists"));

        deleteUser(secondToken);
    }

    @Test
    @DisplayName("Update unauthorized user")
    @Description("Unsuccessful update unauthorized user")
    public void updateUserUnauthorized(){
        String emptyToken = "";

        Response updateEmailOnExistEmail = updateDataUser(emptyToken,
                new RequestDTO("eva@mail.com", "123qwe", "eva"));

        updateEmailOnExistEmail.then()
                .assertThat()
                .statusCode(401)
                .body("success", equalTo(false))
                .body("message", equalTo("You should be authorised"));
    }





}
