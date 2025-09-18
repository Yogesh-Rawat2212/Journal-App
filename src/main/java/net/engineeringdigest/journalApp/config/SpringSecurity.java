package net.engineeringdigest.journalApp.config;

import lombok.RequiredArgsConstructor;
import net.engineeringdigest.journalApp.service.UserDetailsServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;


@Configuration
@EnableWebSecurity //WebSecurity Support ko enable krdo
@RequiredArgsConstructor
public class SpringSecurity {

    private final UserDetailsServiceImpl userDetailsService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(
                        auth -> auth
                                .requestMatchers("/journal/**","/user/**").authenticated()
                                .requestMatchers("/admin/**").hasRole("ADMIN")
                                .anyRequest().permitAll()
                )
                .httpBasic(Customizer.withDefaults())
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                );
        return http.build();
    }

    @Bean
    public AuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider(); // manager uses DaoAuthenticationProvider for DB authentication
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
    /*
        If DB password = $2a$10$abc... (BCrypt hash)
        And user entered "1234"
        BCryptPasswordEncoder compares them securely.
        If passwords match → Spring returns an Authentication object (with username + roles).
        If not → Spring throws BadCredentialsException.
    */

}

/*

In Spring Security, the AuthenticationManager is the main entry point for authentication.
But the AuthenticationManager itself doesn’t actually know how to authenticate a user.


    1: User sends Post/login
       username = yogi, password = 1234 Spring Security intercepts this request (because of .formLogin() in your config).
    2: Spring Security calls AuthenticationManager.authenticate()
        this happens inside spring security
        @Bean
        public AuthenticationManager authManager(AuthenticationConfiguration config) {
            return config.getAuthenticationManager();
        }
        It now has access to all configured providers (like DaoAuthenticationProvider).
    3: So the manager uses DaoAuthenticationProvider for DB authentication.
        inside provider: provider.setUserDetailsService(userDetailsService);
        when user = yogi login,spring calls: provider.setUserDetailsService(userDetailsService);
    4: DaoAuthenticationProvider checks password

*/
