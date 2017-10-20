package oxchains.chat.common;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.impl.DefaultJwtParser;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author aiet
 */
public class JwtService {
      static final String SECRET = "ThisIsASecret";
    public static Map<String,Map<String,ChannelHandler>> userChannels = new ConcurrentHashMap<>();
    public static User parse(String token) {
        try {
            Jws<Claims> jws = new DefaultJwtParser()
              .setSigningKey(SECRET)
              .parseClaimsJws(token);
            Claims claims = jws.getBody();
            User user = new User(claims.get("id",Long.class),claims.getSubject());
            return user;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    public  static String getIDS(String id,String did){
        return Integer.parseInt(id) > Integer.parseInt(did)? did+"_"+id : id+"_"+did;
    }

}
