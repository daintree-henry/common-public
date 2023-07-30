package online.devwiki.common.user.mapper;

import online.devwiki.common.user.domain.model.CommonUser;
import online.devwiki.common.user.dto.CommonUserDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface CommonUserMapper {

    CommonUserMapper INSTANCE = Mappers.getMapper(CommonUserMapper.class);

    CommonUserDto.General toGeneralDto(CommonUser commonUser);

    @Mapping(target = "userId", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "userInfo.userInfoId", ignore = true)
    @Mapping(target = "userInfo.updatedAt", ignore = true)
    CommonUser toCreateEntity(CommonUserDto.Create createDto);
}
