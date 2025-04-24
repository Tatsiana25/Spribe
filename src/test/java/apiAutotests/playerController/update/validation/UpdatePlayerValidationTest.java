package apiAutotests.playerController.update.validation;

import api.actions.PlayerActions;
import api.dataProviders.UpdatePlayerDataProvider;
import api.model.PlayerCreateRequestDto;
import api.model.PlayerCreateResponseDto;
import api.model.PlayerUpdateRequestDto;
import io.qameta.allure.Allure;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static api.helpersAndUtils.GeneratingAgeHelper.generateStringAgeDefault;
import static api.helpersAndUtils.GeneratingPasswordHelper.generatePasswordDefault;
import static api.helpersAndUtils.GeneratingUniqueNameHelper.generateUniqueName;
import static api.helpersAndUtils.Names.*;


@Epic("Api Tests")
@Feature("updatePlayer")
public class UpdatePlayerValidationTest {
    private static final ThreadLocal<PlayerActions> playerActions = ThreadLocal.withInitial(PlayerActions::new);
    private static final ThreadLocal<Long> playerId = ThreadLocal.withInitial(() -> null);
    private static final ThreadLocal<Long> adminId = ThreadLocal.withInitial(() -> null);


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
        adminId.set(responseBodyAdmin.getId());
    }


    @Test(description = "Нельзя сменить пароль игрока на невалидный",
            dataProvider = "invalidUpdatePasswordsData",
            dataProviderClass = UpdatePlayerDataProvider.class)
    public void playerUpdatingIsImpossibleWithInvalidPassword(String passwordDescription, String invalidPassword) {

        Allure.description("Нельзя обновить пароль игрока, если значение нового пароля: " + passwordDescription);

        PlayerUpdateRequestDto updatedPlayer = new PlayerUpdateRequestDto(
                null,
                null,
                null,
                invalidPassword,
                null
        );

        // Так надо для удаления в AfterMethod
        playerId.set(adminId.get());
        String id = String.valueOf(playerId.get());

        Response response = playerActions.get().updatePlayer(LOGIN_SUPERVISOR, id, updatedPlayer);
        Assert.assertEquals(response.getStatusCode(), 400);
    }


    @AfterMethod
    public void deletingMethodPlayers() {
        playerActions.get().deletePlayer(LOGIN_SUPERVISOR, playerId.get());
        playerId.remove();
        adminId.remove();
        playerActions.remove();
    }
}
