package online.devwiki.common.user.application;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import online.devwiki.common.user.domain.service.CommonUserService;
import online.devwiki.common.user.dto.CommonUserDto;
import org.springframework.data.domain.Page;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/common/common-user")
public class CommonUserController {

    private final CommonUserService commonUserService;

    @PostMapping
    public Long create(@Valid @RequestBody CommonUserDto.Create createDto) {
        return commonUserService.createCommonUser(createDto);
    }

    @GetMapping("/{userId}")
    public CommonUserDto.General getUserByUserId(@PathVariable("userId") Long userId) {
        return commonUserService.getUserByUserId(userId);
    }

    @PostMapping("/login-id")
    public CommonUserDto.General getUserByLoginId(@RequestBody CommonUserDto.LoginId loginId) {
        if (!StringUtils.hasLength(loginId.getLoginId())) return null;
        return commonUserService.getUserByLoginId(loginId.getLoginId());
    }

    @PostMapping("/authenticate")
    public Boolean authenticate(@Valid @RequestBody CommonUserDto.Auth authDto) {
        return commonUserService.authenticateUser(authDto);
    }

    @GetMapping("/page")
    public Page<CommonUserDto.General> pageUser(@Valid CommonUserDto.PageSearch pageSearchDto) {
        return commonUserService.pageUsers(pageSearchDto);
    }

    @PostMapping("/is-duplicated")
    public Boolean isDuplicated(@RequestBody CommonUserDto.LoginId loginId) {
        if (!StringUtils.hasLength(loginId.getLoginId())) return false;
        return commonUserService.isDuplicatedLoginId(loginId.getLoginId());
    }
}
