package online.devwiki.common.user.comuser;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "common_user", schema = "canyon")
public class CommonUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long userId;

    @Column(nullable = false, unique = true)
    private String loginId;
    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String name;
    private String gender;
    private LocalDate dateOfBirth;

    private String status;
    private Boolean accountVerified;
    private String nickname;
    private String phoneNumber;
    private String streetAddress;
    private String city;
    private String state;
    private String country;
    private String postalCode;
    private String profilePictureUrl;

    public CommonUser(){
    }

    public CommonUser(Long userId, String loginId, String email, String name, String gender, LocalDate dateOfBirth, String status, Boolean accountVerified, String nickname, String phoneNumber, String streetAddress, String city, String state, String country, String postalCode, String profilePictureUrl) {
        this.userId = userId;
        this.loginId = loginId;
        this.email = email;
        this.name = name;
        this.gender = gender;
        this.dateOfBirth = dateOfBirth;
        this.status = status;
        this.accountVerified = accountVerified;
        this.nickname = nickname;
        this.phoneNumber = phoneNumber;
        this.streetAddress = streetAddress;
        this.city = city;
        this.state = state;
        this.country = country;
        this.postalCode = postalCode;
        this.profilePictureUrl = profilePictureUrl;
    }

    public Long getUserId() {
        return userId;
    }

    public String getLoginId() {
        return loginId;
    }

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }

    public String getGender() {
        return gender;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public String getStatus() {
        return status;
    }

    public Boolean getAccountVerified() {
        return accountVerified;
    }

    public String getNickname() {
        return nickname;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getStreetAddress() {
        return streetAddress;
    }

    public String getCity() {
        return city;
    }

    public String getState() {
        return state;
    }

    public String getCountry() {
        return country;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public String getProfilePictureUrl() {
        return profilePictureUrl;
    }
}