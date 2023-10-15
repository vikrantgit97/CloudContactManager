package com.project.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class MyConfig /*extends WebSecurityConfigurerAdapter*/ {

    @Bean
    public UserDetailsService getUserDetailsService() {
        return new UserDetailServiceImpl();
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setUserDetailsService(this.getUserDetailsService());
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
        return daoAuthenticationProvider;
    }


    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeRequests(req ->
                        req
                                .requestMatchers("/admin/**").hasAnyRole("ADMIN")
                                .requestMatchers("/**").hasAnyAuthority("USER")
                                .anyRequest()
                                .authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/signup")
                        .loginProcessingUrl("/dologin").defaultSuccessUrl("/user/index"));
        return http.build();
    }


/*    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
      //  super.configure(http);
        http.authorizeHttpRequests().
                requestMatchers("/admin/**").
                hasRole("ADMIN").requestMatchers("/user/**").
                hasRole("USER").requestMatchers("/**").
                permitAll().and().formLogin().
                loginPage("/signin").
                loginProcessingUrl("/dologin").defaultSuccessUrl("/user/index").
                //failureUrl("/login-fail").
                and().csrf().disable();
    }*/

}
