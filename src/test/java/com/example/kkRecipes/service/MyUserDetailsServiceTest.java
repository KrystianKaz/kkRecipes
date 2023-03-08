package com.example.kkRecipes.service;

import com.example.kkRecipes.model.User;
import com.example.kkRecipes.model.enums.UserRolesEnum;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MyUserDetailsServiceTest {

    @InjectMocks
    private MyUserDetailsService myUserDetailsService;

    @Mock
    private UserService userService;

    @Test
    void should_build_user_authority_by_giving_role() {
        // given
        User user = getUser();

        // when
        List<GrantedAuthority> userAuthority = myUserDetailsService.getUserAuthority(user);

        // then
        assertThat(userAuthority.size(), is(1));
        assertThat(userAuthority.get(0).getAuthority(), is("USER"));
    }

    @Test
    void should_build_user_for_authentication() {
        // given
        User user = getUser();

        ArrayList<GrantedAuthority> authorities = getGrantedAuthorities(user);

        UserDetails givenUserDetails = getUserDetails(user);

        // when
        UserDetails userDetails = myUserDetailsService.buildUserForAuthentication(user, authorities);

        // then
        assertThat(userDetails.getUsername(), is(user.getUsername()));
        assertThat(userDetails, is(givenUserDetails));
    }

    @Test
    void should_return_UserDetails_by_giving_username_of_registered_user() {
        // given
        User user = getUser();

        UserDetails givenUserDetails = getUserDetails(user);

        // when
        when(userService.findUserByUsername(user.getUsername())).thenReturn(user);
        UserDetails userDetails = myUserDetailsService.loadUserByUsername(user.getUsername());

        // then
        assertThat(userDetails.getAuthorities(), is(givenUserDetails.getAuthorities()));
    }

    private static User getUser() {
        User user = new User();
        user.setUsername("FirstUser");
        user.setPassword("firstP");
        user.setActive(true);
        user.setUserRoles(UserRolesEnum.USER);
        return user;
    }

    private static ArrayList<GrantedAuthority> getGrantedAuthorities(final User user) {
        SimpleGrantedAuthority authority = new SimpleGrantedAuthority(String.valueOf(user.getUserRoles()));

        ArrayList<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(authority);
        return authorities;
    }

    private static UserDetails getUserDetails(User user) {
        return new org.springframework.security.core.userdetails.User(user.getUsername(),
                user.getPassword(),
                user.isActive(),
                true,
                true,
                true,
                getGrantedAuthorities(user));
    }
}