package online.devwiki.common.oauth.domain.service;

import lombok.RequiredArgsConstructor;
import online.devwiki.common.oauth.domain.service.proxy.CommonUserProxy;
import online.devwiki.common.user.dto.CommonUserDetail;
import online.devwiki.common.user.dto.CommonUserDto;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DevWikiUserDetailService implements UserDetailsService {

    private final CommonUserProxy commonUserProxy;

    @Override
    public CommonUserDetail loadUserByUsername(String username) throws UsernameNotFoundException {
        if (username == null) return null;
        CommonUserDto.LoginId loginId = new CommonUserDto.LoginId(username);
        CommonUserDto.General generalDto = commonUserProxy.loadUserByUsername(loginId);

        return CommonUserDetail.toUserDetail(generalDto);
    }

    public Boolean authenticateUser(String username, String password) throws UsernameNotFoundException {
        CommonUserDto.Auth auth = CommonUserDto.Auth.builder()
                .loginId(username)
                .password(password)
                .build();

        return commonUserProxy.authenticateUser(auth);
    }
}
