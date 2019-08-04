package com.leyou.auth.utils;


import com.leyou.auth.entity.UserInfo;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.security.PrivateKey;
import java.security.PublicKey;

@SpringBootTest
@RunWith(SpringRunner.class)
public class JwtUtilsTest {

    private static final String pubKeyPath = "D:\\rsa\\rsa.pub";

    private static final String priKeyPath = "D:\\rsa\\rsa.pri";

    private PublicKey publicKey;

    private PrivateKey privateKey;

    @Test
    public void testRsa() throws Exception {
        RsaUtils.generateKey(pubKeyPath, priKeyPath, "234");
    }

    @Before
    public void testGetRsa() throws Exception {
        this.publicKey = RsaUtils.getPublicKey(pubKeyPath);
        this.privateKey = RsaUtils.getPrivateKey(priKeyPath);
    }

    @Test
    public void testGenerateToken() throws Exception {
        // 生成token
        String token = JwtUtils.generateToken(new UserInfo(20L, "jack"), privateKey, 5);
        System.out.println("token = " + token);
    }

    @Test
    public void testParseToken() throws Exception {
        String token = "eyJhbGciOiJSUzI1NiJ9.eyJpZCI6MjAsInVzZXJuYW1lIjoiamFjayIsImV4cCI6MTU2NDcyNDQ1M30.S4olVJJeJten_wC2souC7_F4XaPP6Ktjsp0s7_7Bjlw6U_2Gy9BQVaDJaJiIcsY6SgetVssxeTheLsM1HY36UldapwZ53IHXi6RK8OTLKsj24cmQ5upwCNFbvcIj9lGYmG9IDqw0UcfXelGNSewjHhbdKvyqgVsnBiqdCp4ehKI";
        // 解析token
        UserInfo user = JwtUtils.getInfoFromToken(token, publicKey);
        System.out.println("id: " + user.getId());
        System.out.println("userName: " + user.getUsername());
    }
}
