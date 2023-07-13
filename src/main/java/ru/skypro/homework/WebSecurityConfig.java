package ru.skypro.homework;

import static org.springframework.security.config.Customizer.withDefaults;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.SecurityBuilder;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import ru.skypro.homework.repository.UserRepository;
import ru.skypro.homework.service.impl.JpaUserDetailsManager;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

@Configuration
public class WebSecurityConfig {

//  private JpaUserDetailsManager jpaUserDetailsManager;


  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http.csrf()
            .disable()
            .authorizeHttpRequests(
                    authorization ->
                            authorization
                                    .mvcMatchers(AUTH_WHITELIST)
                                    .permitAll()
                                    .mvcMatchers("/ads/**", "/users/**")
                                    .hasAnyRole("ADMIN", "USER")
                                    .mvcMatchers("/**")
                                    .hasRole("ADMIN"))
            .cors()
            .and()
            .httpBasic(withDefaults());
    return http.build();
  }
//
  private static final String[] AUTH_WHITELIST = {
    "/swagger-resources/**",
    "/swagger-ui.html",
    "/v3/api-docs",
    "/webjars/**",
    "/login",
    "/register",
    "/ads/image/**",
        "/ads/*"


  };

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




  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

}
