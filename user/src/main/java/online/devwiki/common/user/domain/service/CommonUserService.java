package online.devwiki.common.user.domain.service;

import online.devwiki.common.user.dto.CommonUserDto;
import org.springframework.data.domain.Page;

public interface CommonUserService {

    Long createCommonUser(CommonUserDto.Create createDto);

    CommonUserDto.General getUserByUserId(Long userId);

    CommonUserDto.General getUserByLoginId(String LoginId);

    Boolean authenticateUser(CommonUserDto.Auth authDto);

    Page<CommonUserDto.General> pageUsers(CommonUserDto.PageSearch pageSearchDto);

    Boolean isDuplicatedLoginId(String loginId);
}

