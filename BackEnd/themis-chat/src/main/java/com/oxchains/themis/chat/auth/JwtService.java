package com.oxchains.themis.chat.auth;

import com.oxchains.themis.chat.common.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.DefaultJwtBuilder;
import io.jsonwebtoken.impl.DefaultJwtParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import com.oxchains.themis.chat.repo.UserRepo;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.UUID;

/**
 * @author aiet
 */
@Component
public class  JwtService {
    private Logger LOG = LoggerFactory.getLogger(getClass());
     private String keystore = "this is key store";
     private String keypass = "this is keypass";
      static final String SECRET = "ThisIsASecret";
     private String keyalias = "this is keyalias";
     private String cert = "this is cert";
    private final UserRepo userRepo;
    public JwtService(UserRepo userRepo) {
        this.userRepo = userRepo;
    }
    public String generate(User user) {
        return new DefaultJwtBuilder()
          .setId(UUID
            .randomUUID()
            .toString())
          .setSubject(user.getUsername())
          .setExpiration(Date.from(ZonedDateTime
            .now()
            .plusWeeks(1)
            .toInstant()))
          .claim("email", user
            .getEmail())
                .claim("id",user.getId())
          .signWith(SignatureAlgorithm.HS256, SECRET)
          .compact();
    }

    JwtAuthentication parse(String token) {
        try {
            Jws<Claims> jws = new DefaultJwtParser()
              .setSigningKey(SECRET)
              .parseClaimsJws(token);
            Claims claims = jws.getBody();
            User user = userRepo.findUserByUsernameAndEmail(claims.getSubject(), claims.get("email", String.class));
            System.out.println(user);
            if(user!=null){
                return new JwtAuthentication(user,token,claims);
            }
        } catch (Exception e) {
        }
        return null;
    }
    public String getKeystore(){
        return this.keystore;
    }
}
