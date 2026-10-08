package com.rahimunisha.rahimunishamart.util;

import org.mindrot.jbcrypt.BCrypt;

/**
 * PasswordUtil: Secure password hashing using jBCrypt with configurable workload factor.
 * Satisfies Section 9 Security Checklist: never stores plaintext, never logs passwords.
 */
public class PasswordUtil {
    private static final int BCRYPT_LOG_ROUNDS = 10;

    private PasswordUtil() {
    }

    public static String hashPassword(String plainPassword) {
        if (plainPassword == null || plainPassword.isEmpty()) {
            throw new IllegalArgumentException("Password cannot be null or empty");
        }
        return BCrypt.hashpw(plainPassword, BCrypt.gensalt(BCRYPT_LOG_ROUNDS));
    }

    public static boolean checkPassword(String plainPassword, String hashedPassword) {
        if (plainPassword == null || hashedPassword == null || hashedPassword.isEmpty()) {
            return false;
        }
        try {
            return BCrypt.checkpw(plainPassword, hashedPassword);
        } catch (Exception e) {
            return false;
        }
    }
}
