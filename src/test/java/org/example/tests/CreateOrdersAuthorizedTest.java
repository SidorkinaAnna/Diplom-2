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

public class CreateOrdersAuthorizedTest {
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
    @DisplayName("Create order with real user")
    @Description("Successful create order with real user with ingredients")
    public void createOrderWithIngredients(){

        List<String> ingredients = getIngredientsIds();
        Order body = new Order(ingredients);
        Response createOrderExistUser = createOrder(token, body);

        createOrderExistUser.then()
                .assertThat()
                .statusCode(200)
                .body("success", equalTo(true))
                .body("order.ingredients[0]._id", equalTo(ingredients.get(0)))
                .body("order.ingredients[1]._id", equalTo(ingredients.get(1)));
    }

    private static List<String> getIngredientsIds() {
        List<Ingredient> ingredientList = Steps.getIngredients().getData();
        String ingredient0Id = ingredientList.get(0).getId();
        String ingredient1Id = ingredientList.get(1).getId();

        return List.of(ingredient0Id, ingredient1Id);
    }

    @Test
    @DisplayName("Create order with real user without ingredients")
    @Description("Unsuccessful create order with real user without ingredients")
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
    @DisplayName("Create order with real user with wrong ingredients")
    @Description("Unsuccessful create order with real user with wrong ingredients")
    public void createOrderWrongIngredients(){

        Order body = new Order(List.of("pupupu", "wrongId"));
        Response createOrderExistUser = createOrder(token, body);

        createOrderExistUser.then()
                .assertThat()
                .statusCode(500);
    }

}
