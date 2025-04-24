package apiAutotests.playerController.update.roleModel;

import api.actions.PlayerActions;
import api.model.*;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.restassured.response.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import static api.helpersAndUtils.GeneratingAgeHelper.generateStringAgeDefault;
import static api.helpersAndUtils.GeneratingPasswordHelper.generatePasswordDefault;
import static api.helpersAndUtils.GeneratingUniqueNameHelper.generateUniqueName;
import static api.helpersAndUtils.Names.*;


@Epic("Api Tests")
@Feature("updatePlayer")
public class UpdatePlayerByAdminForUserTest {

    private static final Logger logger = LogManager.getLogger(UpdatePlayerByAdminForUserTest.class);

    private PlayerActions playerActions;


    String initialScreenName = generateUniqueName(SCREEN_NAME);
    String password = generatePasswordDefault();
    String loginAdmin = generateUniqueName(LOGIN_NAME);
    long adminId;
    long userId;


    @BeforeClass(description = "Инициализация Action-класса")
    public void setup() {
        playerActions = new PlayerActions();
    }

    @BeforeMethod
    public void creatingValidAdmin() {
        // Создание админа
        PlayerCreateRequestDto playerAdmin = new PlayerCreateRequestDto(
                generateStringAgeDefault(),
                GENDER_M,
                loginAdmin,
                password,
                ROLE_ADMIN,
                generateUniqueName(SCREEN_NAME)
        );
        Response responseAdmin = playerActions.createPlayer(LOGIN_SUPERVISOR, playerAdmin);
        PlayerCreateResponseDto responseBodyAdmin = responseAdmin.as(PlayerCreateResponseDto.class);
        adminId = responseBodyAdmin.getId();

        // Создание юзера
        PlayerCreateRequestDto playerUser = new PlayerCreateRequestDto(
                generateStringAgeDefault(),
                GENDER_M,
                generateUniqueName(LOGIN_NAME),
                password,
                ROLE_USER,
                initialScreenName
        );
        Response responseUser = playerActions.createPlayer(LOGIN_SUPERVISOR, playerUser);
        PlayerCreateResponseDto responseBodyUser = responseUser.as(PlayerCreateResponseDto.class);
        userId = responseBodyUser.getId();
    }

    @Description("Admin может обновлять пользователя, если это user")
    @Test(description = "Admin успешно обновляет игроков роли User согласно ролевой модели")
    public void updatingUserPlayerByAdminIsPossible() {
        SoftAssert softAssert = new SoftAssert();
// Обновление юзера
        PlayerUpdateRequestDto updatedPlayer = new PlayerUpdateRequestDto(
                null,
                null,
                null,
                null,
                generateUniqueName(SCREEN_NAME)
        );
        Response response = playerActions.updatePlayer(loginAdmin, String.valueOf(userId), updatedPlayer);
        softAssert.assertEquals(response.getStatusCode(), 200);
        PlayerUpdateResponseDto responseBody = response.as(PlayerUpdateResponseDto.class);
        String newScreenName = responseBody.getScreenName();
        logger.info("screenName юзера до обновления: " + initialScreenName + "\nscreenName юзера после обновления: " + newScreenName);
        softAssert.assertNotEquals(newScreenName, initialScreenName, "screenName игрока не обновился");

// Подтверждение того, что игрок действительно обновлён
        Response getResponse = playerActions.getPlayerByPlayerId(userId);
        softAssert.assertEquals(getResponse.getStatusCode(), 200);
        PlayerGetByPlayerIdResponseDto getResponseBody = getResponse.as(PlayerGetByPlayerIdResponseDto.class);
        softAssert.assertEquals(getResponseBody.getScreenName(), newScreenName, "ScreenName полученного и обновлённого игроков не совпадают");

        softAssert.assertAll();
    }

    @AfterMethod
    public void deletingDataPlayers() {
        playerActions.deletePlayer(LOGIN_SUPERVISOR, adminId);
        playerActions.deletePlayer(LOGIN_SUPERVISOR, userId);
    }
}
