import java.security.SecureRandom;

public class PasswordGenerator {

    private static final String UPPER = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String LOWER = "abcdefghijklmnopqrstuvwxyz";
    private static final String NUMBERS = "0123456789";
    private static final String SPECIAL = "!@#$%^&*()_-+=<>?/{}~|";

    // ⭐ Ambiguous set
    private static final String AMBIGUOUS = "0Oo1IlS5";

    private static final SecureRandom random = new SecureRandom();

    // ⭐ UPDATED METHOD SIGNATURE
    public static String generatePassword(int length, boolean useUpper, boolean useLower,
                                          boolean useNumbers, boolean useSpecial,
                                          boolean excludeAmbiguous,
                                          boolean noRepeat,
                                          boolean noSequence,
                                          boolean useCustom,
                                          String customCharacters) {

        // ⭐ STEP 4 — Updated character pool logic
        StringBuilder characterPool = new StringBuilder();

        if (useCustom) {
            // User-provided character set
            if (customCharacters == null || customCharacters.isEmpty()) {
                throw new IllegalArgumentException("Custom character set cannot be empty");
            }
            characterPool.append(customCharacters);

        } else {
            // Default character groups
            if (useUpper) characterPool.append(UPPER);
            if (useLower) characterPool.append(LOWER);
            if (useNumbers) characterPool.append(NUMBERS);
            if (useSpecial) characterPool.append(SPECIAL);
        }

        if (characterPool.length() == 0)
            throw new IllegalArgumentException("You must select at least one character type!");

        // ⭐ STEP 5 — Ambiguous filter only applies when NOT using custom characters
        if (excludeAmbiguous && !useCustom) {
            for (char c : AMBIGUOUS.toCharArray()) {
                int index;
                while ((index = characterPool.indexOf(String.valueOf(c))) != -1) {
                    characterPool.deleteCharAt(index);
                }
            }
        }

        StringBuilder password = new StringBuilder();

        for (int i = 0; i < length; i++) {

            int index = random.nextInt(characterPool.length());
            char nextChar = characterPool.charAt(index);

            // ⭐ No Repeating Characters Rule
            if (noRepeat) {
                while (password.length() > 0 &&
                        password.charAt(password.length() - 1) == nextChar) {

                    index = random.nextInt(characterPool.length());
                    nextChar = characterPool.charAt(index);
                }
            }

            // ⭐ No Sequential Characters Rule
            if (noSequence) {
                while (password.length() > 0 &&
                        isSequential(password.charAt(password.length() - 1), nextChar)) {

                    index = random.nextInt(characterPool.length());
                    nextChar = characterPool.charAt(index);
                }
            }

            password.append(nextChar);
        }

        return password.toString();
    }

    // ⭐ STEP 3 — Detect letter/number sequences
    private static boolean isSequential(char a, char b) {
        return (b == a + 1) || (b == a - 1);
    }
}
