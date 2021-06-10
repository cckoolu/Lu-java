package lujava;

import org.junit.Test;

import static org.junit.Assert.*;


public class AESUtilTest {

    @Test
    public void aesTest(){
        String strToEncrypt = "Hello World!";
        String secret = "123456";
        String key=AESUtil.encrypt(strToEncrypt,secret);
        assertEquals(strToEncrypt,AESUtil.decrypt(key,secret));
    }
}