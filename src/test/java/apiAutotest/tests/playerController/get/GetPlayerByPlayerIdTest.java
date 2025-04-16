package apiAutotest.tests.playerController.get;

import apiAutotest.actions.PlayerActions;
import apiAutotest.model.PlayerCreateRequestDto;
import apiAutotest.model.PlayerCreateResponseDto;
import apiAutotest.model.PlayerGetByPlayerIdResponseDto;
import io.qameta.allure.Allure;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static apiAutotest.helpersAndUtils.GeneratingAgeHelper.generateStringAgeDefault;
import static apiAutotest.helpersAndUtils.GeneratingPasswordHelper.generatePasswordDefault;
import static apiAutotest.helpersAndUtils.GeneratingUniqueNameHelper.generateUniqueName;
import static apiAutotest.helpersAndUtils.Names.*;


@Epic("Api Tests")
@Feature("getPlayerByPlayerId")
public class GetPlayerByPlayerIdTest {
    private PlayerActions playerActions;

    long playerId;

    @BeforeClass
    public void setup() {
        // Инициализация Action-класса
        playerActions = new PlayerActions();
    }

    @BeforeMethod
    public void creatingValidPlayer() {
        PlayerCreateRequestDto player = new PlayerCreateRequestDto(
                generateStringAgeDefault(),
                GENDER_M,
                generateUniqueName(LOGIN_NAME),
                generatePasswordDefault(),
                ROLE_ADMIN,
                generateUniqueName(SCREEN_NAME)
        );
        Response response = playerActions.createPlayer(LOGIN_SUPERVISOR, player);
        PlayerCreateResponseDto responseBody = response.as(PlayerCreateResponseDto.class);
        playerId = responseBody.getId();
    }

    @Test(description = "Можно получить игрока по существующему PlayerId")
    public void gettingPlayerIsPossibleIfPlayerIdExists() {
        Response response = playerActions.getPlayerByPlayerId(playerId);
        Assert.assertEquals(response.getStatusCode(), 200);
        PlayerGetByPlayerIdResponseDto responseBody = response.as(PlayerGetByPlayerIdResponseDto.class);
        Assert.assertEquals(responseBody.getId(), playerId, "ID игроков не совпадают");
    }

    @Test(description = "Нельзя получить игрока, если PlayerId не существует")
    public void gettingPlayerIsImpossibleIfPlayerIdInvalid() {

        Allure.description("Нельзя получить игрока, если PlayerId не существует");

        playerActions.deletePlayer(LOGIN_SUPERVISOR, playerId);
        Response responseGet = playerActions.getPlayerByPlayerId(playerId);
        Assert.assertEquals(responseGet.getStatusCode(), "404");
    }

    @AfterMethod
    public void deletingPlayers() {
        playerActions.deletePlayer(LOGIN_SUPERVISOR, playerId);
    }
}
