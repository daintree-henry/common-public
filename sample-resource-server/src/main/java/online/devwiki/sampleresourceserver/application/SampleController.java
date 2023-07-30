package online.devwiki.sampleresourceserver.application;

import lombok.RequiredArgsConstructor;
import online.devwiki.common.user.dto.CommonUserDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/sample")
@RequiredArgsConstructor
public class SampleController {

    private final UserDetailsService userDetailService;

    @GetMapping("")
    public ResponseEntity<String> get(@AuthenticationPrincipal Long userId) {
        CommonUserDetail userDetail = (CommonUserDetail) userDetailService.loadUserByUsername(userId.toString());
        return ResponseEntity.ok(
                "{\"message\": \"Hello!, " + userDetail.getName() + "\"}"
        );
    }
}
