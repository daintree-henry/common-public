package online.devwiki.common.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import online.devwiki.common.user.role.CommonRoleDto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

public class CommonUserDto {

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class FullInfo {
        Long userId;
        String loginId;
        String password;
        String email;
        LocalDate dateOfBirth;
        Status status;
        Gender gender;
        String name;
        Boolean accountVerified;
        LocalDateTime updatedAt;
        LocalDateTime createdAt;
        Set<CommonRoleDto> roleSet;
        CommonUserInfoDto.General userInfo;
    }

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class General {
        Long userId;
        String loginId;
        String email;
        LocalDate dateOfBirth;
        Status status;
        Gender gender;
        String name;
        Boolean accountVerified;
        LocalDateTime createdAt;
        LocalDateTime updatedAt;
        Set<CommonRoleDto> roleSet;
        CommonUserInfoDto.General userInfo;
    }


    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Update {
        String email;
        LocalDate dateOfBirth;
        Status status;
        Gender gender;
        String name;
        Boolean accountVerified;
        Set<CommonRoleDto> roleSet;
        CommonUserInfoDto.General userInfo;
    }

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Create {
        @NotBlank
        String loginId;

        @NotBlank
        @Email
        String email;

        @NotBlank
        String password;
        Gender gender;

        String name;
        LocalDate dateOfBirth;
        @Builder.Default
        Status status = Status.ACTIVE;
        @Builder.Default
        Boolean accountVerified = false;
        @Builder.Default
        LocalDateTime createdAt = LocalDateTime.now();
        Set<CommonRoleDto> roleSet;
        CommonUserInfoDto.Create userInfo;
    }

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Auth {
        @NotBlank
        String loginId;

        @NotBlank
        String password;
    }

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class LoginId {
        @NotBlank
        String loginId;
    }

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PageSearch {
        String loginId;
        String email;
        Status status;
        Boolean accountVerified;
        Gender gender;
        String name;
        String streetAddress;
        String city;
        String occupation;
        Set<CommonRoleDto> roleSet;

        @Min(0)
        @Builder.Default
        Integer pageNumber = 0;

        @Min(1)
        @Max(250)
        @Builder.Default
        Integer pageSize = 1;
    }
}