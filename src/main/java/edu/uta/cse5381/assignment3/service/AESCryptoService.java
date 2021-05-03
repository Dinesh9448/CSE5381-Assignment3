package edu.uta.cse5381.assignment3.service;

import edu.uta.cse5381.assignment3.model.UserInfo;
import org.springframework.stereotype.Service;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Base64;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class AESCryptoService {

    private static final String AES_ALGORITHM = "AES/ECB/PKCS5Padding";

    public String encrypt(String strToEncrypt, UserInfo userInfo) throws NoSuchPaddingException, NoSuchAlgorithmException, BadPaddingException, IllegalBlockSizeException, InvalidKeyException {
        SecretKeySpec secretKey = getSecretKeySpec(userInfo.getAesKey());

        Cipher cipher = Cipher.getInstance(AES_ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);

        return Base64.getEncoder().encodeToString(cipher.doFinal(strToEncrypt.getBytes(StandardCharsets.UTF_8)));
    }

    public String decrypt(String strToDecrypt, UserInfo userInfo) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        SecretKeySpec secretKey = getSecretKeySpec(userInfo.getAesKey());

        Cipher cipher = Cipher.getInstance(AES_ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, secretKey);

        return new String(cipher.doFinal(Base64.getDecoder().decode(strToDecrypt)));
    }

    private SecretKeySpec getSecretKeySpec(String secret) throws NoSuchAlgorithmException {
        MessageDigest messageDigest = MessageDigest.getInstance("SHA-1");
        byte[] key = Arrays.copyOf(messageDigest.digest(secret.getBytes(StandardCharsets.UTF_8)), 16);
        SecretKeySpec secretKey = new SecretKeySpec(key, "AES");
        return secretKey;
    }

    public String generateAESKey(int i){

        byte[] bytearray = new byte[256];
        new Random().nextBytes(bytearray);
        String string = new String(bytearray, StandardCharsets.UTF_8);
        AtomicInteger atomicInteger = new AtomicInteger();

        StringBuilder stringBuilder = new StringBuilder();
        string.chars().filter(n -> ((n >= 'a' && n <= 'z') || (n >= '0' && n <= '9')) && (atomicInteger.incrementAndGet() < i)).forEach(c -> {
            stringBuilder.append(Character.valueOf((char) c));
        });
        return stringBuilder.toString();
    }
}
