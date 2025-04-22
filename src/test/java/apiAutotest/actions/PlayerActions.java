package apiAutotest.actions;

import apiAutotest.model.PlayerCreateRequestDto;
import apiAutotest.model.PlayerDeleteRequestDto;
import apiAutotest.model.PlayerGetByPlayerIdRequestDto;
import apiAutotest.model.PlayerUpdateRequestDto;
import apiAutotest.specifications.Specs;
import io.qameta.allure.Step;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class PlayerActions {
    public PlayerActions() {
        Specs.setSpecs(Specs.getRequestSpec(), Specs.getResponseSpec());
    }

    @Step("Выполняем запрос 'getAllPlayers' на получение списка всех игроков")
    public Response getAllPlayers() {
        return given()
                .when()
                .get("/player/get/all");
    }

    @Step("Выполняем запрос 'getPlayerByPlayerId' на получение игрока по PlayerId")
    public Response getPlayerByPlayerId(Long playerId) {
        PlayerGetByPlayerIdRequestDto requestBody = new PlayerGetByPlayerIdRequestDto(playerId);

        return given()
                .body(requestBody)
                .when()
                .post("/player/get");

    }

    @Step("Выполняем запрос 'deletePlayer' на удаление игрока")
    public Response deletePlayer(String editor, long playerId) {
        PlayerDeleteRequestDto requestBody = new PlayerDeleteRequestDto(playerId);
        return given()
                .pathParam("editor", editor)
                .body(requestBody)
                .when()
                .delete("/player/delete/{editor}");
    }

    @Step("Выполняем запрос 'createPlayer' на создание игрока")
    public Response createPlayer(String editor, PlayerCreateRequestDto player) {
        return given()
                .queryParam("age", player.getAge())
                .pathParam("editor", editor)
                .queryParam("gender", player.getGender())
                .queryParam("login", player.getLogin())
                .queryParam("password", player.getPassword())
                .queryParam("role", player.getRole())
                .queryParam("screenName", player.getScreenName())
                .when()
                .get("/player/create/{editor}");
    }

    //Сделано так из-за большого кол-ва необязательных полей
    @Step("Выполняем запрос 'updatePlayer' на обновление игрока")
    public Response updatePlayer(String editor, String id, PlayerUpdateRequestDto updatedPlayer) {
        return given()
                .pathParam("editor", editor)
                .pathParam("id", id)
                .body(updatedPlayer)
                .when()
                .patch("/player/update/{editor}/{id}");
    }
}
