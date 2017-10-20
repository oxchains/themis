package oxchains.chat.auth;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.DefaultJwtBuilder;
import io.jsonwebtoken.impl.DefaultJwtParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import oxchains.chat.entity.User;
import oxchains.chat.repo.UserRepo;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.UUID;

/**
 * @author aiet
 */
@Service
public class  JwtService {
    private Logger LOG = LoggerFactory.getLogger(getClass());
     private String keystore = "this is key store";
     private String keypass = "this is keypass";
      static final String SECRET = "ThisIsASecret";
     private String keyalias = "this is keyalias";
     private String cert = "this is cert";
    private PrivateKey privateKey;
    private PublicKey publicKey;

    private final UserRepo userRepo;

    public JwtService(UserRepo userRepo) {
        this.userRepo = userRepo;
    }

    /*@PostConstruct
    private void init() throws Exception {
        char[] pass = keypass.toCharArray();
        KeyStore from = KeyStore.getInstance("JKS", "SUN");
        from.load(new ClassPathResource(keystore).getInputStream(), pass);
        privateKey = (ECPrivateKey) from.getKey(keyalias, pass);

        CertificateFactory certificatefactory = CertificateFactory.getInstance("X.509");
        X509Certificate x509Cert = (X509Certificate) certificatefactory.generateCertificate(new ClassPathResource(cert).getInputStream());
        publicKey = x509Cert.getPublicKey();
    }*/

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
          .claim("password", user.getPassword())
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
            if(user!=null){
                return new JwtAuthentication(user,token,claims);
            }
        } catch (Exception e) {
            LOG.error("failed to parse jwt token {}: ", token, e);
        }
        return null;
    }
    public String getKeystore(){
        return this.keystore;
    }
}
