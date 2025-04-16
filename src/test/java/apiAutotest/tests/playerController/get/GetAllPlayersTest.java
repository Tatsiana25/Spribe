package apiAutotest.tests.playerController.get;

import apiAutotest.actions.PlayerActions;
import apiAutotest.model.PlayerGetAllResponseDto;
import apiAutotest.model.PlayerItem;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;


@Epic("Api Tests")
@Feature("getAllPlayers")
public class GetAllPlayersTest {
    private PlayerActions playerActions;

    @BeforeClass
    public void setup() {
        // Инициализация Action-класса
        playerActions = new PlayerActions();
    }

    @Test(description = "Можно получить всех игроков")
    public void getAllPlayersIsPossible() {
        Response response = playerActions.getAllPlayers();
        Assert.assertEquals(response.getStatusCode(), 200);

        PlayerGetAllResponseDto responseBody = response.as(PlayerGetAllResponseDto.class);

        Assert.assertNotNull(responseBody, "Тело ответа не должно быть null");
        Assert.assertNotNull(responseBody.getPlayers(), "Список игроков не должен быть null");
        Assert.assertFalse(responseBody.getPlayers().isEmpty(), "Список игроков не должен быть пустым");

        PlayerItem firstPlayer = responseBody.getPlayers().get(0);
        Assert.assertNotNull(firstPlayer.getScreenName(), "Имя игрока не должно быть null");
    }
}
