package apiAutotest.tests.playerController.create.roleModel;

import apiAutotest.actions.PlayerActions;
import apiAutotest.model.PlayerCreateRequestDto;
import apiAutotest.model.PlayerCreateResponseDto;
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
@Feature("createPlayer")
public class CreatePlayerByAdminTest {
    private PlayerActions playerActions;
    String adminLogin = generateUniqueName(LOGIN_NAME);
    long adminId;
    long theSecondAdminId;

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
                adminLogin,
                generatePasswordDefault(),
                ROLE_ADMIN,
                generateUniqueName(SCREEN_NAME)
        );
        Response responseAdmin = playerActions.createPlayer(LOGIN_SUPERVISOR, playerAdmin);
        PlayerCreateResponseDto responseBodyAdmin = responseAdmin.as(PlayerCreateResponseDto.class);
        adminId = responseBodyAdmin.getId();

    }


    @Description("Admin не может создавать игроков с ролью admin")
    @Test(description = "Admin не может создать игроков с ролью admin согласно ролевой модели")
    public void creatingAdminPlayerByAdminIsImpossible() {

        PlayerCreateRequestDto player = new PlayerCreateRequestDto(
                generateStringAgeDefault(),
                GENDER_M,
                generateUniqueName(LOGIN_NAME),
                generatePasswordDefault(),
                ROLE_ADMIN,
                generateUniqueName(SCREEN_NAME));
        Response response = playerActions.createPlayer(adminLogin, player);

        if (response.getStatusCode() == 200) {
            PlayerCreateResponseDto responseBody = response.as(PlayerCreateResponseDto.class);
            theSecondAdminId = responseBody.getId();
        }

        Assert.assertEquals(response.getStatusCode(), 403);
    }


    @AfterMethod
    public void deletingPlayers() {
        playerActions.deletePlayer(LOGIN_SUPERVISOR, adminId);
        playerActions.deletePlayer(LOGIN_SUPERVISOR, theSecondAdminId);
    }
}
