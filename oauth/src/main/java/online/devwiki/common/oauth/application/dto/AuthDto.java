package online.devwiki.common.oauth.application.dto;

import jakarta.validation.constraints.NotNull;
import lombok.*;

public class AuthDto {

    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class AuthenticationRequest {
        @NotNull
        private String loginId;

        @NotNull
        private String password;
    }

    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class AuthenticationResponse {
        private String accessToken;
        private String refreshToken;
    }

}
