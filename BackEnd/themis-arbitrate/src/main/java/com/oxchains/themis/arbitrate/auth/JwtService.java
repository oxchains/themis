package com.oxchains.themis.arbitrate.auth;

import com.oxchains.themis.arbitrate.entity.User;
import com.oxchains.themis.arbitrate.repo.UserRepo;
import com.oxchains.themis.common.util.ObjectByteUtil;
import com.oxchains.themis.repo.dao.TokenKeyDao;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.impl.DefaultJwtParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.security.PublicKey;
/**
 * create by huohuo
 * @author huohuo
 */
@Service
public class  JwtService {
    private static final  Logger LOG = LoggerFactory.getLogger(JwtService.class);
    @Resource
    private  UserRepo userRepo;
    @Resource
    private TokenKeyDao tokenKeyDao;
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
}
