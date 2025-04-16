package apiAutotest.dataProviders;

import org.testng.annotations.DataProvider;

import static apiAutotest.helpersAndUtils.GeneratingAgeHelper.generateStringAgeDefault;
import static apiAutotest.helpersAndUtils.GeneratingPasswordHelper.generatePasswordDefault;
import static apiAutotest.helpersAndUtils.GeneratingUniqueNameHelper.generateUniqueName;
import static apiAutotest.helpersAndUtils.Names.*;

public class CreatePlayerDataProvider {

    @DataProvider(name = "validCreateRoleBySupervisorData", parallel = true)
    public static Object[][] validCreateRoleBySupervisorData() {
        return new Object[][]{
                // role создаваемого игрока
                {ROLE_ADMIN},
                {ROLE_USER}
        };
    }

    @DataProvider(name = "missingOrEmptyParamsData", parallel = true)
    public static Object[][] missingOrEmptyParamsData() {
        return new Object[][]{
                // первые 2 параметра нужны для отображения в отчёте/age/editor/gender/login/password/role/screenName
                {"age", "null", null, LOGIN_SUPERVISOR, GENDER_M, generateUniqueName(LOGIN_NAME), generatePasswordDefault(), ROLE_ADMIN,
                        generateUniqueName(SCREEN_NAME), 400},
                {"gender", "null", generateStringAgeDefault(), LOGIN_SUPERVISOR, null, generateUniqueName(LOGIN_NAME), generatePasswordDefault(), ROLE_ADMIN,
                        generateUniqueName(SCREEN_NAME), 400},
                {"login", "null", generateStringAgeDefault(), LOGIN_SUPERVISOR, GENDER_M, null, generatePasswordDefault(), ROLE_ADMIN,
                        generateUniqueName(SCREEN_NAME), 400},
                {"password", "null", generateStringAgeDefault(), LOGIN_SUPERVISOR, GENDER_M, generateUniqueName(LOGIN_NAME), null, ROLE_ADMIN,
                        generateUniqueName(SCREEN_NAME), 400},
                {"role", "null", generateStringAgeDefault(), LOGIN_SUPERVISOR, GENDER_M, generateUniqueName(LOGIN_NAME), generatePasswordDefault(), null,
                        generateUniqueName(SCREEN_NAME), 400},
                {"screenName", "null", generateStringAgeDefault(), LOGIN_SUPERVISOR, GENDER_M, generateUniqueName(LOGIN_NAME), generatePasswordDefault(), ROLE_ADMIN,
                        null, 400},
                {"age", "пустая строка", "", LOGIN_SUPERVISOR, GENDER_M, generateUniqueName(LOGIN_NAME), generatePasswordDefault(), ROLE_ADMIN,
                        generateUniqueName(SCREEN_NAME), 400},
                {"editor", "пустая строка", generateStringAgeDefault(), "", GENDER_M, generateUniqueName(LOGIN_NAME), generatePasswordDefault(), ROLE_ADMIN,
                        generateUniqueName(SCREEN_NAME), 404},
                {"gender", "пустая строка", generateStringAgeDefault(), LOGIN_SUPERVISOR, "", generateUniqueName(LOGIN_NAME), generatePasswordDefault(), ROLE_ADMIN,
                        generateUniqueName(SCREEN_NAME), 400},
                {"login", "пустая строка", generateStringAgeDefault(), LOGIN_SUPERVISOR, GENDER_M, "", generatePasswordDefault(), ROLE_ADMIN,
                        generateUniqueName(SCREEN_NAME), 400},
                {"password", "пустая строка", generateStringAgeDefault(), LOGIN_SUPERVISOR, GENDER_M, generateUniqueName(LOGIN_NAME), "", ROLE_ADMIN,
                        generateUniqueName(SCREEN_NAME), 400},
                {"role", "пустая строка", generateStringAgeDefault(), LOGIN_SUPERVISOR, GENDER_M, generateUniqueName(LOGIN_NAME), generatePasswordDefault(), "",
                        generateUniqueName(SCREEN_NAME), 400},
                {"screenName", "пустая строка", generateStringAgeDefault(), LOGIN_SUPERVISOR, GENDER_M, generateUniqueName(LOGIN_NAME), generatePasswordDefault(), ROLE_ADMIN,
                        "", 400}
        };
    }
}
