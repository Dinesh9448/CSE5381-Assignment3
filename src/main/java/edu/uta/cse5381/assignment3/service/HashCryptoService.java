package edu.uta.cse5381.assignment3.service;

import org.springframework.stereotype.Service;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@Service
public class HashCryptoService {

    public String hashString(byte[] content, String type) throws NoSuchAlgorithmException {
        MessageDigest messageDigest = MessageDigest.getInstance(type);
        byte[] byteArray = new byte[1024];
        int bytesCount = 0;

        messageDigest.update(content);
        byte[] bytes = messageDigest.digest();

        StringBuilder sb = new StringBuilder();
        for (byte aByte : bytes) {
            sb.append(Integer.toString((aByte & 0xff) + 0x100, 16).substring(1));
        }

        return sb.toString();
    }
}
