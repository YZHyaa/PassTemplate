package com.llxs.passbook.passbook.utils;

import com.llxs.passbook.passbook.constant.JwtConstans;
import com.llxs.passbook.passbook.vo.TokenInfo;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;

import java.security.PublicKey;

public class JwtUtils {
    /**
     * 获取token中的用户信息
     *
     * @param token     用户请求中的令牌
     * @param publicKey 公钥
     * @return 用户信息
     */
    public static TokenInfo getInfoFromToken(String token, PublicKey publicKey) {
        Jws<Claims> claimsJws = parserToken(token, publicKey);
        Claims body = claimsJws.getBody();
        return new TokenInfo(
                ObjectUtils.toInt(body.get(JwtConstans.JWT_KEY_TYPE)),
                ObjectUtils.toLong(body.get(JwtConstans.JWT_KEY_ID))
        );
    }

    /**
     * 公钥解析token
     *
     * @param token     用户请求中的token
     * @param publicKey 公钥
     * @return
     */
    private static Jws<Claims> parserToken(String token, PublicKey publicKey) {
        return Jwts.parser().setSigningKey(publicKey).parseClaimsJws(token);
    }

    /**
     * 获取token中的用户信息
     *
     * @param token     用户请求中的令牌
     * @param publicKey 公钥
     * @return 用户信息
     * @throws Exception
     */
    public static TokenInfo getInfoFromToken(String token, byte[] publicKey) throws Exception {
        Jws<Claims> claimsJws = parserToken(token, publicKey);
        Claims body = claimsJws.getBody();
        return new TokenInfo(
                ObjectUtils.toInt(body.get(JwtConstans.JWT_KEY_TYPE)),
                ObjectUtils.toLong(body.get(JwtConstans.JWT_KEY_ID))
        );
    }

    /**
     * 公钥解析token
     *
     * @param token     用户请求中的token
     * @param publicKey 公钥字节数组
     * @return
     * @throws Exception
     */
    private static Jws<Claims> parserToken(String token, byte[] publicKey) throws Exception {
        return Jwts.parser().setSigningKey(RsaUtils.getPublicKey(publicKey))
                .parseClaimsJws(token);
    }
}