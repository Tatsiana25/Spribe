package apiAutotest.helpersAndUtils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Random;

public class GeneratingPasswordHelper {
    private static final Logger logger = LogManager.getLogger(GeneratingPasswordHelper.class);
    // Символы для генерации пароля
    private static final String LATIN_LETTERS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String NUMBERS = "0123456789";
    private static final String SPECIAL_CHARACTERS = "!@#$%^&*()_-+=<>?";
    private static final String CYRILLIC_LETTERS = "абвгдеёжзийклмнопрстуфхцчшщъыьэюя";

    public static String generatePassword(int length, boolean latinLetters, boolean numbers, boolean includeSpecialCharacters, boolean includeCyrillic) {

        StringBuilder characterPool = new StringBuilder();
        if (latinLetters) {
            characterPool.append(LATIN_LETTERS);
        }
        if (numbers) {
            characterPool.append(NUMBERS);
        }
        if (includeSpecialCharacters) {
            characterPool.append(SPECIAL_CHARACTERS);
        }
        if (includeCyrillic) {
            characterPool.append(CYRILLIC_LETTERS);
        }

        Random random = new Random();
        StringBuilder password = new StringBuilder();

        for (int i = 0; i < length; i++) {
            password.append(characterPool.charAt(random.nextInt(characterPool.length())));
        }
        logger.info("Сгенерированный password: " + password);
        return password.toString();
    }

    public static String generatePasswordDefault() {
        return generatePassword(10, true, true, false, false);
    }
}
