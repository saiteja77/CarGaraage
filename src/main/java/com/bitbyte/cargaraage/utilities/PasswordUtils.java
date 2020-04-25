package com.bitbyte.cargaraage.utilities;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

public class PasswordUtils {

    private static final char[] HEX_ARRAY = "0123456789ABCDEF".toCharArray();

    public static byte[] createSalt() {
        byte[] salt = new byte[20];
        SecureRandom random = new SecureRandom();
        random.nextBytes(salt);
        return salt;
    }

    public static byte[] generateSaltedHash(String password, String hashingAlgorithm, byte[] salt) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance(hashingAlgorithm);
        digest.reset();
        digest.update(salt);
        return digest.digest(password.getBytes());
    }

    public static String convertByteArrayToHexString(byte[] passwordHash) {
        char[] hexChars = new char[passwordHash.length * 2];
        for (int j = 0; j < passwordHash.length; j++) {
            int v = passwordHash[j] & 0xFF;
            hexChars[j * 2] = HEX_ARRAY[v >>> 4];
            hexChars[j * 2 + 1] = HEX_ARRAY[v & 0x0F];
        }
        return new String(hexChars);
    }

    public static boolean comparePasswords(String password, String hash, byte[] salt, String hashingAlgorithm) throws NoSuchAlgorithmException {
        String generatedHash = convertByteArrayToHexString(generateSaltedHash(password, hashingAlgorithm, salt));
        return hash.equals(generatedHash);
    }
}
