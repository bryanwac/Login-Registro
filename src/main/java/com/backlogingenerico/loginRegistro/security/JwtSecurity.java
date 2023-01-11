package com.backlogingenerico.loginRegistro.security;

import com.backlogingenerico.loginRegistro.services.UserDetailsServico;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
public class JwtSecurity {

    private final AuthSucessHandler authSucessHandler;
    private final UserDetailsServico userDetailsService;
    private final String secret;
    @Autowired
    private AuthenticationManager authenticationManager;

    public JwtSecurity(AuthSucessHandler authSucessHandler, UserDetailsServico userDetailsService, @Value("${jwt.secret}") String secret) {
        this.authSucessHandler = authSucessHandler;
        this.userDetailsService = userDetailsService;
        this.secret = secret;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.cors().and().csrf().disable()
                .authorizeHttpRequests((auth) -> {
                    try {
                        auth
                                .antMatchers("/user/**").hasRole("PERM_USER")
                                .antMatchers("/admin/**").hasRole("PERM_ADMIN")
                                .antMatchers(HttpMethod.OPTIONS,"/registro").permitAll()
                                .antMatchers(HttpMethod.OPTIONS,"/login").permitAll()
                                .anyRequest().permitAll()
                                .and()
                                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                                .and()
                                .addFilter(authenticationFilter())
                                .addFilter(new JwtAuthorizationFilter(authenticationManager, userDetailsService, secret))
                                .exceptionHandling()
                                .authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED));
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                })
                .httpBasic(Customizer.withDefaults());
        return http.build();
    }

    // Habilita configurações de cors
    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration().applyPermitDefaultValues();
        config.addAllowedMethod(HttpMethod.PUT);
        config.addAllowedMethod(HttpMethod.DELETE);
        config.addAllowedMethod(HttpMethod.PATCH);
        config.addAllowedMethod(HttpMethod.POST);
        config.addAllowedMethod(HttpMethod.GET);
        source.registerCorsConfiguration("/**", config);
        return source;
    }

    @Bean
    public JsonObjectAuthenticationFilter authenticationFilter() throws Exception {
        JsonObjectAuthenticationFilter filter = new JsonObjectAuthenticationFilter();
        filter.setAuthenticationSuccessHandler(authSucessHandler);
        filter.setAuthenticationManager(authenticationManager);
        return filter;
    }
}
