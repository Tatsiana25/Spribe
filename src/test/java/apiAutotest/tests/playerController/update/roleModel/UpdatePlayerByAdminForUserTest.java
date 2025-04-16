package apiAutotest.tests.playerController.update.roleModel;

import apiAutotest.actions.PlayerActions;
import apiAutotest.model.PlayerCreateRequestDto;
import apiAutotest.model.PlayerCreateResponseDto;
import apiAutotest.model.PlayerUpdateRequestDto;
import apiAutotest.model.PlayerUpdateResponseDto;
import io.qameta.allure.Description;
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
@Feature("updatePlayer")
public class UpdatePlayerByAdminForUserTest {
    private PlayerActions playerActions;


    String screenName = generateUniqueName(SCREEN_NAME);
    String password = generatePasswordDefault();
    String loginAdmin = generateUniqueName(LOGIN_NAME);
    long adminId;
    long userId;


    @BeforeClass
    public void setup() {
        // Инициализация Action-класса
        playerActions = new PlayerActions();
    }

    @BeforeMethod
    public void creatingValidAdmin() {
        PlayerCreateRequestDto playerAdmin = new PlayerCreateRequestDto(
                generateStringAgeDefault(),
                GENDER_M,
                loginAdmin,
                password,
                ROLE_ADMIN,
                screenName
        );
        Response responseAdmin = playerActions.createPlayer(LOGIN_SUPERVISOR, playerAdmin);
        PlayerCreateResponseDto responseBodyAdmin = responseAdmin.as(PlayerCreateResponseDto.class);
        adminId = responseBodyAdmin.getId();

        PlayerCreateRequestDto playerUser = new PlayerCreateRequestDto(
                generateStringAgeDefault(),
                GENDER_M,
                generateUniqueName(LOGIN_NAME),
                password,
                ROLE_USER,
                screenName
        );
        Response responseUser = playerActions.createPlayer(LOGIN_SUPERVISOR, playerUser);
        PlayerCreateResponseDto responseBodyUser = responseUser.as(PlayerCreateResponseDto.class);
        userId = responseBodyUser.getId();
    }

    @Description("Admin может обновлять пользователя, если это user")
    @Test(description = "Admin успешно обновляет игроков роли User согласно ролевой модели")
    public void updatingUserPlayerByAdminIsPossible() {
// Обновление игрока
        PlayerUpdateRequestDto updatedPlayer = new PlayerUpdateRequestDto(
                null,
                null,
                null,
                null,
                generateUniqueName(SCREEN_NAME)
        );
        Response response = playerActions.updatePlayer(loginAdmin, String.valueOf(userId), updatedPlayer);
        Assert.assertEquals(response.getStatusCode(), 200);
        PlayerUpdateResponseDto responseBody = response.as(PlayerUpdateResponseDto.class);
        Assert.assertNotEquals(responseBody.getScreenName(), screenName, "screenName игрока не обновился");
    }

    @AfterMethod
    public void deletingDataPlayers() {
        playerActions.deletePlayer(LOGIN_SUPERVISOR, adminId);
        playerActions.deletePlayer(LOGIN_SUPERVISOR, userId);
    }
}
