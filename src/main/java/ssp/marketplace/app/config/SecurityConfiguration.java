package ssp.marketplace.app.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.*;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.web.servlet.config.annotation.*;
import ssp.marketplace.app.security.*;
import ssp.marketplace.app.security.jwt.*;

@Configuration
public class SecurityConfiguration extends WebSecurityConfigurerAdapter implements WebMvcConfigurer {

    private final JwtTokenProvider jwtTokenProvider;

    private static final String[] publicEndpoints = new String[]{
            "/api/login",
            "/api/register/supplier",
            "/api/register/verify"
    };

    private static final String[] generalEndpoints = new String[]{
            "/api/tags",
            "/api/user",
            "/api/law-statuses",
            "/api/orders/**",
            "/document/**",
            "/api/supplier/fill",
    };

    private static final String[] userEndpoints = new String[]{
            "/api/home",
            "/api/supplier/update",
            "/api/suppliers/{supplierId}/{filename}"
    };

    private static final String[] adminEndpoints = new String[]{
            "/api/admin",
            "/api/customer/update",
            "/api/register/customer",
            "/api/register/lawyer",
            "/api/register/admin",
            "/admin/**",
            "/api/suppliers",
            "/api/suppliers/{id}",
            "/api/admin/**"
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
        http.cors()
                .and()
                .httpBasic().disable()
                .formLogin().disable()
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                .antMatchers(publicEndpoints).permitAll()
                .antMatchers(generalEndpoints).hasAnyRole("ADMIN", "USER", "BLANK_USER")
                .antMatchers(userEndpoints).hasAnyRole("USER", "ADMIN")
                .antMatchers(adminEndpoints).hasRole("ADMIN")
                .anyRequest().authenticated()
                .and()
                .apply(new JwtConfigurer(jwtTokenProvider))
                .and()
                .exceptionHandling()
                .authenticationEntryPoint(new RestAuthenticationEntryPoint())
                .accessDeniedHandler(new RestAccessDeniedHandler());
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("*")
                .allowedMethods("*");
    }
}