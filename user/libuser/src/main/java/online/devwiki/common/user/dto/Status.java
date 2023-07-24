package online.devwiki.common.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Objects;

@AllArgsConstructor
@Getter
public enum Status {
    ACTIVE("ACTIVE"),
    PAUSED("PAUSED"),
    EXPIRED("EXPIRED"),
    LOCKED("LOCKED");

    private final String value;

    public boolean isValid() {
        return Objects.equals(this.value, "ACTIVE");
    }
}
