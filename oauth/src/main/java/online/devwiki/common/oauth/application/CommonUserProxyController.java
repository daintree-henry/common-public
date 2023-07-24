package online.devwiki.common.oauth.application;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import online.devwiki.common.oauth.domain.service.proxy.CommonUserProxy;
import online.devwiki.common.user.dto.CommonUserDto;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class CommonUserProxyController {

    private final CommonUserProxy commonUserProxy;

    @PostMapping("/")
    public ResponseEntity<String> createUser(@Valid @RequestBody CommonUserDto.Create createDto) {
        return null;
    }

    @PutMapping("/{userId}")
    public ResponseEntity<String> updateUser(@PathVariable("userId") Long userId, @RequestBody CommonUserDto.Update updateDto) {
        return null;
    }

    @GetMapping("/{userId}")
    public ResponseEntity<CommonUserDto.General> getUserByUserId(@PathVariable("userId") Long userId) {
        CommonUserDto.General generalDto = commonUserProxy.getUserByUserId(userId);
        return new ResponseEntity<>(generalDto, HttpStatus.OK);
    }

    @GetMapping("/page")
    public ResponseEntity<Page<CommonUserDto.General>> pageUser(@Valid CommonUserDto.PageSearch pageSearchDto) {
        Page<CommonUserDto.General> pageDto = commonUserProxy.pageUser(pageSearchDto);
        return new ResponseEntity<>(pageDto, HttpStatus.OK);
    }
}
