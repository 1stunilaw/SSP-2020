package ssp.marketplace.app.security.jwt;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import ssp.marketplace.app.entity.*;

import java.util.*;
import java.util.stream.Collectors;

public final class UserDetailsFactory {

    public UserDetailsFactory() {
    }

    public static UserDetailsImpl create(User user){
        return new UserDetailsImpl(
                user.getId(),
                user.getEmail(),
                user.getPassword(),
                user.getStatus().equals(Status.ACTIVE),
                user.getUpdatedAt(),
                mapToGrantedAuthorities(user.getRoles()));
    }

    private static List<GrantedAuthority> mapToGrantedAuthorities(Set<Role> userRoles){
        return userRoles.stream().map(
                role -> new SimpleGrantedAuthority(role.getName().name())
        ).collect(Collectors.toList());
    }
}
