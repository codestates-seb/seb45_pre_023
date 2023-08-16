package sixman.stackoverflow.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.CorsConfigurer;
import org.springframework.security.config.annotation.web.configurers.ExpressionUrlAuthorizationConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import sixman.stackoverflow.auth.jwt.filter.JwtAuthenticationFilter;
import sixman.stackoverflow.auth.jwt.filter.JwtRefreshFilter;
import sixman.stackoverflow.auth.jwt.filter.JwtVerificationFilter;
import sixman.stackoverflow.auth.jwt.handler.*;
import sixman.stackoverflow.auth.jwt.service.TokenProvider;

import java.util.List;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final TokenProvider tokenProvider;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
                .csrf().disable()
                .httpBasic().disable()
                .formLogin().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .cors(getCorsConfigurerCustomizer());

        http
                .apply(new CustomFilterConfigurer());

        http.oauth2Login();

        http.exceptionHandling()
                .accessDeniedHandler(new MemberAccessDeniedHandler())
                .authenticationEntryPoint(new MemberAuthenticationEntryPoint());

        http.authorizeRequests(getAuthorizeRequestsCustomizer());

        return http.build();
    }

    private Customizer<ExpressionUrlAuthorizationConfigurer<HttpSecurity>.ExpressionInterceptUrlRegistry> getAuthorizeRequestsCustomizer() {
        return (requests) -> requests
                .antMatchers("/auth/**").permitAll()
                .antMatchers(HttpMethod.GET, "/members/**").permitAll()
                .antMatchers(HttpMethod.POST, "/members/email/**").permitAll()
                .antMatchers(HttpMethod.GET, "/questions/**").permitAll()
                .antMatchers(HttpMethod.GET, "/answers/**").permitAll()
                .antMatchers(HttpMethod.GET, "/replies/**").permitAll()
                .anyRequest().authenticated();
    }


    @Bean
    public Customizer<CorsConfigurer<HttpSecurity>> getCorsConfigurerCustomizer() {

        CorsConfiguration configuration = new CorsConfiguration();
        configuration.addAllowedOrigin("http://sixman-front-s3.s3-website.ap-northeast-2.amazonaws.com");
        configuration.addAllowedOrigin("http://localhost:3000");
        configuration.addAllowedMethod("*");
        configuration.addAllowedHeader("*");
        configuration.setAllowCredentials(true);
        configuration.setExposedHeaders(List.of("Authorization", "Refresh", "Location"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return corsConfigurer -> corsConfigurer.configurationSource(source);
    }

    public class CustomFilterConfigurer extends AbstractHttpConfigurer<CustomFilterConfigurer, HttpSecurity> {
        @Override
        public void configure(HttpSecurity builder) {
            AuthenticationManager authenticationManager = builder.getSharedObject(AuthenticationManager.class);

            JwtAuthenticationFilter jwtAuthenticationFilter = new JwtAuthenticationFilter(authenticationManager, tokenProvider);
            jwtAuthenticationFilter.setFilterProcessesUrl("/auth/login");
            jwtAuthenticationFilter.setAuthenticationSuccessHandler(new MemberAuthenticationSuccessHandler());
            jwtAuthenticationFilter.setAuthenticationFailureHandler(new MemberAuthenticationFailureHandler());

            JwtRefreshFilter jwtRefreshFilter = new JwtRefreshFilter(tokenProvider, new MemberRefreshFailureHandler());
            JwtVerificationFilter jwtVerificationFilter = new JwtVerificationFilter(tokenProvider);

            builder
                    .addFilter(jwtAuthenticationFilter)
                    .addFilterAfter(jwtRefreshFilter, JwtAuthenticationFilter.class)
                    .addFilterAfter(jwtVerificationFilter, JwtRefreshFilter.class);
        }
    }
}
