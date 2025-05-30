package apiAutotests.playerController.delete.roleModel;

import api.actions.PlayerActions;
import api.model.PlayerCreateRequestDto;
import api.model.PlayerCreateResponseDto;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.testng.Tag;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static api.helpersAndUtils.GeneratingAgeHelper.generateStringAgeDefault;
import static api.helpersAndUtils.GeneratingPasswordHelper.generatePasswordDefault;
import static api.helpersAndUtils.GeneratingUniqueNameHelper.generateUniqueName;
import static api.helpersAndUtils.Names.*;

@Epic("Api Tests")
@Feature("deletePlayer")
@Tag("Player ControlLer")
public class DeletePlayerByUserTest {
    private PlayerActions playerActions;
    long userId;
    long playerId;
    String userLogin = generateUniqueName(LOGIN_NAME);

    @BeforeClass(description = "Инициализация Action-класса")
    public void setup() {
        playerActions = new PlayerActions();
    }

    @BeforeMethod
    public void creatingValidUser() {
// Создание юзера
        PlayerCreateRequestDto playerUser = new PlayerCreateRequestDto(
                generateStringAgeDefault(),
                GENDER_M,
                userLogin,
                generatePasswordDefault(),
                ROLE_USER,
                generateUniqueName(SCREEN_NAME)
        );
        Response responseUser = playerActions.createPlayer(LOGIN_SUPERVISOR, playerUser);
        PlayerCreateResponseDto responseBodyUser = responseUser.as(PlayerCreateResponseDto.class);
        userId = responseBodyUser.getId();
    }


    @Description("User не может удалить игроков с ролью admin")
    @Test(description = "User не может удалить игроков с ролью admin")
    public void deletingAdminPlayerByUserIsImpossible() {
// Создание админа (что бы удалить)
        PlayerCreateRequestDto player = new PlayerCreateRequestDto(
                generateStringAgeDefault(),
                GENDER_M,
                generateUniqueName(LOGIN_NAME),
                generatePasswordDefault(),
                ROLE_ADMIN,
                generateUniqueName(SCREEN_NAME));
        Response responseCreate = playerActions.createPlayer(LOGIN_SUPERVISOR, player);
        PlayerCreateResponseDto responseBody = responseCreate.as(PlayerCreateResponseDto.class);
        playerId = responseBody.getId();

// Удаление игрока
        Response responseDelete = playerActions.deletePlayer(userLogin, playerId);
        Assert.assertEquals(responseDelete.getStatusCode(), 403);
    }

    @Description("User не может удалить других игроков с ролью user")
    @Test(description = "User не может удалить других игроков с ролью user")
    public void deletingUserPlayerByUserIsImpossible() {
// Создание юзера (что бы удалить)
        PlayerCreateRequestDto player = new PlayerCreateRequestDto(
                generateStringAgeDefault(),
                GENDER_M,
                generateUniqueName(LOGIN_NAME),
                generatePasswordDefault(),
                ROLE_USER,
                generateUniqueName(SCREEN_NAME));
        Response responseCreate = playerActions.createPlayer(LOGIN_SUPERVISOR, player);
        Assert.assertEquals(responseCreate.getStatusCode(), 200);
        PlayerCreateResponseDto responseBody = responseCreate.as(PlayerCreateResponseDto.class);
        playerId = responseBody.getId();

// Удаление игрока
        Response responseDelete = playerActions.deletePlayer(userLogin, playerId);
        Assert.assertEquals(responseDelete.getStatusCode(), 403);
    }

    @Description("User не может удалить себя")
    @Test(description = "User не может удалить себя")
    public void selfDeletingByUserIsImpossible() {
// Удаление игрока
        Response responseDelete = playerActions.deletePlayer(userLogin, userId);
        Assert.assertEquals(responseDelete.getStatusCode(), 403);
    }

    @AfterMethod
    public void deletingMethodPlayers() {
        playerActions.deletePlayer(LOGIN_SUPERVISOR, userId);
    }
}
