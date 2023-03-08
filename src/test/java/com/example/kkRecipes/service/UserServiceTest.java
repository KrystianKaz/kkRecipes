package com.example.kkRecipes.service;

import com.example.kkRecipes.exception.UserNotExistException;
import com.example.kkRecipes.model.User;
import com.example.kkRecipes.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @InjectMocks
    private UserService userService;
    @Mock
    private UserRepository userRepository;

    @Test
    void should_be_able_to_return_registered_user() {
        // given
        User user = new User("FirstUser", "firstP", "first@email.com", true);

        given(userRepository.findByUsername("FirstUser"))
                .willReturn(Optional.of(user));

        // when
        User givenUser = userService.findUserByUsername("FirstUser");

        // then
        assertThat(givenUser, is(user));
        assertThat(givenUser.getUsername(), is("FirstUser"));
    }

    @Test
    void should_throw_UserNotExistException_when_giving_user_not_found() {
        // given + when
        String username = "JohnDoe";
        Throwable exception = assertThrows(UserNotExistException.class,
                () -> userService.findUserByUsername(username));

        // then
        assertThat(exception.getMessage(), is(new UserNotExistException(username).getMessage()));
    }
}