package com.auspos.suburb.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity
public class AppSecurityConfig extends WebSecurityConfigurerAdapter {

//    @Autowired
//    private AuthenticationEntryPoint authEntryPoint;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers(HttpMethod.GET, "/v1/suburb/postcode/**").permitAll()
                .antMatchers(HttpMethod.GET, "/v1/suburb/name/**").permitAll()
                .anyRequest().fullyAuthenticated()
                .and().httpBasic()
                .and().csrf().disable();
//                .authenticationEntryPoint(authEntryPoint);
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication()
                .withUser("admin")
//                .password("adminPassword")
                .password("$2a$04$18zGmcUgSzAQhKfex.mnY..2tx1zPK1lMjXhwiqvlMVwsjubXE6iS")
                .roles("ADMIN");
    }
}
