package api.dataProviders;

import org.testng.annotations.DataProvider;

public class GetPlayerByPlayerIdDataProvider {

    @DataProvider(name = "invalidPlayerIdData")
    public static Object[][] invalidPlayerIdData() {
        return new Object[][]{
                //подставлять в названии теста/в метод/статус код
                {"не существует в БД", 2, 404},
                {"NULL", null, 400},
        };
    }
}
