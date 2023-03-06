package com.example.kkRecipes.service;

import com.example.kkRecipes.TestDataSample;
import com.example.kkRecipes.model.User;
import com.example.kkRecipes.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class UserServiceTest implements TestDataSample {

    @InjectMocks
    private UserService userService;
    @Mock
    private UserRepository userRepository;

    @Test
    void should_be_able_to_return_registered_user() {
        // given
        User user1 = getSampleDataForUsers().get(0);
        User user2 = getSampleDataForUsers().get(1);

        given(userRepository.findByUsername("FirstUser"))
                .willReturn(Optional.of(user1));

        // when
        User givenUser = userService.findUserByUsername("FirstUser");

        // then
        assertThat(givenUser, is(user1));
        assertThat(givenUser.getUsername(), is("FirstUser"));
        assertThat(givenUser, not(user2));
        assertThat(givenUser.getUsername(), not(user2.getUsername()));
    }
}