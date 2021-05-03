package edu.uta.cse5381.assignment3.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.math.BigInteger;
import java.security.PrivateKey;
import java.security.PublicKey;

@Data
public class UserInfo {

    private String username;
    private String password;
    private String aesKey;
    @JsonIgnore private PublicKey publicRSAKey;
    @JsonIgnore private PrivateKey privateRSAKey;

    private BigInteger module;
    private BigInteger publicExpo;
    private BigInteger privateExpo;

    private String rsaPublicKey;

}
