package com.oxchains.themis;

import com.oxchains.themis.user.auth.AuthError;
import com.oxchains.themis.user.auth.JwtAuthenticationProvider;
import com.oxchains.themis.user.auth.JwtTokenFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;


/**
 * @Author ccl
 * @Time 2017-10-12 11:21
 * @Name ApplicationConfiguration
 * @Desc:
 */

@EnableWebSecurity
@Configuration
public class ApplicationConfiguration extends WebSecurityConfigurerAdapter{

    private final JwtAuthenticationProvider jwtAuthenticationProvider;
    private final JwtTokenFilter jwtTokenFilter;
    private AuthError authError;

    public ApplicationConfiguration(@Autowired JwtTokenFilter jwtTokenFilter, @Autowired JwtAuthenticationProvider jwtAuthenticationProvider, @Autowired AuthError authError) {
        this.jwtTokenFilter = jwtTokenFilter;
        this.jwtAuthenticationProvider = jwtAuthenticationProvider;
        this.authError = authError;
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        //http.csrf().disable().authorizeRequests().antMatchers("/", "/").permitAll();
        http.cors().and().csrf().disable().authorizeRequests().antMatchers(HttpMethod.POST,"/login","/token","/register","/account/*", "/notice/search/*").permitAll()
                .antMatchers(HttpMethod.GET,"/verifyCode","/account/*", "/notice/search/*").permitAll().antMatchers("/**/*")
                .authenticated().and()
                .addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling()
                .authenticationEntryPoint(authError)
                .accessDeniedHandler(authError);
    }

    @Override
    protected void configure(final AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(jwtAuthenticationProvider);
    }

    /**
     * allow cross origin requests
     */
    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurerAdapter() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry
                        .addMapping("/**")
                        .allowedOrigins("*")
                        .allowedMethods("GET", "POST", "PUT", "OPTIONS", "DELETE")
                        .allowedHeaders("*");
            }
        };
    }
}
