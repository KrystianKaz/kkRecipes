package com.example.kkRecipes.service;

import com.example.kkRecipes.exception.custom.*;
import com.example.kkRecipes.model.User;
import com.example.kkRecipes.model.enums.UserRolesEnum;
import com.example.kkRecipes.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public List<User> findAllUsers() {
        return userRepository.findAll();
    }

    public User findUserByUsername(String username) {
        return userRepository.findByUsername(username).orElseThrow(() -> new UserNotExistException(username));
    }

    public void activateOrDeactivateUserById(long id, Principal principal) {
        User userIsAdminOrModerator = findUserByUsername(principal.getName());

        if (userIsAdminOrModerator.getUserRoles().equals(UserRolesEnum.ADMIN) ||
                userIsAdminOrModerator.getUserRoles().equals(UserRolesEnum.MODERATOR)) {

            User givenUser = userRepository.findById(id).orElseThrow(() -> new UserNotExistException(id));

            if (!givenUser.getUserRoles().equals(UserRolesEnum.ADMIN)) {
                givenUser.setActive(!givenUser.isActive());
                userRepository.save(givenUser);
            } else throw new IllegalOperationException();
        } else throw new IllegalOperationException();
    }

    public void registerUser(String email, String username, String password) {
        if (!checkIfUserWithGivenUsernameIsRegistered(username)) {
            User user = User.builder().email(emailMatcher(checkIfUserWithGivenEmailIsRegistered(email))).username(username).password(checkIfPasswordIsNotBlank(password)).active(true).accountCreationTime(LocalTime.now()).accountCreationDate(LocalDate.now()).userRoles(UserRolesEnum.USER).likedMeals(null).build();
            userRepository.save(user);
        }
    }

    public boolean checkIfUserWithGivenUsernameIsRegistered(String username) {
        Optional<User> byUsername = userRepository.findByUsername(username);
        if (byUsername.isPresent()) {
            throw new CreatedUserExistException(username);
        }
        return false;
    }

    public String checkIfUserWithGivenEmailIsRegistered(String email) {
        List<User> allRegisteredUsers = userRepository.findAll();
        for (User user : allRegisteredUsers) {
            if (user.getEmail().equals(email)) {
                throw new CreatedUserExistException(email);
            }
        }
        return email;
    }

    public String emailMatcher(String email) {
        if (!email.matches("^[A-Za-z\\d._%+-]+@[A-Za-z\\d.-]+\\.[A-Za-z]{2,6}$")) {
            throw new WrongEmailException(email);
        } else return email;
    }

    public String checkIfPasswordIsNotBlank(String password) {
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        if (password.length() > 0) {
            return bCryptPasswordEncoder.encode(password);
        } else throw new WrongPasswordException();
    }

}
