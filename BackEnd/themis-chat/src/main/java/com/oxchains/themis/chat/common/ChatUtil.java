package com.oxchains.themis.chat.common;

import com.oxchains.themis.chat.auth.JwtAuthentication;
import com.oxchains.themis.chat.repo.UserRepo;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.DefaultJwtBuilder;
import io.jsonwebtoken.impl.DefaultJwtParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.security.interfaces.ECPrivateKey;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author aiet
 */
public class ChatUtil {

    public static Map<String,Map<String,ChannelHandler>> userChannels = new ConcurrentHashMap<>();
    private Logger LOG = LoggerFactory.getLogger(getClass());
    public  static String getIDS(String id,String did){
        return Integer.parseInt(id) > Integer.parseInt(did)? did+"_"+id : id+"_"+did;
    }

}
