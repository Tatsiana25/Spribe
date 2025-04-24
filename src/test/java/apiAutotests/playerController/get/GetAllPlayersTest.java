package apiAutotests.playerController.get;

import api.actions.PlayerActions;
import api.model.PlayerGetAllResponseDto;
import api.model.PlayerItem;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;


@Epic("Api Tests")
@Feature("getAllPlayers")
public class GetAllPlayersTest {
    private PlayerActions playerActions;

    @BeforeClass(description = "Инициализация Action-класса")
    public void setup() {
        playerActions = new PlayerActions();
    }

    @Test(description = "Можно получить всех игроков")
    public void getAllPlayersIsPossible() {
        SoftAssert softAssert = new SoftAssert();

        Response response = playerActions.getAllPlayers();
        Assert.assertEquals(response.getStatusCode(), 200);

        PlayerGetAllResponseDto responseBody = response.as(PlayerGetAllResponseDto.class);

        softAssert.assertNotNull(responseBody, "Тело ответа не должно быть null");
        softAssert.assertNotNull(responseBody.getPlayers(), "Список игроков не должен быть null");
        softAssert.assertFalse(responseBody.getPlayers().isEmpty(), "Список игроков не должен быть пустым");

        PlayerItem firstPlayer = responseBody.getPlayers().get(0);
        softAssert.assertNotNull(firstPlayer.getScreenName(), "Имя игрока не должно быть null");
        softAssert.assertAll();
    }
}
