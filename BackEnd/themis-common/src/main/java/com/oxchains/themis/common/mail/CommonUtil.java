package com.oxchains.themis.common.mail;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author ccl
 * @time 2017-11-13 10:39
 * @name CommonUtil
 * @desc:
 */
public class CommonUtil {
    private static ObjectMapper mapper;

    public static synchronized ObjectMapper getMapperInstance(boolean createNew) {
        if (createNew) {
            return new ObjectMapper();
        } else if (mapper == null) {
            mapper = new ObjectMapper();
        }
        return mapper;
    }
}
