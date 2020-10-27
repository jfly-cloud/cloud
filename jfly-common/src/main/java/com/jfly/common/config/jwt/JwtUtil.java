package com.jfly.common.config.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import com.jfly.common.utils.JKSUtil;
import org.springframework.util.CollectionUtils;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.Map;

public class JwtUtil {

    /**
     * 解密Token
     */
    public static Map<String, Claim> verifyToken(String token) {
        DecodedJWT jwt = null;
        try {
            Algorithm algorithm = Algorithm.RSA256((RSAPublicKey) JKSUtil.getPublicKey(),(RSAPrivateKey)JKSUtil.getPrivateKey());
            JWTVerifier verifier = JWT.require(algorithm).build();
            jwt = verifier.verify(token);
            return jwt.getClaims();
        } catch (Exception e) {
             e.printStackTrace();
             return null;
        }
    }

    /**
     * 根据Token获取userId
     */
    public static Long getUserId(String token) {
        Map<String, Claim> claims = verifyToken(token);
        if (!CollectionUtils.isEmpty(claims)) {
            Claim user_id_claim = claims.get("userId");
            return Long.valueOf(user_id_claim.asString());
        }else{
            return null;
        }

    }

}