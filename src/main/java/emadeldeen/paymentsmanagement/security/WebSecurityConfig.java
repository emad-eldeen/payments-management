package emadeldeen.paymentsmanagement.security;

import emadeldeen.paymentsmanagement.business.Role;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;

import static org.springframework.security.web.util.matcher.RegexRequestMatcher.regexMatcher;

@Configuration
public class WebSecurityConfig {
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(requests -> requests
                        .requestMatchers(HttpMethod.POST, "/api/auth/users").permitAll() // user registration is public
                        .requestMatchers("/api/auth/change-password").authenticated()
                        .requestMatchers("/api/accounting/payments").hasAnyAuthority(Role.RoleEnum.ROLE_ACCOUNTANT.toString())
                        .requestMatchers("/api/employee/payment").hasAnyAuthority(Role.RoleEnum.ROLE_ACCOUNTANT.toString(),
                                Role.RoleEnum.ROLE_USER.toString())
                        .requestMatchers(HttpMethod.GET, "/api/admin/user").hasAnyAuthority(Role.RoleEnum.ROLE_ADMINISTRATOR.toString())
                        .requestMatchers(HttpMethod.DELETE, "/api/admin/user").hasAnyAuthority(Role.RoleEnum.ROLE_ADMINISTRATOR.toString())
                        .requestMatchers(HttpMethod.PUT, "/api/admin/user/role").hasAnyAuthority(Role.RoleEnum.ROLE_ADMINISTRATOR.toString())
                        .requestMatchers(HttpMethod.GET, "/api/security/events").hasAnyAuthority(Role.RoleEnum.ROLE_AUDITOR.toString())
                        .requestMatchers("/error").permitAll() // for sending back errors
                        .requestMatchers(regexMatcher("\\/h2-console.*")).permitAll() // for H2 console
                        .anyRequest().authenticated()
                )
                .exceptionHandling(customizer -> customizer.accessDeniedHandler(accessDeniedHandler()))
                .httpBasic(Customizer.withDefaults()) // enable basic html authentication
                .csrf(AbstractHttpConfigurer::disable) // for postman requests
                .headers(headersConfigurer -> headersConfigurer
                        .frameOptions(HeadersConfigurer.FrameOptionsConfig::disable) // for postman requests
                );
        return http.build();
    }

    // password encoder/decoder
    @Bean
    public PasswordEncoder getEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AccessDeniedHandler accessDeniedHandler() {
        return new CustomAccessDeniedHandler();
    }
}
