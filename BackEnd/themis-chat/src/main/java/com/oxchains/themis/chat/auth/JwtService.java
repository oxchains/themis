package com.oxchains.themis.chat.auth;

import com.oxchains.themis.chat.common.User;
import com.oxchains.themis.common.auth.AuthorizationConst;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.DefaultJwtBuilder;
import io.jsonwebtoken.impl.DefaultJwtParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import com.oxchains.themis.chat.repo.UserRepo;

import javax.annotation.PostConstruct;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.security.interfaces.ECPrivateKey;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.UUID;

/**
 * @author aiet
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

    private PrivateKey privateKey;

    private PublicKey publicKey;

    private Logger LOG = LoggerFactory.getLogger(getClass());

    private final UserRepo userRepo;
    public JwtService(UserRepo userRepo) {
        this.userRepo = userRepo;
    }
    @PostConstruct
    private void init() throws Exception {
        char[] pass = keypass.toCharArray();
        KeyStore from = KeyStore.getInstance("JKS", "SUN");
        from.load(new ClassPathResource(keystore).getInputStream(), pass);
        privateKey = (ECPrivateKey) from.getKey(keyalias, pass);

        CertificateFactory certificatefactory = CertificateFactory.getInstance("X.509");
        X509Certificate x509Cert = (X509Certificate) certificatefactory.generateCertificate(new ClassPathResource(cert).getInputStream());
        publicKey = x509Cert.getPublicKey();
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
          .signWith(SignatureAlgorithm.ES256, privateKey)
          .compact();
    }

    public JwtAuthentication parse(String token) {
        try {
            Jws<Claims> jws = new DefaultJwtParser()
              .setSigningKey(publicKey)
              .parseClaimsJws(token);
            Claims claims = jws.getBody();
           User user1 = new User();
           user1.setId(claims.get("id",Long.class));
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
