package com.example.kkRecipes.service;

import com.example.kkRecipes.exception.custom.*;
import com.example.kkRecipes.model.User;
import com.example.kkRecipes.model.enums.UserRolesEnum;
import com.example.kkRecipes.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @InjectMocks
    private UserService userService;
    @Mock
    private UserRepository userRepository;


    @Test
    void should_find_all_registered_users() {
        // given
        User user1 = getUser();
        User user2 = new User("C3PO", "password", "c3po@page.com", true);
        List<User> givenUsers = List.of(user1, user2);

        given(userRepository.findAll()).willReturn(givenUsers);

        // when
        List<User> registeredUsers = userService.findAllUsers();

        // then
        assertThat(registeredUsers.size(), is(2));
        assertThat(givenUsers, is(registeredUsers));
    }

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
    void should_throw_exception_when_changing_status_of_user_with_administrator_role() {
        // given
        User givenUser = getUser();
        givenUser.setUserRoles(UserRolesEnum.ADMIN);

        given(userRepository.findById(givenUser.getId())).willReturn(Optional.of(givenUser));

        // when
        IllegalOperationException exception = assertThrows(IllegalOperationException.class,
                () -> userService.activateOrDeactivateUserById(givenUser.getId()));

        // then
        assertThat(exception.getMessage(), is(new IllegalOperationException().getMessage()));
    }

    @Test
    void should_change_status_of_given_user() {
        // given
        User user1 = getUser(); // active:true
        user1.setId(1L);
        User user2 = getUser();
        user2.setId(2L);
        user2.setActive(false);

        given(userRepository.findById(user1.getId())).willReturn(Optional.of(user1));
        given(userRepository.findById(user2.getId())).willReturn(Optional.of(user2));

        // when
        userService.activateOrDeactivateUserById(user1.getId());
        userService.activateOrDeactivateUserById(user2.getId());

        // then
        assertThat(user1.isActive(), is(false));
        assertThat(user2.isActive(), is(true));
    }

    @Test
    void should_be_able_to_build_user_for_registration() {
        // given
        User user = getUser();

        // when
        userService.registerUser(user.getEmail(), user.getUsername(), user.getPassword());

        // then
        then(userRepository).should().save(any(User.class));
    }

    @Test
    void should_throw_exception_while_registering_user_with_username_that_already_exists() {
        // given
        User user = getUser();
        given(userRepository.findByUsername(user.getUsername())).willReturn(Optional.of(user));

        // when
        CreatedUserExistException exception = assertThrows(CreatedUserExistException.class, () -> userService
                .registerUser(user.getEmail(), user.getUsername(), user.getPassword()));

        // then
        assertThat(exception.getMessage(), is(new CreatedUserExistException(user.getUsername()).getMessage()));
    }

    @Test
    void should_throw_exception_when_giving_user_not_found() {
        // given
        String username = "JohnDoe";

        // when
        Throwable exception = assertThrows(UserNotExistException.class,
                () -> userService.findUserByUsername(username));

        // then
        assertThat(exception.getMessage(), is(new UserNotExistException(username).getMessage()));
    }

    @Test
    void should_return_false_if_user_is_not_registered() {
        // given
        String username = "Johnny";

        // when
        boolean isRegistered = userService.checkIfUserWithGivenUsernameIsRegistered(username);

        // then
        assertThat(isRegistered, is(false));
    }

    @Test
    void should_throw_exception_when_user_with_given_username_is_registered() {
        // given
        List<User> userList = getUserList();
        User givenUser = userList.get(0);
        String givenUsername = userList.get(0).getUsername();

        given(userRepository.findByUsername(givenUsername)).willReturn(Optional.ofNullable(givenUser));

        // when
        CreatedUserExistException exception = assertThrows(CreatedUserExistException.class,
                () -> userService.checkIfUserWithGivenUsernameIsRegistered(givenUsername));

        // then
        assertThat(exception.getMessage(), is(new CreatedUserExistException(givenUsername).getMessage()));
    }

    @Test
    void should_return_email_when_not_registered_by_user() {
        // given
        String notRegisteredMail = "admin@page.pl";

        // when
        String email = userService.checkIfUserWithGivenEmailIsRegistered(notRegisteredMail);

        // then
        assertThat(email, is(notRegisteredMail));
    }

    @Test
    void should_throw_exception_when_user_with_given_email_is_already_registered() {
        // given
        List<User> registeredListOfUsers = getUserList();
        String registeredMail = registeredListOfUsers.get(0).getEmail();

        given(userRepository.findAll()).willReturn(registeredListOfUsers);

        // when
        CreatedUserExistException exception = assertThrows(CreatedUserExistException.class,
                () -> userService.checkIfUserWithGivenEmailIsRegistered(registeredMail));

        // then
        assertThat(exception.getMessage(), is(new CreatedUserExistException(registeredMail).getMessage()));
    }

    @Test
    void should_return_email_when_matches_to_regex() {
        // given
        String givenEmail = "testmail321@mail.com";

        // when
        String email = userService.emailMatcher(givenEmail);

        // then
        assertThat(email, is(givenEmail));
    }
    @Test
    void should_throw_exception_when_giving_wrong_email_format() {
        // given
        String givenWrongEmail1 = "testmail100mail.pl";
        String givenWrongEmail2 = "testmail101@";
        String givenWrongEmail3 = "@mail.com";

        // when
        Throwable exception1 = assertThrows(WrongEmailException.class,
                () -> userService.emailMatcher(givenWrongEmail1));

        Throwable exception2 = assertThrows(WrongEmailException.class,
                () -> userService.emailMatcher(givenWrongEmail2));

        Throwable exception3 = assertThrows(WrongEmailException.class,
                () -> userService.emailMatcher(givenWrongEmail3));

        // then
        assertThat(exception1.getMessage(), is(new WrongEmailException(givenWrongEmail1).getMessage()));
        assertThat(exception2.getMessage(), is(new WrongEmailException(givenWrongEmail2).getMessage()));
        assertThat(exception3.getMessage(), is(new WrongEmailException(givenWrongEmail3).getMessage()));
    }

    @Test
    void should_throw_exception_when_giving_blank_password() {
        // given
        String blankPassword = "";

        // when
        Throwable exception = assertThrows(WrongPasswordException.class,
                () -> userService.checkIfPasswordIsNotBlank(blankPassword));

        // then
        assertThat(exception.getMessage(), is(new WrongPasswordException().getMessage()));
    }



    private static User getUser() {
        return new User("R2D2SW", "mypassword", "r2d2sw@page.com", true);
    }

    private static List<User> getUserList() {
        User user = new User("testeduser@page.com", "password", "test@page.com", true);
        return List.of(user);
    }
}