package online.devwiki.common.user.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum Gender {
    MAN("MAN"),
    WOMAN("WOWAN"),
    OTHER("OTHER");

    private final String value;
}
