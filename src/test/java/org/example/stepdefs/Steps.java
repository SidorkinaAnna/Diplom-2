package org.example.stepdefs;

import io.qameta.allure.Step;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.example.dto.IngredientsDto;
import org.example.dto.Order;
import org.example.dto.RequestDTO;

import static io.restassured.RestAssured.given;

public class Steps {


    @Step("POST request create new user with data: {request}")
    public static Response registration(RequestDTO requestDTO){
        return given()
                .contentType(ContentType.JSON)
                .body(requestDTO)
                .when()
                .post("/api/auth/register");
    }
    @Step("Delete user with token {token}")
    public static Response deleteUser(String token){
        return given()
                .header("Authorization", token)
                .when()
                .delete("/api/auth/user");
    }
    @Step("Login user with data {request}")
    public static Response authorization(RequestDTO requestDTO){
        return given()
                .contentType(ContentType.JSON)
                .body(requestDTO)
                .when()
                .post("/api/auth/login");
    }
    @Step("POST request update user with data: {request}")
    public static Response updateDataUser(String token, RequestDTO requestDTO){
        return given()
                .contentType(ContentType.JSON)
                .header("Authorization", token)
                .body(requestDTO)
                .when()
                .patch("/api/auth/user");
    }
    @Step("Get orders")
    public static Response receivingOrder(String token){
        return given()
                .header("Authorization", token)
                .when()
                .get("/api/orders");
    }
    @Step("Getting actual ingredients list")
    public static IngredientsDto getIngredients(){
        return given()
                .when()
                .get("/api/ingredients")
                .as(IngredientsDto.class);
    }


    @Step("Create order")
    public static Response createOrder(String token, Order ingredients){
        return given()
                .contentType(ContentType.JSON)
                .header("Authorization", token)
                .body(ingredients)
                .when()
                .post("/api/orders");
    }








}
