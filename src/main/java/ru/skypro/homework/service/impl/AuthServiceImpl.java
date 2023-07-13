package ru.skypro.homework.service.impl;

import ru.skypro.homework.dto.RegisterReq;
import ru.skypro.homework.dto.Role;
import ru.skypro.homework.model.AuthGrantedAuthority;
import ru.skypro.homework.model.User;
import ru.skypro.homework.repository.AuthGrantedAuthorityRepository;
import ru.skypro.homework.repository.UserRepository;
import ru.skypro.homework.service.AuthService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class AuthServiceImpl implements AuthService {
  private final JpaUserDetailsManager manager;

  private final UserRepository userRepository;
  private final AuthGrantedAuthorityRepository authGrantedAuthorityRepository;


  private final UserService userService;

  private final PasswordEncoder encoder;

  public AuthServiceImpl(JpaUserDetailsManager manager, UserRepository userRepository, AuthGrantedAuthorityRepository authGrantedAuthorityRepository, UserService userService, PasswordEncoder passwordEncoder) {
    this.manager = manager;
    this.userRepository = userRepository;
    this.authGrantedAuthorityRepository = authGrantedAuthorityRepository;
    this.userService = userService;
    this.encoder = passwordEncoder;
  }

  @Override
  public boolean login(String userName, String password) {
    if (!manager.userExists(userName)) {
      return false;
    }
    UserDetails userDetails = manager.loadUserByUsername(userName);
    return encoder.matches(password, userDetails.getPassword());
  }

  @Override
  public boolean register(RegisterReq registerReq, Role role) {
    if (manager.userExists(registerReq.getUsername())) {
      return false;
    }
    User newUser = new User();
    newUser.setUsername(registerReq.getUsername());
    newUser.setPassword(encoder.encode(registerReq.getPassword()));
    newUser.setFirstName(registerReq.getFirstName());
    newUser.setLastName(registerReq.getLastName());
    newUser.setPhone(registerReq.getPhone());
    newUser.setEnabled(true);
    newUser.setAccountNonExpired(true);
    newUser.setAccountNonLocked(true);
    newUser.setCredentialsNonExpired(true);

    AuthGrantedAuthority grantedAuthority = new AuthGrantedAuthority();
    grantedAuthority.setAuthority("ROLE_"+role.name());
    grantedAuthority.setUser(newUser);
    manager.createUser(newUser);
    authGrantedAuthorityRepository.save(grantedAuthority);
    newUser.setAuthorities(Collections.singleton(grantedAuthority));
    return true;
  }
}
