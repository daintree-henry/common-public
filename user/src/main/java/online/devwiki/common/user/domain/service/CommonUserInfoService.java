package online.devwiki.common.user.domain.service;

import online.devwiki.common.user.dto.CommonUserInfoDto;

public interface CommonUserInfoService {

    Long createCommonUserInfo(CommonUserInfoDto.Create createDto);

    CommonUserInfoDto.General getCommonUserInfo(Long userInfoId);

}
