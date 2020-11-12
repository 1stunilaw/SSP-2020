package ssp.marketplace.app.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;
import ssp.marketplace.app.entity.User;
import ssp.marketplace.app.security.jwt.*;
import ssp.marketplace.app.service.UserService;

@Service
@Slf4j
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserService userService;

    @Autowired
    public UserDetailsServiceImpl(UserService userService) {
        this.userService = userService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userService.findByEmail(username);

        UserDetailsImpl jwtUser = UserDetailsFactory.create(user);
        log.info("IN loadUserByUsername - user with email: {} successfully loaded", username);
        return jwtUser;
    }
}
