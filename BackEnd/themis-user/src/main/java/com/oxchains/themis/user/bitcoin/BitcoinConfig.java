package com.oxchains.themis.user.bitcoin;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Properties;
import java.util.Set;

/**
 * @author ccl
 * @time 2017-10-13 17:43
 * @name BitcoinConfig
 * @desc:
 */
@Component
public class BitcoinConfig extends AbstractConfig{

    private static String url;
    private static String port;
    private static String username;
    private static String password;

    private static String feeRate;
    private static String maxFee;

    static {
        Properties pro = new Properties();
        try {
            pro.load(BitcoinConfig.class.getResourceAsStream("/application.properties"));
            url = pro.getProperty("bitcoin.service.url");
            port = pro.getProperty("bitcoin.service.port");
            username = pro.getProperty("bitcoin.service.username");
            password = pro.getProperty("bitcoin.service.password");
            feeRate = pro.getProperty("bitcoin.fee.rate");
            maxFee = pro.getProperty("bitcoin.max.fee");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private BitcoinConfig(){}
    public static String getUrlString(){
        return "http://"+username+":"+password+"@"+url+":"+port+"/";
    }

    public static double getFeeRate(){
        return Double.valueOf(feeRate);
    }

    public static double getMaxFee(){
        return Double.valueOf(maxFee);
    }

}
