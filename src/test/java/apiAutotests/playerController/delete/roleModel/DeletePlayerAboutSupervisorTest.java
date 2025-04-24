package apiAutotests.playerController.delete.roleModel;

import api.actions.PlayerActions;
import api.dataProviders.DeletePlayerDataProvider;
import api.model.PlayerCreateRequestDto;
import api.model.PlayerCreateResponseDto;
import io.qameta.allure.Allure;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.testng.Tag;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;

import static api.helpersAndUtils.GeneratingAgeHelper.generateStringAgeDefault;
import static api.helpersAndUtils.GeneratingPasswordHelper.generatePasswordDefault;
import static api.helpersAndUtils.GeneratingUniqueNameHelper.generateUniqueName;
import static api.helpersAndUtils.Names.*;
import static org.testng.Assert.assertEquals;

@Epic("Api Tests")
@Feature("deletePlayer")
@Tag("Player ControlLer")
public class DeletePlayerAboutSupervisorTest {
    private static final ThreadLocal<PlayerActions> playerActions = ThreadLocal.withInitial(PlayerActions::new);
    private static final ThreadLocal<Long> playerId = ThreadLocal.withInitial(() -> null);


    @Test(description = "Супервайзер может удалять игроков согласно ролевой модели",
            dataProvider = "validDeleteRoleRorSupervisorData", dataProviderClass = DeletePlayerDataProvider.class)
    public void deletingPlayerBySupervisorIsPossibleAccordingToTheRoleModel(String editor,
                                                                            String roleOfCreatedPlayer,
                                                                            int expectedStatusCode) {

        Allure.description("Супервайзер может удалить пользователя с ролью " + roleOfCreatedPlayer);

// Создание игрока (что бы удалить)
        PlayerCreateRequestDto player = new PlayerCreateRequestDto(generateStringAgeDefault(),
                GENDER_M,
                generateUniqueName(LOGIN_NAME),
                generatePasswordDefault(),
                roleOfCreatedPlayer,
                generateUniqueName(SCREEN_NAME));
        Response responseCreate = playerActions.get().createPlayer(LOGIN_SUPERVISOR, player);
        Assert.assertEquals(responseCreate.getStatusCode(), 200);
        PlayerCreateResponseDto responseBody = responseCreate.as(PlayerCreateResponseDto.class);
        playerId.set(responseBody.getId());

// Удаление игрока
        Response responseDelete = playerActions.get().deletePlayer(editor, playerId.get());
        Assert.assertEquals(responseDelete.getStatusCode(), expectedStatusCode);

// Подтверждение того, что игрок действительно удалён
        Response responseGet = playerActions.get().getPlayerByPlayerId(playerId.get());
        assertEquals(responseGet.getStatusCode(), 404, "Неверный статус при запросе удалённого игрока");
    }


    @Test(description = "Супервайзера нельзя удалить согласно ролевой модели",
            dataProvider = "invalidDeleteRoleRorSupervisorData", dataProviderClass = DeletePlayerDataProvider.class)
    public void deletingOfSupervisorIsImpossibleAccordingToTheRoleModel(String roleOfCreatedPlayer,
                                                                        long playerId,
                                                                        int expectedStatusCode) {
        Allure.description(roleOfCreatedPlayer + " не может удалить пользователя с ролью " + ROLE_SUPERVISOR);

// Создание игрока (который будет удалять)
// Делаем логин игрока, который будет удалять. Если роль удаляющего пользователя = supervisor, делаем логин "supervisor",
// иначе, генерим ему уникальный логин.
// Если сделали логин "supervisor", скипаем создание игрока,
// если логин другой, делаем игрока с ролью из параметра roleOfCreatedPlayer (admin/user)
        String editor = roleOfCreatedPlayer.equals(ROLE_SUPERVISOR) ? LOGIN_SUPERVISOR : generateUniqueName(LOGIN_NAME);
        if (!editor.equals(LOGIN_SUPERVISOR)) {
            PlayerCreateRequestDto player = new PlayerCreateRequestDto(
                    generateStringAgeDefault(),
                    GENDER_M,
                    editor,
                    generatePasswordDefault(),
                    roleOfCreatedPlayer,
                    generateUniqueName(SCREEN_NAME));
            Response responseCreate = playerActions.get().createPlayer(LOGIN_SUPERVISOR, player);
            assertEquals(responseCreate.getStatusCode(), 200);
        }

// Удаление супервайзера
// Пытаемся удалить супервайзера (его id в playerId) созданным ранее игроком
        Response responseDelete = playerActions.get().deletePlayer(editor, playerId);
        assertEquals(responseDelete.getStatusCode(), expectedStatusCode);
    }

    @AfterMethod
    public void cleanup() {
        playerId.remove();
        playerActions.remove();
    }
}
