package com.bastion.cyber.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.bastion.cyber.exception.ExceptionCast;
import com.bastion.cyber.model.bo.AuthBo;
import com.bastion.cyber.model.bo.SignBo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.*;

import static com.bastion.cyber.constant.ReturnNo.AUTH_JWT_EXPIRED;

/**
 * JWT工具
 *
 * @author Ming Qiu
 * @date created in 2019/11/11
 * modifiedBy Ming Qiu
 * 增加departId 到createToken
 */
@Component
@Slf4j
public class JwtHelper {
    // 秘钥
    private static final String SECRET = "Role-Privilege-Token";
    // 签名是有谁生成
    private static final String ISSUSER = "Bastion";
    // 签名的主题
    private static final String SUBJECT = "this is a token";
    // 签名的观众
    private static final String AUDIENCE = "MINIAPP";

    /**
     * 创建用户Token
     *
     * @return token
     */
    public static String createToken(SignBo signBo, int expireTime) {
        log.debug("createToken:");
        try {
            Algorithm algorithm = Algorithm.HMAC256(SECRET);
            Map<String, Object> map = new HashMap<>();
            Date nowDate = new Date();
            Date expireDate = getAfterDate(nowDate, 0, 0, 0, 0, expireTime, 0);
            map.put("alg", "HS256");
            map.put("typ", "JWT");
            StringBuilder message = new StringBuilder().append("createToken: ")
                    .append(" userBo = ").append(signBo.string());
            log.debug(message.toString());
            String token = JWT.create()
                    // 设置头部信息 Header
                    .withHeader(map)
                    // 设置 载荷 Payload
                    .withClaim("payload", signBo.toMap())

                    .withIssuer(ISSUSER)
                    .withSubject(SUBJECT)
                    .withAudience(AUDIENCE)
                    // 生成签名的时间
                    .withIssuedAt(nowDate)
                    // 签名过期的时间
                    .withExpiresAt(expireDate)
                    // 签名 Signature
                    .sign(algorithm);
            return token;
        } catch (JWTCreationException exception) {
            log.error(exception.getMessage());
        }
        return null;
    }

    /**
     * 获得UserId和DepartId
     *
     * @param token
     * @return UserAndDepart
     * modifiedBy Ming Qiu 2020/11/3 23:09
     */
    public static AuthBo verifyTokenAndGetClaims(String token) {
        if (token == null || token.isEmpty()) {
            return null;
        }
        token = token.replace("Bearer ", "");
        try {
            Algorithm algorithm = Algorithm.HMAC256(SECRET);
            JWTVerifier verifier = JWT.require(algorithm)
                    .withIssuer(ISSUSER)
                    .build();
            DecodedJWT jwt = verifier.verify(token);
            Map<String, Claim> claims = jwt.getClaims();
            SignBo signBo = claims.get("payload").as(SignBo.class);
            return AuthBo.builder().id(signBo.getId()).build();
        } catch (JWTVerificationException exception) {
            log.error(exception.getMessage());
            if (exception.getMessage().contains("The Token has expired on Sat Apr")) {
                ExceptionCast.cast(AUTH_JWT_EXPIRED);
            }
        }
        return null;
    }

    private static Date getAfterDate(Date date, int year, int month, int day, int hour, int minute, int second) {
        if (date == null) {
            date = new Date();
        }

        Calendar cal = new GregorianCalendar();

        cal.setTime(date);
        if (year != 0) {
            cal.add(Calendar.YEAR, year);
        }
        if (month != 0) {
            cal.add(Calendar.MONTH, month);
        }
        if (day != 0) {
            cal.add(Calendar.DATE, day);
        }
        if (hour != 0) {
            cal.add(Calendar.HOUR_OF_DAY, hour);
        }
        if (minute != 0) {
            cal.add(Calendar.MINUTE, minute);
        }
        if (second != 0) {
            cal.add(Calendar.SECOND, second);
        }
        return cal.getTime();
    }
}
