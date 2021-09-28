package myfuture.gifticonhub.domain.member.service;

import java.security.NoSuchAlgorithmException;

public interface EncryptionService {
    public static final int SALT_SIZE = 16;
    public static final int NUMBUR_OF_ITERATIONS = 1000;

    public String hashing(String password, String salt, int numberOfIterations);

    public String getSalt();



}
