package com.codesoom.assignment.application;

import com.codesoom.assignment.UserNotFoundException;
import com.codesoom.assignment.domain.User;
import com.codesoom.assignment.dto.UserData;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@ActiveProfiles("test")
@DisplayName("UserService 클래스")
class UserServiceTest {
    final Long NOT_EXIST_ID = 1000L;
    final String NAME = "My Name";
    final String EMAIL = "my@gmail.com";
    final String PASSWORD = "My Password";

    final String UPDATE_NAME = "Your Name";
    final String UPDATE_EMAIL = "user@gmail.com";
    final String UPDATE_PASSWORD = "Your Password";

    @Autowired
    private UserService userService;

    //subject
    UserData createUser() {
        return UserData.builder()
                .name(NAME)
                .email(EMAIL)
                .password(PASSWORD)
                .build();
    }

    void verifyUser(User user) {
        assertThat(user.getName()).isEqualTo(NAME);
        assertThat(user.getEmail()).isEqualTo(EMAIL);
        assertThat(user.getPassword()).isEqualTo(PASSWORD);
    }

    @AfterEach
    void tearDown() {
        userService.clearData();
    }

    @DisplayName("createUser()")
    @Nested
    class Describe_create {
        @Nested
        @DisplayName("유저 정보가 주어진다면")
        class Context_with_user {
            UserData givenUser;

            @BeforeEach
            void setUp() {
                givenUser = UserData.builder()
                        .name(NAME)
                        .email(EMAIL)
                        .password(PASSWORD)
                        .build();
            }

            User subject() {
                return userService.createUser(givenUser);
            }

            @DisplayName("생성된 user를 반환한다")
            @Test
            void it_returns_user() {
                User user = subject();
                verifyUser(user);
            }
        }
    }

    @DisplayName("findUser()")
    @Nested
    class Describe_findById {
        @Nested
        @DisplayName("존재하는 user id가 주어진다면")
        class Context_with_exist_user_id {
            Long givenUserId;

            @BeforeEach
            void setUp() {
                UserData source = createUser();
                User givenUser = userService.createUser(source);
                givenUserId = givenUser.getId();
            }

            @DisplayName("주어진 id와 일치하는 user를 반환한다")
            @Test
            void it_returns_user() {
                User user = userService.findUser(givenUserId);
                verifyUser(user);
            }
        }

        @Nested
        @DisplayName("존재하지 않는 user id와 user가 주어진다면")
        class Context_with_not_exist_user_id {
            Long givenUserId = NOT_EXIST_ID;

            @DisplayName("user를 찾을수 없다는 예외를 던진다")
            @Test
            void it_returns_not_fount_exception() {
                assertThrows(UserNotFoundException.class, () -> userService.findUser(givenUserId));
            }
        }
    }

    @DisplayName("updateUser()")
    @Nested
    class Describe_update {
        User subject(Long id, UserData source) {
            return userService.updateUser(id, source);
        }

        @Nested
        @DisplayName("존재하는 user id와 user가 주어진다면")
        class Context_with_exist_user_id {
            Long givenId;
            UserData source;

            @BeforeEach
            void setUp() {
                UserData givenUser = createUser();
                givenId = userService.createUser(givenUser).getId();
                source = UserData.builder()
                        .name(UPDATE_NAME)
                        .email(UPDATE_EMAIL)
                        .password(UPDATE_PASSWORD)
                        .build();
            }

            @DisplayName("수정된 user를 반환한다")
            @Test
            void it_returns_user() {
                User user = subject(givenId, source);

                assertThat(user.getName()).isEqualTo(UPDATE_NAME);
                assertThat(user.getEmail()).isEqualTo(UPDATE_EMAIL);
                assertThat(user.getPassword()).isEqualTo(UPDATE_PASSWORD);
            }
        }

        @Nested
        @DisplayName("존재하지 않는 user id와 user가 주어진다면")
        class Context_with_not_exist_user_id {
            Long givenId;
            UserData source;

            @BeforeEach
            void setUp() {
                givenId = NOT_EXIST_ID;
                source = UserData.builder()
                        .name(UPDATE_NAME)
                        .email(UPDATE_EMAIL)
                        .password(UPDATE_PASSWORD)
                        .build();
            }

            @DisplayName("user를 찾을수 없다는 예외를 던진다")
            @Test
            void it_returns_not_fount_exception() {
                assertThrows(UserNotFoundException.class, () -> subject(givenId, source));
            }
        }
    }

    @Nested
    @DisplayName("deleteUser()")
    class Describe_Delete {
        User subject(Long id) {
            return userService.deleteUser(id);
        }

        @Nested
        @DisplayName("존재하는 user id와 user가 주어진다면")
        class Context_with_exist_user_id {
            Long givenId;

            @BeforeEach
            void setUp() {
                UserData givenUser = createUser();
                givenId = userService.createUser(givenUser).getId();
            }

            @DisplayName("삭제된 user를 반환한다")
            @Test
            void it_returns_user() {
                User user = subject(givenId);
                verifyUser(user);
            }
        }

        @Nested
        @DisplayName("존재하지 않는 user id와 user가 주어진다면")
        class Context_with_not_exist_user_id {
            Long givenId = NOT_EXIST_ID;

            @DisplayName("user를 찾을수 없다는 예외를 던진다")
            @Test
            void it_returns_not_fount_exception() {
                assertThrows(UserNotFoundException.class, () -> subject(givenId));
            }
        }
    }
}
