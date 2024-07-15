package com.example.bdMarketMaster.security;

import com.example.bdMarketMaster.features.auth.dao.AuthDAO;
import com.example.bdMarketMaster.models.UserModel;
import com.example.bdMarketMaster.security.jwt.AuthEntryPointJwt;
import com.example.bdMarketMaster.security.jwt.AuthTokenFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.DefaultAuthenticationEventPublisher;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {
    @Autowired
    DataSource dataSource;

    @Autowired
    private AuthEntryPointJwt unauthorizedHandler;

    @Autowired
    private AuthDAO authDAO;

    @Bean
    public AuthTokenFilter authenticationJwtTokenFilter() {
        return  new AuthTokenFilter();
    }

    @Bean
    SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http, DefaultAuthenticationEventPublisher authenticationEventPublisher) throws Exception {
        http.authorizeRequests(authorizeRequests -> authorizeRequests.requestMatchers("/public/**")
                .permitAll().requestMatchers("/admin/**").hasRole("ADMIN")
                .anyRequest().authenticated());
        http.sessionManagement(session -> session.
                sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        http.exceptionHandling(exception -> exception.authenticationEntryPoint(unauthorizedHandler));
        http.headers(headers -> headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin));

        http.csrf(AbstractHttpConfigurer::disable);

        http.addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        UserDetails user1 = User.withUsername("user1")
                .password(passwordEncoder().encode("asdfgh"))
                .roles("USER")
                .build();// noop ensure password encoding
        UserDetails admin = User.withUsername("admin")
                .password(passwordEncoder().encode("asdfgh1"))
                .roles("ADMIN")
                .build();
        UserModel userModel = new UserModel();
        userModel.setUsername("admin");
        userModel.setPassword("asdfgh1");
        userModel.setDob("01/01/2000");
        userModel.setEmail("admin@example.com");
        userModel.setIdentity("admin");
        userModel.setPhoneNumber("01000000000");
        userModel.setVerifyTryCount(-1);
        try{
            authDAO.save(userModel);
        }catch (Exception e){
//            e.printStackTrace();
        }
        JdbcUserDetailsManager userDetailsManager = new JdbcUserDetailsManager(dataSource);
        try {
            if (!userDetailsManager.userExists(user1.getUsername())) {
                userDetailsManager.createUser(user1);
            }
            if (!userDetailsManager.userExists(admin.getUsername())) {
                userDetailsManager.createUser(admin);
            }
        } catch (DataIntegrityViolationException e) {
            // Handle exception if any
        }
        return userDetailsManager;
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }


    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
}

