package com.oxchains.chat.common;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.impl.DefaultJwtParser;
import io.netty.channel.Channel;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author aiet
 */
public class JwtService {
    static final String SECRET = "ThisIsASecret";

    public static Map<String,ChannelHandler> userChannels = new ConcurrentHashMap<>();

    public static User parse(String token) {
        try {
            Jws<Claims> jws = new DefaultJwtParser()
              .setSigningKey(SECRET)
              .parseClaimsJws(token);
            Claims claims = jws.getBody();
            User user = new User(claims.get("id",Integer.class),claims.getSubject());
            return user;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
