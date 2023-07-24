package online.devwiki.common.user.infrastructure.util;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum MessageUtil {
    IS_REQUIRED("%s 은(는) 필수입니다."),
    IS_INVALID("%s 의 형식이 잘못되었습니다."),
    NOT_FOUND("%s 을(를) 찾을 수 없습니다."),
    IS_DUPLICATED("%s 이(가) 중복됩니다.");

    private final String message;

    public String makeMessage(String param) {
        return String.format(this.message, param);
    }
}

