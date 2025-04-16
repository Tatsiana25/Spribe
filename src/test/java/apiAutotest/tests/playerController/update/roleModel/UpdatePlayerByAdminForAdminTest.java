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
public class UpdatePlayerByAdminForAdminTest {
    private PlayerActions playerActions;

    String screenName = generateUniqueName(SCREEN_NAME);
    String password = generatePasswordDefault();
    String loginAdmin = generateUniqueName(LOGIN_NAME);
    long adminId;


    @BeforeClass
    public void setup() {
        // Инициализация Action-класса
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
                screenName
        );
        Response responseAdmin = playerActions.createPlayer(LOGIN_SUPERVISOR, playerAdmin);
        PlayerCreateResponseDto responseBodyAdmin = responseAdmin.as(PlayerCreateResponseDto.class);
        adminId = responseBodyAdmin.getId();
    }

    @Description("Admin может обновлять данные самого себя")
    @Test(description = "Admin успешно обновляет свои данные согласно ролевой модели")
    public void selfUpdatingByAdminIsPossible() {

// Обновление игрока
        PlayerUpdateRequestDto updatedPlayer = new PlayerUpdateRequestDto(
                null,
                null,
                null,
                null,
                generateUniqueName(SCREEN_NAME)
        );
        Response response = playerActions.updatePlayer(loginAdmin, String.valueOf(adminId), updatedPlayer);
        Assert.assertEquals(response.getStatusCode(), 200);
        PlayerUpdateResponseDto responseBody = response.as(PlayerUpdateResponseDto.class);
        Assert.assertNotEquals(responseBody.getScreenName(), screenName, "screenName игрока не обновился");
    }

    @AfterMethod
    public void deletingMethodPlayers() {
        playerActions.deletePlayer(LOGIN_SUPERVISOR, adminId);
    }
}
