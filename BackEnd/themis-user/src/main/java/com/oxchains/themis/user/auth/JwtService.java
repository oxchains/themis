package com.oxchains.themis.user.auth;

import com.oxchains.themis.user.dao.UserDao;
import com.oxchains.themis.user.domain.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.DefaultJwtBuilder;
import io.jsonwebtoken.impl.DefaultJwtParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.security.interfaces.ECPrivateKey;
import java.sql.Date;
import java.time.ZonedDateTime;
import java.util.Optional;
import java.util.UUID;

import static java.util.Optional.empty;

/**
 * @author aiet
 */
@Service
public class JwtService {

    private Logger LOG = LoggerFactory.getLogger(getClass());

    @Value("${jwt.key.store}") private String keystore;

    @Value("${jwt.key.pass}") private String keypass;

    @Value("${jwt.key.alias}") private String keyalias;

    @Value("${jwt.cert}") private String cert;

    private PrivateKey privateKey;
    private PublicKey publicKey;

    private final UserDao userDao;

    public JwtService(UserDao userDao){
        this.userDao=userDao;
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

    public String generate(User user){
        return new DefaultJwtBuilder().
                setId(UUID.randomUUID().toString()).
                setSubject(user.getLoginname()).
                setExpiration(Date.from(ZonedDateTime.now().plusWeeks(1).toInstant())).claim("id",user.getId()).claim("email",user.getEmail()).
                signWith(SignatureAlgorithm.ES256,privateKey).
                compact();
    }

    Optional<JwtAuthentication> parse(String token) {

        try {
            Jws<Claims> jws = new DefaultJwtParser()
                    .setSigningKey(publicKey)
                    .parseClaimsJws(token);
            Claims claims = jws.getBody();
            User user=userDao.findByLoginname(claims.getSubject());
            JwtAuthentication jwtAuthentication=new JwtAuthentication(user,token,claims);
            return Optional.of(jwtAuthentication);
        } catch (Exception e) {
            LOG.error("failed to parse jwt token {}: ", token, e);
        }
        return empty();
    }

}
