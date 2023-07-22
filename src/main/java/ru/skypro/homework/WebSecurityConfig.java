package ru.skypro.homework;

import static org.springframework.security.config.Customizer.withDefaults;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class WebSecurityConfig {

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http.csrf()
            .disable()
            .authorizeHttpRequests(
                    authorization ->
                            authorization
                                    .mvcMatchers("/login", "/register")
                                    .permitAll()
                                    .mvcMatchers(HttpMethod.GET, "/ads/image/**", "/ads")
                                    .permitAll()
                                    .mvcMatchers("/ads/**", "/users/**").authenticated()
                                    .mvcMatchers("/ads/**", "/users/**").hasAnyAuthority("ADMIN")
            )
            .cors()
            .and()
            .httpBasic(withDefaults());
    return http.build();
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

}
