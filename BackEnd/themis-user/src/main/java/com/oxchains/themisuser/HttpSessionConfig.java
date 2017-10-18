package com.oxchains.themisuser;

import org.springframework.context.annotation.Configuration;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

/**
 * @Author ccl
 * @Time 2017-10-18 14:11
 * @Name HttpSessionConfig
 * @Desc:
 */
@Configuration
@EnableRedisHttpSession
public class HttpSessionConfig {
}
