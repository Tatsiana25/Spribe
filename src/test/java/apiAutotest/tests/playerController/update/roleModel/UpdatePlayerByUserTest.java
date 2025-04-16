package apiAutotest.tests.playerController.update.roleModel;

import apiAutotest.actions.PlayerActions;
import apiAutotest.model.PlayerCreateRequestDto;
import apiAutotest.model.PlayerCreateResponseDto;
import apiAutotest.model.PlayerUpdateRequestDto;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static apiAutotest.helpersAndUtils.GeneratingAgeHelper.generateIntAgeDefault;
import static apiAutotest.helpersAndUtils.GeneratingAgeHelper.generateStringAgeDefault;
import static apiAutotest.helpersAndUtils.GeneratingPasswordHelper.generatePasswordDefault;
import static apiAutotest.helpersAndUtils.GeneratingUniqueNameHelper.generateUniqueName;
import static apiAutotest.helpersAndUtils.Names.*;


@Epic("Api Tests")
@Feature("updatePlayer")
public class UpdatePlayerByUserTest {
    private PlayerActions playerActions;

    String loginUser = generateUniqueName(LOGIN_NAME);
    long userId;
    long playerIdForUpdate;


    @BeforeClass
    public void setup() {
        // Инициализация Action-класса
        playerActions = new PlayerActions();
    }

    @BeforeMethod
    public void creatingValidUser() {
//        PlayerCreateRequestDto playerAdmin = new PlayerCreateRequestDto(
//                generateStringAgeDefault(),
//                GENDER_M,
//                loginAdmin,
//                password,
//                ROLE_ADMIN,
//                screenName
//        );
//        Response responseAdmin = playerActions.createPlayer(LOGIN_SUPERVISOR, playerAdmin);
//        PlayerCreateResponseDto responseBodyAdmin = responseAdmin.as(PlayerCreateResponseDto.class);
//        adminId = responseBodyAdmin.getId();

        PlayerCreateRequestDto playerUser = new PlayerCreateRequestDto(
                generateStringAgeDefault(),
                GENDER_M,
                loginUser,
                generatePasswordDefault(),
                ROLE_USER,
                generateUniqueName(SCREEN_NAME)
        );
        Response responseUser = playerActions.createPlayer(LOGIN_SUPERVISOR, playerUser);
        PlayerCreateResponseDto responseBodyUser = responseUser.as(PlayerCreateResponseDto.class);
        userId = responseBodyUser.getId();
    }

    @Description("User не может обновлять пользователя, если это admin")
    @Test(description = "User не может обновлять игроков с ролью admin согласно ролевой модели")
    public void updatingAdminPlayerByUserIsImpossible() {
        // Создание игрока (что бы удалить)
        PlayerCreateRequestDto player = new PlayerCreateRequestDto(
                generateStringAgeDefault(),
                GENDER_F,
                generateUniqueName(LOGIN_NAME),
                generatePasswordDefault(),
                ROLE_ADMIN,
                generateUniqueName(SCREEN_NAME));
        Response responseCreate = playerActions.createPlayer(LOGIN_SUPERVISOR, player);
        PlayerCreateResponseDto responseBody = responseCreate.as(PlayerCreateResponseDto.class);
        playerIdForUpdate = responseBody.getId();

// Обновление игрока
        PlayerUpdateRequestDto updatedPlayer = new PlayerUpdateRequestDto(
                generateIntAgeDefault(),
                null,
                null,
                null,
                generateUniqueName(SCREEN_NAME)
        );
        Response response = playerActions.updatePlayer(loginUser, String.valueOf(playerIdForUpdate), updatedPlayer);
        Assert.assertEquals(response.getStatusCode(), 403);
    }

    @Description("User не может обновлять другого игрока с ролью user")
    @Test(description = "User не может обновлять другого игрока с ролью user согласно ролевой модели")
    public void updatingUserPlayerByUserIsImpossible() {
        // Создание игрока (что бы удалить)
        PlayerCreateRequestDto player = new PlayerCreateRequestDto(
                generateStringAgeDefault(),
                GENDER_M,
                generateUniqueName(LOGIN_NAME),
                generatePasswordDefault(),
                ROLE_USER,
                generateUniqueName(SCREEN_NAME));
        Response responseCreate = playerActions.createPlayer(LOGIN_SUPERVISOR, player);
        PlayerCreateResponseDto responseBody = responseCreate.as(PlayerCreateResponseDto.class);
        playerIdForUpdate = responseBody.getId();

// Обновление игрока
        PlayerUpdateRequestDto updatedPlayer = new PlayerUpdateRequestDto(
                generateIntAgeDefault(),
                null,
                null,
                null,
                generateUniqueName(SCREEN_NAME)
        );
        Response response = playerActions.updatePlayer(loginUser, String.valueOf(playerIdForUpdate), updatedPlayer);
        Assert.assertEquals(response.getStatusCode(), 403);
    }

    @AfterMethod
    public void deletingDataPlayers() {
        playerActions.deletePlayer(LOGIN_SUPERVISOR, userId);
        playerActions.deletePlayer(LOGIN_SUPERVISOR, playerIdForUpdate);
    }
}
