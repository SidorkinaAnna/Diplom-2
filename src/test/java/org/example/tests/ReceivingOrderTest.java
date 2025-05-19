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

import java.util.List;

import static org.example.stepdefs.Steps.*;
import static org.hamcrest.Matchers.equalTo;

public class ReceivingOrderTest {
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
    @DisplayName("Get order real user")
    @Description("Success getting order real user")
    public void receivingOrderRealUser(){
        List<String> ingredients = getIngredientsIds();
        Order body = new Order(ingredients);
        Response createOrderExistUser = createOrder(token, body);
        String orderId = createOrderExistUser.jsonPath().getString("order._id");

        Response receivingOrderRealUser = receivingOrder(token);
        receivingOrderRealUser.then()
                .assertThat()
                .statusCode(200)
                .body("success", equalTo(true))
                .body("orders[0]._id", equalTo(orderId));
    }

    private static List<String> getIngredientsIds() {
        List<Ingredient> ingredientList = Steps.getIngredients().getData();
        String ingredient0Id = ingredientList.get(0).getId();
        String ingredient1Id = ingredientList.get(1).getId();

        return List.of(ingredient0Id, ingredient1Id);
    }

    @Test
    @DisplayName("Get order without token")
    @Description("Unsuccessful getting order without token")
    public void receivingOrderUnauthorizedUser(){

        Response receivingOrderUnauthorizedUser = receivingOrder("");
        receivingOrderUnauthorizedUser.then()
                .assertThat()
                .statusCode(401)
                .body("success", equalTo(false))
                .body("message", equalTo("You should be authorised"));
    }

}
