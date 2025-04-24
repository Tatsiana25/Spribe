package apiAutotests.playerController.create.roleModel;

import api.actions.PlayerActions;
import api.model.PlayerCreateRequestDto;
import api.model.PlayerCreateResponseDto;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.restassured.response.Response;
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
@Feature("createPlayer")
public class CreatePlayerByAdminTest {
    private PlayerActions playerActions;
    String adminLogin = generateUniqueName(LOGIN_NAME);
    long adminId;

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
        SoftAssert softAssert = new SoftAssert();

        PlayerCreateRequestDto player = new PlayerCreateRequestDto(
                generateStringAgeDefault(),
                GENDER_M,
                generateUniqueName(LOGIN_NAME),
                generatePasswordDefault(),
                ROLE_ADMIN,
                generateUniqueName(SCREEN_NAME));

        Long theSecondAdminId = null;
        Response response = playerActions.createPlayer(adminLogin, player);

        try {
            softAssert.assertEquals(response.getStatusCode(), 403);
            // Если вдруг админ создался, берём его id, что бы потом удалить (очистка тестовых данных)
            PlayerCreateResponseDto responseBody = response.as(PlayerCreateResponseDto.class);
            theSecondAdminId = responseBody.getId();
        } finally {
            if (theSecondAdminId != null) {
                playerActions.deletePlayer(LOGIN_SUPERVISOR, theSecondAdminId);
            }
            softAssert.assertAll();
        }
    }


    @AfterMethod
    public void deletingPlayers() {
        playerActions.deletePlayer(LOGIN_SUPERVISOR, adminId);
    }
}
