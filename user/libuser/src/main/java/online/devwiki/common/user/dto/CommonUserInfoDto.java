package online.devwiki.common.user.dto;

import lombok.*;

import java.time.LocalDateTime;

public class CommonUserInfoDto {

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class General {
        Long userInfoId;
        String nickname;
        String phoneNumber;
        String streetAddress;
        String city;
        String state;
        String country;
        String postalCode;
        String profilePictureUrl;
        String occupation;
        LocalDateTime createdAt;
        LocalDateTime updatedAt;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Update {
        String nickname;
        String phoneNumber;
        String streetAddress;
        String city;
        String state;
        String country;
        String postalCode;
        String profilePictureUrl;
        String occupation;
        LocalDateTime createdAt;
        LocalDateTime updatedAt;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Create {
        String nickname;
        String phoneNumber;
        String streetAddress;
        String city;
        String state;
        String country;
        String postalCode;
        String profilePictureUrl;
        String occupation;
        @Builder.Default
        LocalDateTime createdAt = LocalDateTime.now();
    }
}
