package edu.uta.cse5381.assignment3.controller;

import edu.uta.cse5381.assignment3.model.UserInfo;
import edu.uta.cse5381.assignment3.service.AESCryptoService;
import edu.uta.cse5381.assignment3.service.CryptoService;
import edu.uta.cse5381.assignment3.service.HashCryptoService;
import edu.uta.cse5381.assignment3.service.RSACryptoService;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.websocket.server.PathParam;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/crypto")
@CrossOrigin
@Slf4j
public class CryptoController {

    @Autowired @Setter CryptoService cryptoService;
    @Autowired @Setter AESCryptoService aesCryptoService;
    @Autowired @Setter RSACryptoService rsaCryptoService;
    @Autowired @Setter HashCryptoService hashCryptoService;

    private static final ConcurrentMap<String, UserInfo> USER_INFO_CONCURRENT_MAP = new ConcurrentHashMap<>();

    @PostMapping("/aesencrypt")
    public ResponseEntity<?> aesEncryption(@RequestBody String body, @RequestParam String user) throws IllegalBlockSizeException, InvalidKeyException, BadPaddingException, NoSuchAlgorithmException, NoSuchPaddingException {
        return ResponseEntity.ok(aesCryptoService.encrypt(body, USER_INFO_CONCURRENT_MAP.get(user)));
    }

    @PostMapping("/aesdecrypt")
    public ResponseEntity<?> aesDecryption(@RequestBody String body, @RequestParam String user) throws IllegalBlockSizeException, InvalidKeyException, BadPaddingException, NoSuchAlgorithmException, NoSuchPaddingException {
        return ResponseEntity.ok(aesCryptoService.decrypt(body, USER_INFO_CONCURRENT_MAP.get(user)));
    }

    @PostMapping("/rsaencrypt")
    public ResponseEntity<?> rsaEncryption(@RequestBody String body, @RequestParam String user) throws IllegalBlockSizeException, BadPaddingException, NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException {
        return ResponseEntity.ok(rsaCryptoService.encrypt(body, USER_INFO_CONCURRENT_MAP.get(user)));
    }

    @PostMapping("/rsadecrypt")
    public ResponseEntity<?> rsaDecryption(@RequestBody String body, @RequestParam String user) throws IllegalBlockSizeException, InvalidKeyException, BadPaddingException, NoSuchAlgorithmException, NoSuchPaddingException {
        return ResponseEntity.ok(rsaCryptoService.decrypt(body.getBytes(), USER_INFO_CONCURRENT_MAP.get(user)));
    }

    @GetMapping("/userinfo")
    public ResponseEntity<?> generateUser(@RequestParam String user) throws NoSuchAlgorithmException {
        return ResponseEntity.ok(USER_INFO_CONCURRENT_MAP.computeIfAbsent(user, usr -> {
            UserInfo userInfo = new UserInfo();
            userInfo.setUsername(usr);
            userInfo.setPassword(aesCryptoService.generateAESKey(8));
            userInfo.setAesKey(aesCryptoService.generateAESKey(16));

            userInfo.setRsaPublicKey(rsaCryptoService.generatePublicPrivateRSAKey(userInfo));

            return userInfo;
        }));
    }

    @GetMapping("/users")
    public ResponseEntity<?> getUsers(){
        return ResponseEntity.ok(USER_INFO_CONCURRENT_MAP.keySet());
    }

    @RequestMapping(value = "/encrypt-file/{type}/{encryptDecryptFlag}", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<?> uploadFile(@RequestParam("file") MultipartFile file, @PathVariable("type") String type, @PathVariable("encryptDecryptFlag") String encryptDecryptFlag, @RequestParam String user) throws IOException, IllegalBlockSizeException, InvalidKeyException, BadPaddingException, NoSuchAlgorithmException, NoSuchPaddingException {
        /*System.out.println(request.getInputStream());
        try (InputStream initialStream = new ByteArrayInputStream(file.getBytes());
             Reader targetReader = new InputStreamReader(initialStream);){
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }*/
        String text = new BufferedReader(
                new InputStreamReader(new ByteArrayInputStream(file.getBytes()), StandardCharsets.UTF_8))
                .lines()
                .collect(Collectors.joining("\n"));
        log.info("Content: " + text);
        if(type.equals("AES")){
            if(encryptDecryptFlag.equals("encrypt")){
                return ResponseEntity.ok(aesCryptoService.encrypt(text, USER_INFO_CONCURRENT_MAP.get(user)));
            }else if(encryptDecryptFlag.equals("decrypt")){
                return ResponseEntity.ok(aesCryptoService.decrypt(text, USER_INFO_CONCURRENT_MAP.get(user)));
            } else {
                return ResponseEntity.badRequest().body("Only Encrypt or Decrypt process allowed !!!");
            }

        } else if(type.equals("RSA")){
            if(encryptDecryptFlag.equals("encrypt")){
                return ResponseEntity.ok(rsaCryptoService.encrypt(text, USER_INFO_CONCURRENT_MAP.get(user)));
            }else if(encryptDecryptFlag.equals("decrypt")){
                return ResponseEntity.ok(rsaCryptoService.decrypt(file.getBytes(), USER_INFO_CONCURRENT_MAP.get(user)));
            } else {
                return ResponseEntity.badRequest().body("Only Encrypt or Decrypt process allowed !!!");
            }
        } else if(type.equals("HASH")){
            return ResponseEntity.ok(hashCryptoService.hashString(file.getBytes(), encryptDecryptFlag));
        } else {
            return ResponseEntity.badRequest().body("only AES/RSA supported !!! !!!");
        }
    }

}
