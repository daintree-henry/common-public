package online.devwiki.common.user.mapper;

import online.devwiki.common.user.domain.model.CommonUserInfo;
import online.devwiki.common.user.dto.CommonUserInfoDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface
CommonUserInfoMapper {

    CommonUserInfoMapper INSTANCE = Mappers.getMapper(CommonUserInfoMapper.class);

    CommonUserInfoDto.General toGeneralDto(CommonUserInfo commonUserInfo);

    @Mapping(target = "userInfoId", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    CommonUserInfo toCreateEntity(CommonUserInfoDto.Create createDto);
}
