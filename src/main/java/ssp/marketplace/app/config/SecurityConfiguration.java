package ssp.marketplace.app.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.*;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import ssp.marketplace.app.security.*;
import ssp.marketplace.app.security.jwt.*;

@Configuration
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    private final JwtTokenProvider jwtTokenProvider;

    private static final String[] publicEndpoints = new String[]{
            "/api/v1/auth/login",
            "/api/v1/register/user",
            "/api/v1/register/verify"
    };

    private static final String[] userEndpoints = new String[]{
            "/api/v1/auth/home",
            "/api/v1/user"
    };

    private static final String[] adminEndpoints = new String[]{
            "/api/v1/admin",
            "/api/v1/register/admin",
            "/api/v1/register/lawyer"
    };

    @Autowired
    public SecurityConfiguration(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .httpBasic().disable()
                .formLogin().disable()
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .exceptionHandling().authenticationEntryPoint(new RestAuthenticationEntryPoint())
                .and()
                .authorizeRequests()
                .antMatchers(publicEndpoints).permitAll()
                .antMatchers(userEndpoints).hasAnyRole("USER", "ADMIN")
                .antMatchers(adminEndpoints).hasRole("ADMIN")
                .anyRequest().authenticated()
                .and()
                .apply(new JwtConfigurer(jwtTokenProvider));

    }
}