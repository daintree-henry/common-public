package online.devwiki.common.user.domain.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "common_user_info", schema = "canyon")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommonUserInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_info_id")
    private Long userInfoId;

    private String nickname;
    private String phoneNumber;
    private String streetAddress;
    private String city;
    private String state;
    private String country;
    private String postalCode;
    private String profilePictureUrl;
    private String occupation;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}