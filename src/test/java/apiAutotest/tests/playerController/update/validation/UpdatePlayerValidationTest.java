package apiAutotest.tests.playerController.update.validation;

import apiAutotest.actions.PlayerActions;
import apiAutotest.dataProviders.UpdatePlayerDataProvider;
import apiAutotest.model.PlayerCreateRequestDto;
import apiAutotest.model.PlayerCreateResponseDto;
import apiAutotest.model.PlayerUpdateRequestDto;
import io.qameta.allure.Allure;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.restassured.response.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static apiAutotest.config.TestConfig.THREAD_COUNT;
import static apiAutotest.helpersAndUtils.GeneratingAgeHelper.generateStringAgeDefault;
import static apiAutotest.helpersAndUtils.GeneratingPasswordHelper.generatePasswordDefault;
import static apiAutotest.helpersAndUtils.GeneratingUniqueNameHelper.generateUniqueName;
import static apiAutotest.helpersAndUtils.Names.*;


@Epic("Api Tests")
@Feature("updatePlayer")
public class UpdatePlayerValidationTest {
    private static final Logger logger = LogManager.getLogger(UpdatePlayerValidationTest.class);
    //private PlayerActions playerActions;
    private static final ThreadLocal<PlayerActions> playerActions = ThreadLocal.withInitial(PlayerActions::new);

    //long playerId;
    private static final ThreadLocal<Long> playerId = ThreadLocal.withInitial(() -> null);
    //String screenName = generateUniqueName(SCREEN_NAME);
    //String password = generatePasswordDefault();
    //String loginAdmin = generateUniqueName(LOGIN_NAME);
    //long adminId;
    private static final ThreadLocal<Long> adminId = ThreadLocal.withInitial(() -> null);


    @BeforeClass
    public void setup() {
        // Инициализация Action-класса
        //playerActions = new PlayerActions();
        playerActions.set(new PlayerActions());
    }

    @BeforeMethod
    public void creatingValidAdmin() {
// Создание админа
        PlayerCreateRequestDto playerAdmin = new PlayerCreateRequestDto(
                generateStringAgeDefault(),
                GENDER_M,
                generateUniqueName(LOGIN_NAME),
                generatePasswordDefault(),
                ROLE_ADMIN,
                generateUniqueName(SCREEN_NAME)
        );
        Response responseAdmin = playerActions.get().createPlayer(LOGIN_SUPERVISOR, playerAdmin);
        PlayerCreateResponseDto responseBodyAdmin = responseAdmin.as(PlayerCreateResponseDto.class);
        //adminId = responseBodyAdmin.getId();
        adminId.set(responseBodyAdmin.getId());
    }


    @Test(description = "Нельзя сменить пароль игрока на невалидный",
            dataProvider = "invalidUpdatePasswordsData",
            dataProviderClass = UpdatePlayerDataProvider.class,
            threadPoolSize = THREAD_COUNT)
    public void playerUpdatingIsImpossibleWithInvalidPassword(String passwordDescription, String invalidPassword) {

        Allure.description("Нельзя обновить пароль игрока, если значение нового пароля: " + passwordDescription);
        PlayerActions playerActions = new PlayerActions();

        PlayerUpdateRequestDto updatedPlayer = new PlayerUpdateRequestDto(
                null,
                null,
                null,
                invalidPassword,
                null
        );

        // Так надо для удаления в AfterMethod
        //playerId = adminId;
        playerId.set(adminId.get());
        //String id = String.valueOf(playerId);
        String id = String.valueOf(playerId.get());

        Response response = playerActions.updatePlayer(LOGIN_SUPERVISOR, id, updatedPlayer);
//        logger.info(playerActions.getPlayerByPlayerId(playerId));
        Assert.assertEquals(response.getStatusCode(), 400);
    }


    @AfterMethod
    public void deletingMethodPlayers() {
        //playerActions.deletePlayer(LOGIN_SUPERVISOR, playerId);
        playerActions.get().deletePlayer(LOGIN_SUPERVISOR, playerId.get());
        playerId.remove();
        adminId.remove();
        playerActions.remove();
    }
}
