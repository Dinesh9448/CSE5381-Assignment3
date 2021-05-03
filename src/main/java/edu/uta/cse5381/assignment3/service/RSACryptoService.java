package edu.uta.cse5381.assignment3.service;

import edu.uta.cse5381.assignment3.model.UserInfo;
import edu.uta.cse5381.assignment3.util.rsa.*;
import org.springframework.stereotype.Service;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.ByteArrayOutputStream;
import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

@Service
public class RSACryptoService {

    private static final String RSA_ALGORITHM = "RSA";

    /*public String generatePublicPrivateRSAKey(UserInfo userInfo) throws NoSuchAlgorithmException {
        KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
        keyGen.initialize(1024);
        KeyPair pair = keyGen.generateKeyPair();
        userInfo.setPrivateRSAKey(pair.getPrivate());
        userInfo.setPublicRSAKey(pair.getPublic());

        return Base64.getEncoder().encodeToString(pair.getPublic().getEncoded());
    }

    public byte[] encrypt(String plainText, UserInfo userInfo) throws InvalidKeyException, BadPaddingException, IllegalBlockSizeException, NoSuchPaddingException, NoSuchAlgorithmException {
        Cipher cipher = Cipher.getInstance(RSA_ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, userInfo.getPublicRSAKey());
        return cipher.doFinal(plainText.getBytes()) ;
    }

    public String decrypt(byte[] cipherTextArray, UserInfo userInfo) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        Cipher cipher = Cipher.getInstance(RSA_ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, userInfo.getPrivateRSAKey());
        byte[] decryptedTextArray = cipher.doFinal(cipherTextArray);
        return new String(decryptedTextArray);
    }*/


    public String generatePublicPrivateRSAKey(UserInfo userInfo){
        RSAKeyGenerator keygen = new RSAKeyGenerator();
        RSAPublicKey publicKey = (RSAPublicKey)keygen.makeKey(RSAKey.PUBLIC_KEY);
        RSAPrivateKey privateKey = (RSAPrivateKey)keygen.makeKey(RSAKey.PRIVATE_KEY);
        userInfo.setModule(publicKey.getModulus());
        userInfo.setPublicExpo(publicKey.getPubExp());
        userInfo.setPrivateExpo(privateKey.getPriExp());
        return null;
    }

    public byte[] encrypt(String plainText, UserInfo userInfo){
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        RSAPublicKey rsaPublicKey = new RSAPublicKey(userInfo.getModule(), userInfo.getPublicExpo());
        rsaPublicKey.use(plainText, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }

    public String decrypt(byte[] text, UserInfo userInfo){
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        RSAPrivateKey rsaPrivateKey = new RSAPrivateKey(userInfo.getModule(), userInfo.getPrivateExpo());
        rsaPrivateKey.use(text, byteArrayOutputStream);
        return byteArrayOutputStream.toString();
    }
}
