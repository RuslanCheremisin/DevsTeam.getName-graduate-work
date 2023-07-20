package ru.skypro.homework;

import static org.springframework.security.config.Customizer.withDefaults;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class WebSecurityConfig {

//  private JpaUserDetailsManager jpaUserDetailsManager;
private static final String[] AUTH_WHITELIST = {
        "/swagger-resources/**",
        "/swagger-ui.html",
        "/v3/api-docs",
        "/webjars/**",
        "/login",
        "/register",
        "/ads/image/**",
        "/ads"
};

/** Авторизация и аутентификация. Фильтр безопасности */
  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http.csrf()
            .disable()
            .authorizeHttpRequests(
                    authorization ->
                            authorization
                                    .mvcMatchers(AUTH_WHITELIST)
                                    .permitAll()
                                    .mvcMatchers("/ads/**", "/users/**").authenticated())
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
//


//  @Bean
//  public DaoAuthenticationProvider jpaDaoAuthenticationProvider() {
//    DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
//    daoAuthenticationProvider.setUserDetailsService(jpaUserDetailsManager);
//    daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
//    return daoAuthenticationProvider;
//  }
//  @Override
//  protected void configure(AuthenticationManagerBuilder auth) {
//    auth.authenticationProvider(jpaDaoAuthenticationProvider());
//  }
//  @Override
//  protected void configure(HttpSecurity http) throws Exception {
//    http.csrf()
//            .disable()
//            .authorizeHttpRequests(
//                    authorization ->
//                            authorization
//                                    .mvcMatchers(AUTH_WHITELIST)
//                                    .permitAll()
//                                    .mvcMatchers("/ads/**", "/users/**")
//                                    .authenticated())
//            .cors()
//            .and()
//            .httpBasic(withDefaults());
//  }
//  @Bean
//  public JdbcUserDetailsManager userDetailsService() {
//    UserDetails user =
//        User.builder()
//            .username("user@gmail.com")
//            .password("password")
//            .passwordEncoder((plainText) -> passwordEncoder().encode(plainText))
//            .roles("USER")
//            .build();
//    JdbcUserDetailsManager users = new JdbcUserDetailsManager(dataSource);
//    users.createUser(user);
//    return users;
//  }





