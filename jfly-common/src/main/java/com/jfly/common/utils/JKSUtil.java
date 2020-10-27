package com.jfly.common.utils;
import org.springframework.core.io.ClassPathResource;
import java.io.FileInputStream;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.cert.Certificate;

public class JKSUtil {

    /***
     * 从jks密钥库容器中的密钥对中读取证书公钥
     */
    public static  PublicKey getPublicKey() throws Exception{

        // 读取密钥是所要用到的工具类
        KeyStore keystore = KeyStore.getInstance("JKS");
        String strorePasswd= "jfly-cloud";//密钥容器密码
        String keyAlias="jfly-cloud";//证书别名
        FileInputStream jksFileInputStream = new FileInputStream(new ClassPathResource("keystore.jks").getFilename());
        //读取公钥
        keystore.load(jksFileInputStream, strorePasswd.toCharArray());
        Certificate cert = keystore.getCertificate(keyAlias);
        //公钥类所对应的类
        PublicKey pubkey = cert.getPublicKey();
        return pubkey;
    }

    /***
     * 从jks密钥库容器中的密钥对中读取证书私钥
     */
    public static PrivateKey getPrivateKey() throws Exception{

        // 读取密钥是所要用到的工具类
        KeyStore keystore = KeyStore.getInstance("JKS");
        String strorePasswd= "jfly-cloud";//密钥容器密码
        String keyAlias="jfly-cloud";//别名
        String keyAliasPass="jfly-cloud";//私钥密码
        FileInputStream jksFileInputStream = new FileInputStream(new ClassPathResource("keystore.jks").getFilename());
        // 读取公钥
        keystore.load(jksFileInputStream, strorePasswd.toCharArray());
        //通过别名和密码得到私钥
        PrivateKey privatekey = (PrivateKey)keystore.getKey(keyAlias,keyAliasPass.toCharArray());
        return privatekey;

    }


}
