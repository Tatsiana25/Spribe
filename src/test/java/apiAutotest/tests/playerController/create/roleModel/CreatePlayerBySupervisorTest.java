package apiAutotest.tests.playerController.create.roleModel;

import apiAutotest.actions.PlayerActions;
import apiAutotest.dataProviders.CreatePlayerDataProvider;
import apiAutotest.model.PlayerCreateRequestDto;
import apiAutotest.model.PlayerCreateResponseDto;
import io.qameta.allure.Allure;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static apiAutotest.config.TestConfig.THREAD_COUNT;
import static apiAutotest.helpersAndUtils.GeneratingAgeHelper.generateStringAgeDefault;
import static apiAutotest.helpersAndUtils.GeneratingPasswordHelper.generatePasswordDefault;
import static apiAutotest.helpersAndUtils.GeneratingUniqueNameHelper.generateUniqueName;
import static apiAutotest.helpersAndUtils.Names.*;

@Epic("Api Tests")
@Feature("createPlayer")
public class CreatePlayerBySupervisorTest {
    private PlayerActions playerActions;
    long playerId;

    @BeforeClass
    public void setup() {
        // Инициализация Action-класса
        playerActions = new PlayerActions();
    }


    @Test(description = "Supervisor успешно создаёт игроков согласно ролевой модели", dataProvider = "validCreateRoleBySupervisorData",
            dataProviderClass = CreatePlayerDataProvider.class, threadPoolSize = THREAD_COUNT)
    public void creatingPlayerBySupervisorIsPossibleAccordingToTheRoleModel(
            String role) {

        Allure.description("Supervisor может создавать пользователя с ролью " + role);

        PlayerCreateRequestDto player = new PlayerCreateRequestDto(
                generateStringAgeDefault(),
                GENDER_M,
                generateUniqueName(LOGIN_NAME),
                generatePasswordDefault(),
                role,
                generateUniqueName(SCREEN_NAME));
        Response response = playerActions.createPlayer(LOGIN_SUPERVISOR, player);
        PlayerCreateResponseDto responseBody = response.as(PlayerCreateResponseDto.class);
        playerId = responseBody.getId();
        Assert.assertEquals(response.getStatusCode(), 200);
    }

    @AfterMethod
    public void deletingPlayers() {
        playerActions.deletePlayer(LOGIN_SUPERVISOR, playerId);
    }

}
