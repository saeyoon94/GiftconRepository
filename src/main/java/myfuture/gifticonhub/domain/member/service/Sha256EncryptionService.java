package myfuture.gifticonhub.domain.member.service;

import org.springframework.stereotype.Service;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

@Service
public class Sha256EncryptionService implements EncryptionService {

    @Override
    public String hashing(String password, String salt, int numberOfIterations) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");

            //stretching
            for (int i = 0; i < numberOfIterations; i++) {
                String tempPassword = password + salt;
                md.update(tempPassword.getBytes());
                password = bytesToHex(md.digest());
            }

            return password;
        } catch (NoSuchAlgorithmException e) {
            return "";
        }
    }


    @Override
    public String getSalt() {
        SecureRandom secureRandom = new SecureRandom();
        byte[] salt = new byte[SALT_SIZE];
        secureRandom.nextBytes(salt);
        return salt.toString();
    }

    private String bytesToHex(byte[] bytes) {
        StringBuilder builder = new StringBuilder();
        for (byte b : bytes) {
            builder.append(String.format("%02x", b));
        }
        return builder.toString();
    }
}
