package emadeldeen.paymentsmanagement.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.web.util.matcher.RegexRequestMatcher.regexMatcher;

@Configuration
public class WebSecurityConfig {
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(requests -> requests
                        .requestMatchers(HttpMethod.POST, "/api/auth/users").permitAll() // user registration is public
                        .requestMatchers("/api/auth/change-password").authenticated()
                        .requestMatchers("/api/accounting/payments").permitAll()
                        .requestMatchers("/api/employee/payment").authenticated()
                        .requestMatchers("/error").permitAll() // for sending back errors
                        .requestMatchers(regexMatcher("\\/h2-console.*")).permitAll() // for H2 console
                        .anyRequest().authenticated()
                )
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
}
