package apiAutotest.tests.playerController.create.validation;

import apiAutotest.actions.PlayerActions;
import apiAutotest.dataProviders.CreatePlayerDataProvider;
import apiAutotest.model.PlayerCreateRequestDto;
import apiAutotest.model.PlayerCreateResponseDto;
import io.qameta.allure.Allure;
import io.qameta.allure.Description;
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
public class CreatePlayerValidationTest {
    private PlayerActions playerActions;
    long playerId;

    @BeforeClass(description = "Инициализация Action-класса")
    public void setup() {
        playerActions = new PlayerActions();
    }


    @Description("При создании игрока с валидными данными возвращается response body в корректном формате")
    @Test(description = "При создании игрока с валидными данными возвращается response body в корректном формате")
    public void correctResponseBodyIsReturnedWhenValidPlayerIsCreated(){
        PlayerCreateRequestDto player = new PlayerCreateRequestDto(
                generateStringAgeDefault(),
                GENDER_M,
                generateUniqueName(LOGIN_NAME),
                generatePasswordDefault(),
                ROLE_ADMIN,
                generateUniqueName(SCREEN_NAME));
        Response response = playerActions.createPlayer(LOGIN_SUPERVISOR, player);
        Assert.assertNotNull(response, "Тело ответа не должно быть null");

        PlayerCreateResponseDto responseBody = response.as(PlayerCreateResponseDto.class);
        playerId = responseBody.getId();

        Assert.assertNotNull(responseBody.getAge(), "Age не должен быть null");
        Assert.assertNotNull(responseBody.getGender(), "Gender не должен быть null");
        Assert.assertNotNull(responseBody.getLogin(), "Login не должен быть null");
        Assert.assertNotNull(responseBody.getPassword(), "Password не должен быть null");
        Assert.assertNotNull(responseBody.getRole(), "Role не должен быть null");
        Assert.assertNotNull(responseBody.getScreenName(), "ScreenName не должен быть null");

        playerId = responseBody.getId();
        Assert.assertEquals(response.getStatusCode(), 200);
    }

    @Test(description = "Игрок не может быть создан без указания обязательных полей или если они пусты",
            dataProvider = "missingOrEmptyParamsData",
            dataProviderClass = CreatePlayerDataProvider.class, threadPoolSize = THREAD_COUNT)
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
        Response response = playerActions.createPlayer(editor, player);
        if (response.getStatusCode() == 200) {
            PlayerCreateResponseDto responseBody = response.as(PlayerCreateResponseDto.class);
            playerId = responseBody.getId();
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
        Response responseFirst = playerActions.createPlayer(LOGIN_SUPERVISOR, theFirstPlayer);
        PlayerCreateResponseDto responseBody = responseFirst.as(PlayerCreateResponseDto.class);
        playerId = responseBody.getId();
        // Тест
        PlayerCreateRequestDto theSecondPlayer = new PlayerCreateRequestDto(
                generateStringAgeDefault(),
                GENDER_M,
                login,
                generatePasswordDefault(),
                ROLE_ADMIN,
                generateUniqueName(SCREEN_NAME));
        Response responseSecond = playerActions.createPlayer(LOGIN_SUPERVISOR, theSecondPlayer);
        Assert.assertEquals(responseSecond.getStatusCode(), 400);
    }

    @AfterMethod
    public void deletingPlayers() {
        playerActions.deletePlayer(LOGIN_SUPERVISOR, playerId);
    }
}
