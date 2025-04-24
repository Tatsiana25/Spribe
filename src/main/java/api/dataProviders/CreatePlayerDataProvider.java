package api.dataProviders;

import api.helpersAndUtils.GeneratingAgeHelper;
import api.helpersAndUtils.GeneratingUniqueNameHelper;
import org.testng.annotations.DataProvider;

import static api.helpersAndUtils.GeneratingPasswordHelper.generatePasswordDefault;
import static api.helpersAndUtils.Names.*;

public class CreatePlayerDataProvider {

    @DataProvider(name = "validCreateRoleBySupervisorData")
    public static Object[][] validCreateRoleBySupervisorData() {
        return new Object[][]{
                // role создаваемого игрока
                {ROLE_ADMIN},
                {ROLE_USER}
        };
    }

    @DataProvider(name = "missingOrEmptyParamsData")
    public static Object[][] missingOrEmptyParamsData() {
        return new Object[][]{
                // первые 2 параметра нужны для отображения в отчёте/age/editor/gender/login/password/role/screenName
                {"age", "null", null, LOGIN_SUPERVISOR, GENDER_M, GeneratingUniqueNameHelper.generateUniqueName(LOGIN_NAME), generatePasswordDefault(), ROLE_ADMIN,
                        GeneratingUniqueNameHelper.generateUniqueName(SCREEN_NAME), 400},
                {"gender", "null", GeneratingAgeHelper.generateStringAgeDefault(), LOGIN_SUPERVISOR, null, GeneratingUniqueNameHelper.generateUniqueName(LOGIN_NAME), generatePasswordDefault(), ROLE_ADMIN,
                        GeneratingUniqueNameHelper.generateUniqueName(SCREEN_NAME), 400},
                {"login", "null", GeneratingAgeHelper.generateStringAgeDefault(), LOGIN_SUPERVISOR, GENDER_M, null, generatePasswordDefault(), ROLE_ADMIN,
                        GeneratingUniqueNameHelper.generateUniqueName(SCREEN_NAME), 400},
                {"password", "null", GeneratingAgeHelper.generateStringAgeDefault(), LOGIN_SUPERVISOR, GENDER_M, GeneratingUniqueNameHelper.generateUniqueName(LOGIN_NAME), null, ROLE_ADMIN,
                        GeneratingUniqueNameHelper.generateUniqueName(SCREEN_NAME), 400},
                {"role", "null", GeneratingAgeHelper.generateStringAgeDefault(), LOGIN_SUPERVISOR, GENDER_M, GeneratingUniqueNameHelper.generateUniqueName(LOGIN_NAME), generatePasswordDefault(), null,
                        GeneratingUniqueNameHelper.generateUniqueName(SCREEN_NAME), 400},
                {"screenName", "null", GeneratingAgeHelper.generateStringAgeDefault(), LOGIN_SUPERVISOR, GENDER_M, GeneratingUniqueNameHelper.generateUniqueName(LOGIN_NAME), generatePasswordDefault(), ROLE_ADMIN,
                        null, 400},
                {"age", "пустая строка", "", LOGIN_SUPERVISOR, GENDER_M, GeneratingUniqueNameHelper.generateUniqueName(LOGIN_NAME), generatePasswordDefault(), ROLE_ADMIN,
                        GeneratingUniqueNameHelper.generateUniqueName(SCREEN_NAME), 400},
                {"editor", "пустая строка", GeneratingAgeHelper.generateStringAgeDefault(), "", GENDER_M, GeneratingUniqueNameHelper.generateUniqueName(LOGIN_NAME), generatePasswordDefault(), ROLE_ADMIN,
                        GeneratingUniqueNameHelper.generateUniqueName(SCREEN_NAME), 404},
                {"gender", "пустая строка", GeneratingAgeHelper.generateStringAgeDefault(), LOGIN_SUPERVISOR, "", GeneratingUniqueNameHelper.generateUniqueName(LOGIN_NAME), generatePasswordDefault(), ROLE_ADMIN,
                        GeneratingUniqueNameHelper.generateUniqueName(SCREEN_NAME), 400},
                {"login", "пустая строка", GeneratingAgeHelper.generateStringAgeDefault(), LOGIN_SUPERVISOR, GENDER_M, "", generatePasswordDefault(), ROLE_ADMIN,
                        GeneratingUniqueNameHelper.generateUniqueName(SCREEN_NAME), 400},
                {"password", "пустая строка", GeneratingAgeHelper.generateStringAgeDefault(), LOGIN_SUPERVISOR, GENDER_M, GeneratingUniqueNameHelper.generateUniqueName(LOGIN_NAME), "", ROLE_ADMIN,
                        GeneratingUniqueNameHelper.generateUniqueName(SCREEN_NAME), 400},
                {"role", "пустая строка", GeneratingAgeHelper.generateStringAgeDefault(), LOGIN_SUPERVISOR, GENDER_M, GeneratingUniqueNameHelper.generateUniqueName(LOGIN_NAME), generatePasswordDefault(), "",
                        GeneratingUniqueNameHelper.generateUniqueName(SCREEN_NAME), 400},
                {"screenName", "пустая строка", GeneratingAgeHelper.generateStringAgeDefault(), LOGIN_SUPERVISOR, GENDER_M, GeneratingUniqueNameHelper.generateUniqueName(LOGIN_NAME), generatePasswordDefault(), ROLE_ADMIN,
                        "", 400}
        };
    }
}
