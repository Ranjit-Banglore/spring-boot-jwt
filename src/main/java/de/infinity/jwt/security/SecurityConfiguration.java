package de.infinity.jwt.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/** This configuration creates a Servlet Filter known as the springSecurityFilterChain.
 * -Spring security handles authentication using authentication manager.
 * - With each type of authentication(basic, ooauth, ldap, jwt), we have to build a authentication manager.
 * --When user request (in this case basic auth), it gets validated at first to authentication manager.
 * In case of invalid credential- 401, unauthorize rquest error is thrown.
 * --Authentication manager loads the roles and other details with database and validate
 * credentials(username, and password)
 * --It loads the authorities (roles), from database.
 * --With these details authorization is being performed if logged in user is allowed to request this perticular
 * APIs.
 * --In case authorization failed, 403 forbidded HttpError Code is sent with response.
 */

@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
    @Autowired
    private JpaUserDetailsService userDetailsService;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService)
                .passwordEncoder(getPasswordEncoder());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.headers().disable(); // disable the cache

        //http.csrf().disable(); // cross site request frogery

        http.authorizeRequests().antMatchers("/api/**").hasRole("USER")
                .and()
                //.authorizeRequests().antMatchers("/api/**").hasRole("USER")
                .authorizeRequests().antMatchers("/admin").hasRole("ADMIN")
                .and()
                .authorizeRequests().anyRequest().permitAll()
                .and()
                .httpBasic();
    }

    private PasswordEncoder getPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
