package apiAutotest.dataProviders;

import org.testng.annotations.DataProvider;

import static apiAutotest.helpersAndUtils.GeneratingPasswordHelper.generatePassword;

public class UpdatePlayerDataProvider {

    @DataProvider(name = "invalidUpdatePasswordsData", parallel = true)
    public static Object[][] invalidUpdatePasswordsData() {
        return new Object[][]{
                // description, невалидный password
                {"6 символов", generatePassword(6, true, true, false, false)},
                {"16 символов", generatePassword(16, true, true, false, false)},
                {"0 символов", generatePassword(0, true, true, false, false)},
                {"только латиница", generatePassword(10, true, false, false, false)},
                {"только цифры", generatePassword(10, false, true, false, false)},
                {"латиница и спецсимволы", generatePassword(10, true, false, true, false)},
                {"кириллица и цифры", generatePassword(10, false, true, false, true)},
        };
    }
}
