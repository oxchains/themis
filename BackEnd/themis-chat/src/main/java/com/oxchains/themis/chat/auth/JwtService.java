package com.oxchains.themis.chat.auth;

import com.oxchains.themis.chat.entity.User;
import com.oxchains.themis.common.util.ObjectByteUtil;
import com.oxchains.themis.repo.dao.TokenKeyDao;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.impl.DefaultJwtParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import com.oxchains.themis.chat.repo.UserRepo;

import java.security.PublicKey;

/**
 * create by huohuo
 * @author huohuo
 */
@Component
public class  JwtService {
    @Value("${jwt.key.store}")
    private String keystore;

    @Value("${jwt.key.pass}")
    private String keypass;

    @Value("${jwt.key.alias}")
    private String keyalias;

    @Value("${jwt.cert}")
    private String cert;

    private static final  Logger LOG = LoggerFactory.getLogger(JwtService.class);
    private final UserRepo userRepo;
    @Autowired
    private TokenKeyDao tokenKeyDao;
    public JwtService(UserRepo userRepo) {
        this.userRepo = userRepo;
    }

    public JwtAuthentication parse(String token) {
        try {
            Jws<Claims> jws = new DefaultJwtParser()
              .setSigningKey((PublicKey) ObjectByteUtil.toObject(tokenKeyDao.findOne(1L).getPubKey()))
              .parseClaimsJws(token);
            Claims claims = jws.getBody();
           User user1 = new User();
           user1.setId(claims.get("id",Integer.class).longValue());
           user1.setUsername(claims.getSubject());
           user1.setEmail(claims.get("email",String.class));
            if(user1!=null){
                return new JwtAuthentication(user1,token,claims);
            }
        } catch (Exception e) {
        }
        return null;
    }
    public String getKeystore(){
        return this.keystore;
    }
}
