package online.devwiki.common.user.application;

import jakarta.persistence.EntityNotFoundException;
import online.devwiki.common.user.domain.service.CommonUserService;
import online.devwiki.common.user.dto.CommonUserDto;
import online.devwiki.common.user.dto.Status;
import online.devwiki.common.user.dto.Gender;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.time.LocalDate;
import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = CommonUserController.class)
public class CommonUserControllerTest {

    @MockBean
    private CommonUserService commonUserService;

    private MockMvc mockMvc;

    private CommonUserDto.General general;
    private String generalJsonResult;

    @BeforeEach
    void setUp(WebApplicationContext webApplicationContext) {
        MockitoAnnotations.openMocks(this);
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();

        general = CommonUserDto.General.builder()
                .loginId("testUser")
                .userId(1L)
                .name("testUser")
                .dateOfBirth(LocalDate.parse("1990-09-17"))
                .gender(Gender.MAN)
                .status(Status.ACTIVE)
                .email("testUser@qmail.com")
                .accountVerified(true)
                .build();
        generalJsonResult = """
                {"userId":1,"loginId":"testUser","email":"testUser@qmail.com","dateOfBirth":"1990-09-17","status":"ACTIVE","gender":"MAN","name":"testUser","accountVerified":true,"createdAt":null,"updatedAt":null,"roleSet":null,"userInfo":null}""";

        given(commonUserService.createCommonUser(any())).willReturn(1L);
        given(commonUserService.getUserByUserId(1L)).willReturn(general);
        given(commonUserService.getUserByUserId(2L)).willThrow(
                new EntityNotFoundException("사용자를 찾을 수 없습니다.")
        );
        given(commonUserService.authenticateUser(any())).willReturn(true);

        Page<CommonUserDto.General> page = new PageImpl<>(Collections.singletonList(general));
        given(commonUserService.pageUsers(any())).willReturn(page);
        given(commonUserService.isDuplicatedLoginId("existingUser")).willReturn(true);
        given(commonUserService.isDuplicatedLoginId("newUser")).willReturn(false);

    }

    @Test
    void createWithOutDetail() throws Exception {
        String postBody = """
                {
                    "loginId": "test_user",
                    "email": "test@email.com",
                    "password": "password",
                    "gender": "MAN",
                    "name": "test",
                    "dateOfBirth": "1990-09-17"
                }
                """;
        mockMvc.perform(post("/api/v1/common/common-user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(postBody))
                .andExpect(content().string("1"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        postBody = """
                {
                    "loginId": "test_user",
                    "email": "test@email.com",
                    "password": "password",
                    "gender": "MAN",
                    "name": "test",
                    "dateOfBirth": "1990-09-17",
                    "userDetail": ""
                }
                """;
        mockMvc.perform(post("/api/v1/common/common-user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(postBody))
                .andExpect(content().string("1"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void createWithNullAndEmptyLoginIdProperty() throws Exception {
        String postBody = """
                {
                    "loginId": "",
                    "email": "test@email.com",
                    "password": "password",
                    "gender": "MAN",
                    "name": "test",
                    "dateOfBirth": "1990-09-17"
                }
                """;
        mockMvc.perform(post("/api/v1/common/common-user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(postBody))
                .andExpect(status().isBadRequest());

        postBody = """
                {
                    "email": "test@email.com",
                    "password": "password",
                    "gender": "MAN",
                    "name": "test",
                    "dateOfBirth": "1990-09-17"
                }
                """;
        mockMvc.perform(post("/api/v1/common/common-user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(postBody))
                .andExpect(status().isBadRequest());

    }

    @Test
    void createWithNullAndEmptyEmailProperty() throws Exception {
        String postBody = """
                {
                    "loginId": "test_user",
                    "email": "",
                    "password": "password",
                    "gender": "MAN",
                    "name": "test",
                    "dateOfBirth": "1990-09-17"
                }
                """;
        mockMvc.perform(post("/api/v1/common/common-user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(postBody))
                .andExpect(status().isBadRequest());


        postBody = """
                {
                    "loginId": "test_user",
                    "password": "password",
                    "gender": "MAN",
                    "name": "test",
                    "dateOfBirth": "1990-09-17"
                }
                """;
        mockMvc.perform(post("/api/v1/common/common-user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(postBody))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createWithNullAndEmptyPasswordProperty() throws Exception {
        String postBody = """
                {
                    "loginId": "test_user",
                    "email": "test@email.com",
                    "password": "",
                    "gender": "MAN",
                    "name": "test",
                    "dateOfBirth": "1990-09-17"
                }
                """;
        mockMvc.perform(post("/api/v1/common/common-user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(postBody))
                .andExpect(status().isBadRequest());

        postBody = """
                {
                    "loginId": "test_user",
                    "email": "test@email.com",
                    "gender": "MAN",
                    "name": "test",
                    "dateOfBirth": "1990-09-17"
                }
                """;
        mockMvc.perform(post("/api/v1/common/common-user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(postBody))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createWithNullAndEmptyAndNullGenderProperty() throws Exception {
        String postBody = """
                {
                    "loginId": "test_user",
                    "email": "test@email.com",
                    "password": "password",
                    "gender": "",
                    "name": "test",
                    "dateOfBirth": "1990-09-17"
                }
                """;
        mockMvc.perform(post("/api/v1/common/common-user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(postBody))
                .andExpect(status().isBadRequest());

        postBody = """
                {
                    "loginId": "test_user",
                    "email": "test@email.com",
                    "password": "password",
                    "name": "test",
                    "dateOfBirth": "1990-09-17"
                }
                """;
        mockMvc.perform(post("/api/v1/common/common-user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(postBody))
                .andExpect(content().string("1"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void createWithNullAndEmptyNameProperty() throws Exception {
        String postBody = """
                {
                    "loginId": "test_user",
                    "email": "test@email.com",
                    "password": "password",
                    "gender": "MAN",
                    "name": "",
                    "dateOfBirth": "1990-09-17"
                }
                """;
        mockMvc.perform(post("/api/v1/common/common-user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(postBody))
                .andExpect(content().string("1"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());


        postBody = """
                {
                    "loginId": "test_user",
                    "email": "test@email.com",
                    "password": "password",
                    "gender": "MAN",
                    "dateOfBirth": "1990-09-17"
                }
                """;
        mockMvc.perform(post("/api/v1/common/common-user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(postBody))
                .andExpect(content().string("1"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void createWithNullAndEmptyDateOfBirthProperty() throws Exception {
        String postBody = """
                {
                    "loginId": "test_user",
                    "email": "test@email.com",
                    "password": "password",
                    "gender": "MAN",
                    "name": "test",
                    "dateOfBirth": ""
                }
                """;
        mockMvc.perform(post("/api/v1/common/common-user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(postBody))
                .andExpect(content().string("1"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        postBody = """
                {
                    "loginId": "test_user",
                    "email": "test@email.com",
                    "password": "password",
                    "gender": "MAN",
                    "name": "test"
                }
                """;
        mockMvc.perform(post("/api/v1/common/common-user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(postBody))
                .andExpect(content().string("1"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void createWithInvalidDateOfBirth() throws Exception {
        String postBody = """
                {
                    "loginId": "test_user",
                    "email": "test@example.com",
                    "password": "password",
                    "gender": "MAN",
                    "name": "test",
                    "dateOfBirth": "1990/09/17"
                }
                """;

        mockMvc.perform(post("/api/v1/common/common-user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(postBody))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createWithDetail() throws Exception {
        String postBody = """
                {
                    "loginId": "test_user",
                    "email": "test@email.com",
                    "password": "password",
                    "gender": "MAN",
                    "name": "test",
                    "dateOfBirth": "1990-09-17",
                    "userDetail": {
                        "nickname": "test",
                        "phoneNumber": "010-1234-5678",
                        "streetAddress": "123-13",
                        "city": "gang-nam",
                        "state": "seoul",
                        "country": "korea",
                        "postalCode": "12345",
                        "profilePictureUrl": "https://mypicture.com/1234",
                        "occupation": "developer"
                    }
                }
                """;

        mockMvc.perform(post("/api/v1/common/common-user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(postBody))
                .andExpect(content().string("1"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void getUser() throws Exception {
        mockMvc.perform(get("/api/v1/common/common-user/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().string(generalJsonResult))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        mockMvc.perform(get("/api/v1/common/common-user/2")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        mockMvc.perform(get("/api/v1/common/common-user/abc")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void authenticate() throws Exception {
        String postBody = """
                {
                    "loginId": "test_user",
                    "password": "password"
                }
                """;
        mockMvc.perform(post("/api/v1/common/common-user/authenticate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(postBody))
                .andExpect(content().string("true"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        postBody = """
                {
                    "loginId": "",
                    "password": "password"
                }
                """;
        mockMvc.perform(post("/api/v1/common/common-user/authenticate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(postBody))
                .andExpect(status().isBadRequest());

        postBody = """
                {
                    "loginId": "test_user",
                    "password": ""
                }
                """;
        mockMvc.perform(post("/api/v1/common/common-user/authenticate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(postBody))
                .andExpect(status().isBadRequest());

        postBody = """
                {
                    "password": "password"
                }
                """;
        mockMvc.perform(post("/api/v1/common/common-user/authenticate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(postBody))
                .andExpect(status().isBadRequest());

        postBody = """
                {
                    "loginId": "test_user"
                }
                """;
        mockMvc.perform(post("/api/v1/common/common-user/authenticate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(postBody))
                .andExpect(status().isBadRequest());
    }

    @Test
    void pageWithValidPageSearch() throws Exception {
        mockMvc.perform(get("/api/v1/common/common-user/page")
                        .param("loginId", "test")
                        .param("email", "test@example.com")
                        .param("pageNumber", "0")
                        .param("pageSize", "10")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void pageWithInvalidPageSearch() throws Exception {
        mockMvc.perform(get("/api/v1/common/common-user/page")
                        .param("pageNumber", "-1")
                        .param("pageSize", "100")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        mockMvc.perform(get("/api/v1/common/common-user/page")
                        .param("pageNumber", "10")
                        .param("pageSize", "10000")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

    }

    @Test
    void isDuplicatedWithExistingLoginId() throws Exception {
        String postBody = """
                {
                    "loginId": "existingUser"
                }
                """;

        mockMvc.perform(post("/api/v1/common/common-user/is-duplicated")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(postBody))
                .andExpect(content().string("true"))
                .andExpect(status().isOk());
    }

    @Test
    void isDuplicatedWithNonExistingLoginId() throws Exception {
        String postBody = """
                {
                    "loginId": "newUser"
                }
                """;

        mockMvc.perform(post("/api/v1/common/common-user/is-duplicated")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(postBody))
                .andExpect(content().string("false"))
                .andExpect(status().isOk());
    }

    @Test
    void isDuplicatedWithMissingLoginId() throws Exception {
        mockMvc.perform(post("/api/v1/common/common-user/is-duplicated")
                .contentType(MediaType.APPLICATION_JSON)
                .content(""))
                .andExpect(status().isBadRequest());
    }

}