package Utility;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

public class PasswordHashUtility {

    public static String hashPassword(char[] password, String gensalt) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
    
            byte[] salt;
            if (gensalt != null && !gensalt.isEmpty()) {
                salt = Base64.getDecoder().decode(gensalt); // Use the provided salt
            } 
            else {
                SecureRandom random = new SecureRandom();
                salt = new byte[16];
                random.nextBytes(salt); // Generate a new salt if not provided
            }
    
            byte[] saltedPasswordBytes = concatenateBytes(salt, new String(password).getBytes());
            md.update(saltedPasswordBytes);
    
            byte[] hashedPasswordBytes = md.digest();
    
            StringBuilder hexStringBuilder = new StringBuilder();
            for (byte b : hashedPasswordBytes) {
                hexStringBuilder.append(String.format("%02x", b));
            }
            return hexStringBuilder.toString();
        } 
        catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static byte[] concatenateBytes(byte[] arr1, byte[] arr2) {
        byte[] combined = new byte[arr1.length + arr2.length];
        System.arraycopy(arr1, 0, combined, 0, arr1.length);
        System.arraycopy(arr2, 0, combined, arr1.length, arr2.length);
        return combined;
    }
}
