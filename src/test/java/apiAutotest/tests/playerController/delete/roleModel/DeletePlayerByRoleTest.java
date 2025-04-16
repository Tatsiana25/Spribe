package apiAutotest.tests.playerController.delete.roleModel;

import apiAutotest.actions.PlayerActions;
import apiAutotest.dataProviders.DeletePlayerDataProvider;
import apiAutotest.model.PlayerCreateRequestDto;
import apiAutotest.model.PlayerCreateResponseDto;
import io.qameta.allure.Allure;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.testng.Tag;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static apiAutotest.config.TestConfig.THREAD_COUNT;
import static apiAutotest.helpersAndUtils.GeneratingAgeHelper.generateStringAgeDefault;
import static apiAutotest.helpersAndUtils.GeneratingPasswordHelper.generatePasswordDefault;
import static apiAutotest.helpersAndUtils.GeneratingUniqueNameHelper.generateUniqueName;
import static apiAutotest.helpersAndUtils.Names.*;

@Epic("Api Tests")
@Feature("deletePlayer")
@Tag("Player ControlLer")
public class DeletePlayerByRoleTest {
    private PlayerActions playerActions;
    long playerId;

    @BeforeClass
    public void setup() {
        // Инициализация Action-класса
        playerActions = new PlayerActions();
    }


    @Test(description = "Супервайзер может удалять игроков согласно ролевой модели",
            dataProvider = "validDeleteRoleRorSupervisorData", dataProviderClass = DeletePlayerDataProvider.class,
            threadPoolSize = THREAD_COUNT)
    public void deletingPlayerBySupervisorIsPossibleAccordingToTheRoleModel(String editor,
                                                                            String roleOfCreatedPlayer,
                                                                            int expectedStatusCode) {

        Allure.description("Супервайзер может удалить пользователя с ролью " + roleOfCreatedPlayer);

// Создание игрока (что бы удалить)
        PlayerCreateRequestDto player = new PlayerCreateRequestDto(generateStringAgeDefault(), GENDER_M, generateUniqueName(LOGIN_NAME), generatePasswordDefault(), roleOfCreatedPlayer, generateUniqueName(SCREEN_NAME));
        Response responseCreate = playerActions.createPlayer(LOGIN_SUPERVISOR, player);
        Assert.assertEquals(responseCreate.getStatusCode(), 200);
        PlayerCreateResponseDto responseBody = responseCreate.as(PlayerCreateResponseDto.class);
        playerId = responseBody.getId();

// Удаление игрока
        Response responseDelete = playerActions.deletePlayer(editor, playerId);
        Assert.assertEquals(responseDelete.getStatusCode(), expectedStatusCode);
    }


    @Test(description = "Супервайзера нельзя удалить согласно ролевой модели",
            dataProvider = "invalidDeleteRoleRorSupervisorData", dataProviderClass = DeletePlayerDataProvider.class,
            threadPoolSize = THREAD_COUNT)
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
            Response responseCreate = playerActions.createPlayer(LOGIN_SUPERVISOR, player);
            Assert.assertEquals(responseCreate.getStatusCode(), 200);
        }

// Удаление супервайзера
// Пытаемся удалить супервайзера (его id в playerId) созданным ранее игроком
        Response responseDelete = playerActions.deletePlayer(editor, playerId);
        Assert.assertEquals(responseDelete.getStatusCode(), expectedStatusCode);
    }
}
