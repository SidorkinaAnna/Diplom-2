package org.example.tests;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.example.dto.Ingredient;
import org.example.dto.Order;
import org.example.dto.RequestDTO;
import org.example.stepdefs.Steps;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Collections;
import java.util.List;

import static org.example.stepdefs.Steps.*;
import static org.hamcrest.Matchers.equalTo;

public class CreateOrdersUnauthorizedTest {
    private String token = "";

    @Before
    public void setUp() {
        RestAssured.baseURI = "https://stellarburgers.nomoreparties.site";
    }


    @Test
    @DisplayName("Create order with unauthorized user")
    @Description("Successful create order with unauthorized user with ingredients")
    public void createOrderWithIngredients() {
        List<String> ingredients = getIngredientsIds();
        Order body = new Order(ingredients);
        Response createOrderExistUser = createOrder(token, body);

        createOrderExistUser.then()
                .assertThat()
                .statusCode(200)
                .body("success", equalTo(true));
    }


    private static List<String> getIngredientsIds() {
        List<Ingredient> ingredientList = Steps.getIngredients().getData();
        String ingredient0Id = ingredientList.get(0).getId();
        String ingredient1Id = ingredientList.get(1).getId();

        return List.of(ingredient0Id, ingredient1Id);
    }

    @Test
    @DisplayName("Create order with unauthorized user without ingredients")
    @Description("Unsuccessful create order with unauthorized user without ingredients")
    public void createOrderNoIngredients(){

        Order body = new Order(Collections.emptyList());
        Response createOrderExistUser = createOrder(token, body);

        createOrderExistUser.then()
                .assertThat()
                .statusCode(400)
                .body("success", equalTo(false))
                .body("message", equalTo("Ingredient ids must be provided"));
    }


    @Test
    @DisplayName("Create order with unauthorized user with wrong ingredients")
    @Description("Unsuccessful create order with unauthorized user with wrong ingredients")
    public void createOrderWrongIngredients(){

        Order body = new Order(List.of("pupupu", "wrongId"));
        Response createOrderExistUser = createOrder(token, body);

        createOrderExistUser.then()
                .assertThat()
                .statusCode(500);
    }
}