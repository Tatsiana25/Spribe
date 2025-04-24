package apiAutotests.playerController.create.validation;

import api.actions.PlayerActions;
import api.dataProviders.CreatePlayerDataProvider;
import api.model.PlayerCreateRequestDto;
import api.model.PlayerCreateResponseDto;
import io.qameta.allure.Allure;
import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import static api.helpersAndUtils.GeneratingAgeHelper.generateStringAgeDefault;
import static api.helpersAndUtils.GeneratingPasswordHelper.generatePasswordDefault;
import static api.helpersAndUtils.GeneratingUniqueNameHelper.generateUniqueName;
import static api.helpersAndUtils.Names.*;


@Epic("Api Tests")
@Feature("createPlayer")
public class CreatePlayerValidationTest {
    private static final ThreadLocal<PlayerActions> playerActions = ThreadLocal.withInitial(PlayerActions::new);
    private static final ThreadLocal<Long> playerId = ThreadLocal.withInitial(() -> null);


    @Description("При создании игрока с валидными данными возвращается response body в корректном формате")
    @Test(description = "При создании игрока с валидными данными возвращается response body в корректном формате")
    public void correctResponseBodyIsReturnedWhenValidPlayerIsCreated() {
        SoftAssert softAssert = new SoftAssert();

        String age = generateStringAgeDefault();
        String gender = GENDER_M;
        String login = generateUniqueName(LOGIN_NAME);
        String password = generatePasswordDefault();
        String role = ROLE_ADMIN;
        String screenName = generateUniqueName(SCREEN_NAME);

        PlayerCreateRequestDto player = new PlayerCreateRequestDto(
                age,
                gender,
                login,
                password,
                role,
                screenName);
        Response response = playerActions.get().createPlayer(LOGIN_SUPERVISOR, player);
        softAssert.assertEquals(response.getStatusCode(), 200);
        softAssert.assertNotNull(response, "Тело ответа не должно быть null");

        PlayerCreateResponseDto responseBody = response.as(PlayerCreateResponseDto.class);
        playerId.set(responseBody.getId());

        // Проверяем, что полученный id не null
        softAssert.assertNotNull(responseBody.getId(), "Id игрока не должен быть null");
        // Проверяем, что значения полученных полей соответствуют значениям отправленных в запросе
        softAssert.assertEquals(responseBody.getAge(), age, "Age не соответствует установленному");
        softAssert.assertEquals(responseBody.getGender(), gender, "Gender не соответствует установленному");
        softAssert.assertEquals(responseBody.getLogin(), login, "Login не соответствует установленному");
        softAssert.assertEquals(responseBody.getPassword(), password, "Password не соответствует установленному");
        softAssert.assertEquals(responseBody.getRole(), role, "Role не соответствует установленному");
        softAssert.assertEquals(responseBody.getScreenName(), screenName, "ScreenName не соответствует установленному");
        softAssert.assertAll();
    }

    @Test(description = "Игрок не может быть создан без указания обязательных полей или если они пусты",
            dataProvider = "missingOrEmptyParamsData",
            dataProviderClass = CreatePlayerDataProvider.class)
    public void playerCreatingIsImpossibleWithoutRequiredParams(
            String param,
            String value,
            String age,
            String editor,
            String gender,
            String login,
            String password,
            String role,
            String screenName,
            int expectedStatusCode) {

        Allure.description("Игрок не может быть создан, если поле " + param + " = " + value);

        PlayerCreateRequestDto player = new PlayerCreateRequestDto(age,
                gender,
                login,
                password,
                role,
                screenName);
        Response response = playerActions.get().createPlayer(editor, player);

        if (response.getStatusCode() != expectedStatusCode) {
            PlayerCreateResponseDto responseBody = response.as(PlayerCreateResponseDto.class);
            playerId.set(responseBody.getId());
        }
        Assert.assertEquals(response.getStatusCode(), expectedStatusCode);
    }

    @Description("Игрок не может быть создан, если его логин неуникальный")
    @Test(description = "Игрок не может быть создан, если его логин неуникальный")
    public void playerCreatingIsImpossibleIfLoginNonUnique() {
        // Предусловие
        String login = generateUniqueName(LOGIN_NAME);
        PlayerCreateRequestDto theFirstPlayer = new PlayerCreateRequestDto(
                generateStringAgeDefault(),
                GENDER_F,
                login,
                generatePasswordDefault(),
                ROLE_ADMIN,
                generateUniqueName(SCREEN_NAME));
        Response responseFirst = playerActions.get().createPlayer(LOGIN_SUPERVISOR, theFirstPlayer);
        PlayerCreateResponseDto responseBody = responseFirst.as(PlayerCreateResponseDto.class);
        playerId.set(responseBody.getId());
        // Тест
        PlayerCreateRequestDto theSecondPlayer = new PlayerCreateRequestDto(
                generateStringAgeDefault(),
                GENDER_M,
                login,
                generatePasswordDefault(),
                ROLE_ADMIN,
                generateUniqueName(SCREEN_NAME));
        Response responseSecond = playerActions.get().createPlayer(LOGIN_SUPERVISOR, theSecondPlayer);
        Assert.assertEquals(responseSecond.getStatusCode(), 400);
    }

    @AfterMethod
    public void deletingPlayers() {
        if (playerId.get() != null) {
            playerActions.get().deletePlayer(LOGIN_SUPERVISOR, playerId.get());
        }
        playerId.remove();
        playerActions.remove();
    }
}
