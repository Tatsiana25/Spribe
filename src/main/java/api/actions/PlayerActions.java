package api.actions;

import api.model.PlayerCreateRequestDto;
import api.model.PlayerDeleteRequestDto;
import api.model.PlayerGetByPlayerIdRequestDto;
import api.model.PlayerUpdateRequestDto;
import api.specifications.Specs;
import io.qameta.allure.Step;
import io.restassured.RestAssured;
import io.restassured.response.Response;

public class PlayerActions {
    public PlayerActions() {
        Specs.setSpecs(Specs.getRequestSpec(), Specs.getResponseSpec());
    }

    @Step("Выполняем запрос 'getAllPlayers' на получение списка всех игроков")
    public Response getAllPlayers() {
        return RestAssured.given()
                .when()
                .get("/player/get/all");
    }

    @Step("Выполняем запрос 'getPlayerByPlayerId' на получение игрока по PlayerId")
    public Response getPlayerByPlayerId(Long playerId) {
        PlayerGetByPlayerIdRequestDto requestBody = new PlayerGetByPlayerIdRequestDto(playerId);
        return RestAssured.given()
                .body(requestBody)
                .when()
                .post("/player/get");
    }

    @Step("Выполняем запрос 'deletePlayer' на удаление игрока")
    public Response deletePlayer(String editor, long playerId) {
        PlayerDeleteRequestDto requestBody = new PlayerDeleteRequestDto(playerId);
        return RestAssured.given()
                .pathParam("editor", editor)
                .body(requestBody)
                .when()
                .delete("/player/delete/{editor}");
    }

    @Step("Выполняем запрос 'createPlayer' на создание игрока")
    public Response createPlayer(String editor, PlayerCreateRequestDto player) {
        return RestAssured.given()
                .queryParams(player.allParamsToMap()) // Передаём все query параметры через Map
                .pathParam("editor", editor)
                .when()
                .get("/player/create/{editor}");
    }

    //Сделано так из-за большого кол-ва необязательных полей
    @Step("Выполняем запрос 'updatePlayer' на обновление игрока")
    public Response updatePlayer(String editor, String id, PlayerUpdateRequestDto updatedPlayer) {
        return RestAssured.given()
                .pathParam("editor", editor)
                .pathParam("id", id)
                .body(updatedPlayer)
                .when()
                .patch("/player/update/{editor}/{id}");
    }
}
