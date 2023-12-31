package shop.jitlee.parchment.dto;

import lombok.*;
import shop.jitlee.parchment.entity.Member;

import javax.validation.constraints.Pattern;

@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserJoinDto {

    @Pattern(regexp = "^[a-z\\d]{5,20}$")
    private String username;

    @Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*\\d)[a-zA-Z\\d\\`\\~\\!\\@\\#\\$\\%\\^\\&\\*\\(\\)\\-\\_\\=\\+\\,\\<\\.\\>\\/\\?]{10,}$")
    private String password;

    public Member toEntity() {
        return Member.builder()
                .username(username)
                .password(password)
                .build();
    }
}
