package api.dataProviders;

import org.testng.annotations.DataProvider;

import static api.helpersAndUtils.Names.*;

public class DeletePlayerDataProvider {

    @DataProvider(name = "validDeleteRoleRorSupervisorData")
    public static Object[][] validDeleteRoleRorSupervisorData() {
        return new Object[][]{
                // Логин эдитора (который будет удалять)/роль созданного игрока (для удаления)/код
                {ROLE_SUPERVISOR, ROLE_ADMIN, 204},
                {ROLE_SUPERVISOR, ROLE_USER, 204}
        };
    }

    @DataProvider(name = "invalidDeleteRoleRorSupervisorData")
    public static Object[][] invalidDeleteRoleRorSupervisorData() {
        return new Object[][]{
                // Роль эдитора (который будет удалять)/id игрока (для удаления)/код
                {ROLE_SUPERVISOR, 1, 403},
                {ROLE_ADMIN, 1, 403},
                {ROLE_USER, 1, 403}
        };
    }
}
