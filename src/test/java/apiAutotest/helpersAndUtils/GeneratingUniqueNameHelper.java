package apiAutotest.helpersAndUtils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Random;

public class GeneratingUniqueNameHelper {
    private static final Logger logger = LogManager.getLogger(GeneratingUniqueNameHelper.class);

    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    private static final int NAME_LENGTH = 7;
    private static final Random random = new Random();

    public static String generateUniqueName(String preName) {
        StringBuilder name = new StringBuilder();
        for (int i = 0; i < NAME_LENGTH; i++) {
            int index = random.nextInt(CHARACTERS.length());
            name.append(CHARACTERS.charAt(index));
        }
        String uniqueName = preName + "_" + name;
        logger.info("Сгенерированное " + preName + " : " + uniqueName);
        return uniqueName;
    }
}
