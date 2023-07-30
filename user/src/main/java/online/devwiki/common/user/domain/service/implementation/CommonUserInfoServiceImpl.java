package online.devwiki.common.user.domain.service.implementation;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import online.devwiki.common.user.domain.model.CommonUserInfo;
import online.devwiki.common.user.domain.repository.CommonUserInfoRepository;
import online.devwiki.common.user.domain.service.CommonUserInfoService;
import online.devwiki.common.user.dto.CommonUserInfoDto;
import online.devwiki.common.user.mapper.CommonUserInfoMapper;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class CommonUserInfoServiceImpl implements CommonUserInfoService {

    private final CommonUserInfoRepository repository;

    @Override
    public Long createCommonUserInfo(CommonUserInfoDto.Create createDto) {
        CommonUserInfo commonUserInfo = CommonUserInfoMapper.INSTANCE.toCreateEntity(createDto);
        return repository.save(commonUserInfo).getUserInfoId();
    }

    @Override
    public CommonUserInfoDto.General getCommonUserInfo(Long userInfoId) {
        Optional<CommonUserInfo> commonUserInfo = repository.findById(userInfoId);
        if (commonUserInfo.isEmpty()) throw new EntityNotFoundException("사용자 상세 정보가 존재하지 않습니다.");

        return CommonUserInfoMapper.INSTANCE.toGeneralDto(commonUserInfo.get());
    }
}
