package online.devwiki.common.user.domain.service.implementation;

import com.querydsl.core.BooleanBuilder;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import online.devwiki.common.user.domain.repository.CommonUserRepository;
import online.devwiki.common.user.domain.service.CommonUserService;
import online.devwiki.common.user.dto.CommonUserDto;
import online.devwiki.common.user.infrastructure.util.EncryptionUtil;
import online.devwiki.common.user.infrastructure.util.MessageUtil;
import online.devwiki.common.user.domain.model.CommonUser;
import online.devwiki.common.user.domain.model.QCommonUser;
import online.devwiki.common.user.mapper.CommonUserMapper;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class CommonUserServiceImpl implements CommonUserService {

    private final CommonUserRepository commonUserRepository;

    @Transactional
    @Override
    public Long createCommonUser(CommonUserDto.Create createDto) {
        createDto.setPassword(EncryptionUtil.encrypt(createDto.getPassword()));
        if (isDuplicatedLoginId(createDto.getLoginId()))
            throw new DuplicateKeyException(MessageUtil.IS_DUPLICATED.makeMessage("사용자 ID"));

        CommonUser commonUser = CommonUserMapper.INSTANCE.toCreateEntity(createDto);
        return commonUserRepository.save(commonUser).getUserId();
    }


    @Override
    public CommonUserDto.General getUserByUserId(Long userId) {
        Optional<CommonUser> commonUser = commonUserRepository.findById(userId);
        if (commonUser.isEmpty()) {
            log.error("사용자를 찾을 수 없음, userId: {}", userId);
            return new CommonUserDto.General();
        }

        return CommonUserMapper.INSTANCE.toGeneralDto(commonUser.get());
    }

    @Override
    public CommonUserDto.General getUserByLoginId(String loginId) {
        Optional<CommonUser> commonUser = commonUserRepository.findByLoginId(loginId);
        if (commonUser.isEmpty()) {
            log.error("사용자를 찾을 수 없음, loginId: {}", loginId);
            return new CommonUserDto.General();
        }

        return CommonUserMapper.INSTANCE.toGeneralDto(commonUser.get());
    }

    @Override
    public Boolean authenticateUser(CommonUserDto.Auth authDto) {
        Optional<CommonUser> commonUser = commonUserRepository.findByLoginId(authDto.getLoginId());
        if (commonUser.isEmpty()) {
            log.error("미등록 사용자 인증 시도, loginId: {}", authDto.getLoginId());
            return false;
        }

        return EncryptionUtil.match(authDto.getPassword(), commonUser.get().getPassword());
    }

    @Override
    public Page<CommonUserDto.General> pageUsers(CommonUserDto.PageSearch pageSearchDto) {
        BooleanBuilder builder = createPageUsersPredicate(pageSearchDto);
        Pageable pageable = PageRequest.of(pageSearchDto.getPageNumber(), pageSearchDto.getPageSize());

        assert builder.getValue() != null;
        Page<CommonUser> commonUserPage = commonUserRepository.findAll(builder.getValue(), pageable);

        return commonUserPage.map(CommonUserMapper.INSTANCE::toGeneralDto);
    }

    @Override
    public Boolean isDuplicatedLoginId(String loginId) {
        Optional<CommonUser> commonUser = commonUserRepository.findByLoginId(loginId);
        return commonUser.isPresent();
    }

    private BooleanBuilder createPageUsersPredicate(CommonUserDto.PageSearch pageSearchDto) {
        BooleanBuilder builder = new BooleanBuilder();

        if (pageSearchDto.getName() != null)
            builder.and(QCommonUser.commonUser.name.contains(pageSearchDto.getName()));

        if (pageSearchDto.getLoginId() != null)
            builder.and(QCommonUser.commonUser.loginId.eq(pageSearchDto.getLoginId()));

        if (pageSearchDto.getEmail() != null)
            builder.and(QCommonUser.commonUser.email.contains(pageSearchDto.getEmail()));

        if (pageSearchDto.getStatus() != null)
            builder.and(QCommonUser.commonUser.status.eq(pageSearchDto.getStatus().toString()));

        if (pageSearchDto.getAccountVerified() != null)
            builder.and(QCommonUser.commonUser.accountVerified.eq(pageSearchDto.getAccountVerified()));

        if (pageSearchDto.getGender() != null)
            builder.and(QCommonUser.commonUser.gender.eq(pageSearchDto.getGender().toString()));

        if (pageSearchDto.getStreetAddress() != null)
            builder.and(QCommonUser.commonUser.userInfo.streetAddress.eq(pageSearchDto.getStreetAddress()));

        if (pageSearchDto.getCity() != null)
            builder.and(QCommonUser.commonUser.userInfo.city.eq(pageSearchDto.getCity()));

        if (pageSearchDto.getOccupation() != null)
            builder.and(QCommonUser.commonUser.userInfo.occupation.eq(pageSearchDto.getOccupation()));

        return builder;
    }

}
