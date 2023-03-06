package com.example.kkRecipes;

import com.example.kkRecipes.model.User;
import com.example.kkRecipes.model.enums.UserRolesEnum;

import java.util.Arrays;
import java.util.List;

public interface TestDataSample {

    default List<User> getSampleDataForUsers() {
        User user1 = new User("FirstUser", "firstP", "first@email.com", true);
        User user2 = new User("SecondUser", "secondP", "second@email.com", true);
        User user3 = new User("ThirdUser", "thirdP", "third@email.com", true);
        User user4 = new User("FourthUser", "fourthP", "fourth@email.com", false);
        User user5 = new User("FifthUser", "fifthP", "fifth@email.com", false);
        user1.setUserRoles(UserRolesEnum.ADMIN);
        user2.setUserRoles(UserRolesEnum.MODERATOR);
        user3.setUserRoles(UserRolesEnum.USER);
        user4.setUserRoles(UserRolesEnum.USER);
        user5.setUserRoles(UserRolesEnum.USER);
        return Arrays.asList(user1, user2, user3, user4, user5);
    }
}
