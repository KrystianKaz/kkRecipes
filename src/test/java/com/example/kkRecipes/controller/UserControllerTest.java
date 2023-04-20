package com.example.kkRecipes.controller;

import com.example.kkRecipes.model.User;
import com.example.kkRecipes.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private UserRepository userRepository;

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    void should_get_profile_page_when_user_is_admin() throws Exception {
        // given
        User givenUser = getUser();

        // when + then
        when(userRepository.findByUsername(givenUser.getUsername())).thenReturn(Optional.of(givenUser));

        mockMvc.perform(get("/user/profile/" + givenUser.getUsername())
                .with(csrf()))
                .andExpect(status().is2xxSuccessful())
                .andExpect(model().attribute("user",
                        hasProperty("username", is("test"))));
    }

    private static User getUser() {
        return new User("test", "test", "test@test.com", true);
    }
}