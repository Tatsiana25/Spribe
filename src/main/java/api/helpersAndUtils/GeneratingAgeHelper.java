package api.helpersAndUtils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.util.Random;

public class GeneratingAgeHelper {
    private static final Logger logger = LogManager.getLogger(GeneratingAgeHelper.class);

    private static int generateAge(int minAge, int maxAge) {
        Random random = new Random();
        int age = random.nextInt((maxAge - minAge) + 1) + minAge;
        logger.info("Сгенерированный возраст: " + age);
        return age;
    }

    //Для создания игрока в поле возраста ожидается String
    public static String generateStringAgeDefault() {
        return String.valueOf(generateAge(17, 59));
    }

    public static int generateIntAgeDefault() {
        return generateAge(17, 59);
    }
}
