package com.bootcamp.demo.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    public void configure(final HttpSecurity http) throws Exception {
        http
                .csrf().csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
                .and()
                .authorizeRequests()
                .antMatchers("/public/**").permitAll()
                .antMatchers("/actuator/health").permitAll()
                .antMatchers("/swagger-ui/**").permitAll()
                .antMatchers("/swagger-resources/**").permitAll()
                .antMatchers("/webjars/**").permitAll()
                .anyRequest().authenticated()
                .and()
                .formLogin()
                .loginPage("/login")
                .failureUrl("/login-error")
                .permitAll()
                .and()
                .logout()
                .permitAll();
        // temporal solution
        http.csrf().disable();
    }

    @Override
    protected void configure(final AuthenticationManagerBuilder auth) throws Exception {
        final var inMemoryAuth = auth.inMemoryAuthentication();
        inMemoryAuth.withUser("test.team@gmail.com").password("{noop}xxx").roles("USER");
        // Spring Security 5 requires specifying the password storage format
    }

}
