package online.devwiki.common.oauth.domain.service.proxy;

import online.devwiki.common.user.dto.CommonUserDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "user", url = "http://${feginclient.url.common.user:localhost:7005}/api/v1/common/common-user")
public interface CommonUserProxy {

    @PostMapping("/login-id")
    CommonUserDto.General loadUserByUsername(@RequestBody CommonUserDto.LoginId loginId);

    @PostMapping("/authenticate")
    Boolean authenticateUser(@RequestBody CommonUserDto.Auth authDto);

    @PostMapping("/")
    Long createUser(@RequestBody CommonUserDto.Create createDto);

    @GetMapping("/{userId}")
    CommonUserDto.General getUserByUserId(@PathVariable("userId") Long userId);

    @GetMapping("/page")
    Page<CommonUserDto.General> pageUser(CommonUserDto.PageSearch pageSearchDto);

    @PostMapping("/is-duplicated")
    Boolean isDuplicated(@RequestBody CommonUserDto.LoginId loginId);
}
