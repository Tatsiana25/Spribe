package apiAutotests.playerController.create.roleModel;

import api.actions.PlayerActions;
import api.dataProviders.CreatePlayerDataProvider;
import api.model.PlayerCreateRequestDto;
import api.model.PlayerCreateResponseDto;
import api.model.PlayerGetByPlayerIdResponseDto;
import io.qameta.allure.Allure;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.restassured.response.Response;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import static api.helpersAndUtils.GeneratingAgeHelper.generateStringAgeDefault;
import static api.helpersAndUtils.GeneratingPasswordHelper.generatePasswordDefault;
import static api.helpersAndUtils.GeneratingUniqueNameHelper.generateUniqueName;
import static api.helpersAndUtils.Names.*;

@Epic("Api Tests")
@Feature("createPlayer")
public class CreatePlayerBySupervisorTest {
    private static final ThreadLocal<PlayerActions> playerActions = ThreadLocal.withInitial(PlayerActions::new);
    private static final ThreadLocal<Long> playerId = ThreadLocal.withInitial(() -> null);


    @Test(description = "Supervisor успешно создаёт игроков согласно ролевой модели", dataProvider = "validCreateRoleBySupervisorData",
            dataProviderClass = CreatePlayerDataProvider.class)
    public void creatingPlayerBySupervisorIsPossibleAccordingToTheRoleModel(
            String role) {

        Allure.description("Supervisor может создавать пользователя с ролью " + role);

        SoftAssert softAssert = new SoftAssert();

        // Создание игрока
        String login = generateUniqueName(LOGIN_NAME);
        PlayerCreateRequestDto player = new PlayerCreateRequestDto(
                generateStringAgeDefault(),
                GENDER_M,
                login,
                generatePasswordDefault(),
                role,
                generateUniqueName(SCREEN_NAME));
        Response response = playerActions.get().createPlayer(LOGIN_SUPERVISOR, player);
        PlayerCreateResponseDto responseBody = response.as(PlayerCreateResponseDto.class);
        playerId.set(responseBody.getId());
        softAssert.assertEquals(response.getStatusCode(), 200);

        // Проверка того, что игрок действительно создан
        Response getResponse = playerActions.get().getPlayerByPlayerId(playerId.get());
        softAssert.assertEquals(getResponse.getStatusCode(), 200);
        PlayerGetByPlayerIdResponseDto getResponseBody = getResponse.as(PlayerGetByPlayerIdResponseDto.class);
        softAssert.assertEquals(getResponseBody.getLogin(), login, "Login полученного и созданного игроков не совпадают");

        softAssert.assertAll();
    }

    @AfterMethod
    public void deletingPlayers() {
        playerActions.get().deletePlayer(LOGIN_SUPERVISOR, playerId.get());
        playerId.remove();
        playerActions.remove();
    }
}
