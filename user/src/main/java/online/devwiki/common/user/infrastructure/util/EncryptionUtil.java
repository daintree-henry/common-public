package online.devwiki.common.user.infrastructure.util;

import at.favre.lib.crypto.bcrypt.BCrypt;

public class EncryptionUtil {

    public static String encrypt(String password) {
        return BCrypt.withDefaults().hashToString(BCrypt.MIN_COST, password.toCharArray());
    }

    public static boolean match(String password, String hashedPassword) {
        return BCrypt.verifyer().verify(password.toCharArray(), hashedPassword).verified;
    }
}