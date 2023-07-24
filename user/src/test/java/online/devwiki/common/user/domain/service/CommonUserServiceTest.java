package online.devwiki.common.user.domain.service;

import online.devwiki.common.user.domain.model.CommonUser;
import online.devwiki.common.user.domain.model.CommonUserInfo;
import online.devwiki.common.user.domain.repository.CommonUserRepository;
import online.devwiki.common.user.domain.service.implementation.CommonUserServiceImpl;
import online.devwiki.common.user.dto.CommonUserDto;
import online.devwiki.common.user.dto.CommonUserInfoDto;
import online.devwiki.common.user.dto.Gender;
import online.devwiki.common.user.dto.Status;
import online.devwiki.common.user.infrastructure.util.EncryptionUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.when;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class CommonUserServiceTest {

    CommonUserInfo createdCommonUserInfo = CommonUserInfo.builder()
            .userInfoId(1L)
            .phoneNumber("010-1234-5678")
            .streetAddress("testStreet")
            .city("testCity")
            .state("testState")
            .country("testCountry")
            .postalCode("12345")
            .profilePictureUrl("https://image.com")
            .occupation("testjob")
            .build();
    CommonUser createdCommonUser = CommonUser.builder()
            .userId(1L)
            .loginId("testuserid")
            .email("testUsetId@qmail.com")
            .password(EncryptionUtil.encrypt("password"))
            .gender("MAN")
            .name("testusername")
            .dateOfBirth(LocalDate.parse("2020-10-10"))
            .status("ACTIVE")
            .accountVerified(false)
            .userInfo(createdCommonUserInfo).build();
    @InjectMocks
    private CommonUserServiceImpl commonUserService;
    @Mock
    private CommonUserRepository commonUserRepository;

    @BeforeEach
    public void setUp() {
        when(commonUserRepository.save(any())).thenReturn(createdCommonUser);
        when(commonUserRepository.findById(1L)).thenReturn(Optional.of(createdCommonUser));
        when(commonUserRepository.findById(100L)).thenReturn(Optional.empty());
        when(commonUserRepository.findByLoginId("testuserid")).thenReturn(Optional.of(createdCommonUser));
    }

    @Test
    public void createCommonUserTest() {
        //Given
        CommonUserInfoDto.Create createInfoDto = CommonUserInfoDto.Create.builder()
                .phoneNumber("010-1234-5678")
                .streetAddress("testStreet")
                .city("testCity")
                .state("testState")
                .country("testCountry")
                .postalCode("12345")
                .profilePictureUrl("https://image.com")
                .occupation("testjob")
                .build();

        CommonUserDto.Create createDto = CommonUserDto.Create.builder()
                .loginId("testuserid2")
                .email("testUsetId@qmail.com")
                .password("testpassword")
                .gender(Gender.MAN)
                .name("testusername")
                .dateOfBirth(LocalDate.parse("2020-10-10"))
                .status(Status.ACTIVE)
                .accountVerified(false)
                .userInfo(createInfoDto).build();

        //When
        Long userId = commonUserService.createCommonUser(createDto);

        //Then
        assertEquals(1L, userId);
    }

    @Test
    public void getUserByUserIdTest() {
        //When
        CommonUserDto.General result = commonUserService.getUserByUserId(1L);

        //Then
        assertNotNull(result);
        assertEquals(1L, result.getUserId());
        assertEquals("testuserid", result.getLoginId());
        assertEquals("testUsetId@qmail.com", result.getEmail());
        assertEquals("MAN", result.getGender().getValue());
        assertEquals("testusername", result.getName());
        assertEquals("2020-10-10", result.getDateOfBirth().toString());
        assertEquals("ACTIVE", result.getStatus().getValue());
        assertEquals(false, result.getAccountVerified());
        assertEquals(1L, result.getUserInfo().getUserInfoId());
        assertEquals("010-1234-5678", result.getUserInfo().getPhoneNumber());
        assertEquals("testState", result.getUserInfo().getState());
        assertEquals("testCity", result.getUserInfo().getCity());
        assertEquals("testStreet", result.getUserInfo().getStreetAddress());
        assertEquals("testCountry", result.getUserInfo().getCountry());
        assertEquals("12345", result.getUserInfo().getPostalCode());
    }

    @Test
    public void getUserByUserNotValidIdTest() {
        //When & Then
    }

    @Test
    public void authenticateUserTest() {
        CommonUserDto.Auth authDto = CommonUserDto.Auth.builder()
                .loginId("testuserid")
                .password("password")
                .build();

        Boolean result = commonUserService.authenticateUser(authDto);

        assertTrue(result);
    }

    @Test
    public void authenticateInvalidUserTest() {
        CommonUserDto.Auth authDto = CommonUserDto.Auth.builder()
                .loginId("testuserid")
                .password("password223")
                .build();

        Boolean result = commonUserService.authenticateUser(authDto);

        assertFalse(result);
    }

}
