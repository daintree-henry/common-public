package online.devwiki.common.oauth.application;


import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import online.devwiki.common.oauth.application.dto.AuthDto;
import online.devwiki.common.oauth.domain.service.CommonUserAuthenticationService;
import online.devwiki.common.oauth.jwt.JwtConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.io.IOException;
import java.util.Objects;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doAnswer;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = AuthenticationController.class)
public class AuthenticationControllerTest {

    @MockBean
    private CommonUserAuthenticationService commonUserAuthenticationService;

    @MockBean
    private JwtConfig jwtConfig;

    private MockMvc mockMvc;
    private String successResponseStr;

    @BeforeEach
    public void setup(WebApplicationContext webApplicationContext) throws IOException {
        MockitoAnnotations.openMocks(this);
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
        AuthDto.AuthenticationResponse successResponse = new AuthDto.AuthenticationResponse("accessToken", "refreshToken");
        successResponseStr = "{\"accessToken\":\"accessToken\",\"refreshToken\":\"refreshToken\"}";
        given(commonUserAuthenticationService.authenticateAndGetAuthResponse("testuser", "testpassword")).willReturn(successResponse);
        given(commonUserAuthenticationService.authenticateAndGetAuthResponse("testuser", "wrongPassword")).willReturn(null);
        doAnswer(invocation -> {
            HttpServletRequest request = invocation.getArgument(0);
            HttpServletResponse response = invocation.getArgument(1);
            if (Objects.equals(request.getHeader(HttpHeaders.AUTHORIZATION), "Bearer validRefreshToken")) {
                response.setHeader("accessToken", "newAccessToken");
                return true;
            }else{
                return false;
            }
        }).when(commonUserAuthenticationService).refreshToken(any(), any());
    }

    @Test
    public void testAuthenticateByLoginIdAndPassword() throws Exception {
        String postBody = """
                {
                    "loginId": "testuser",
                    "password": "testpassword"
                }
                """;
        mockMvc.perform(post("/api/v1/auth/authenticate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(postBody))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(successResponseStr));
    }

    @Test
    public void testAuthenticateByInvalidLoginIdAndPassword() throws Exception {
        String postBody = """
                {
                    "login_id": "testuser",
                    "password": "testpassword"
                }
                """;
        mockMvc.perform(post("/api/v1/auth/authenticate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(postBody))
                .andExpect(status().isBadRequest());

        postBody = """
                {
                    "loginId": "testuser"
                }
                """;
        mockMvc.perform(post("/api/v1/auth/authenticate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(postBody))
                .andExpect(status().isBadRequest());

        postBody = """
                {
                    "password": "testpassword"
                }
                """;
        mockMvc.perform(post("/api/v1/auth/authenticate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(postBody))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testRefreshToken() throws Exception {
        mockMvc.perform(post("/api/v1/auth/refresh-token")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer validRefreshToken"))
                .andExpect(status().isOk())
                .andExpect(header().string("accessToken", "newAccessToken"));
    }

    @Test
    public void testInvalidRefreshToken() throws Exception {
        mockMvc.perform(post("/api/v1/auth/refresh-token")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer InvalidRefreshToken"))
                .andExpect(status().isBadRequest())
                .andExpect(header().doesNotExist("accessToken"));
    }

}