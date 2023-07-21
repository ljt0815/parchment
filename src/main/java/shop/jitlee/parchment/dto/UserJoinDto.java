package shop.jitlee.parchment.dto;

import lombok.*;
import shop.jitlee.parchment.entity.Member;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserJoinDto {

    @Pattern(regexp = "^[a-z\\d]{5,20}$")
    private String username;

    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[a-zA-Z\\d\\`\\~\\!\\@\\#\\$\\%\\^\\&\\*\\(\\)\\-\\_\\=\\+\\,\\<\\.\\>\\/\\?]{10,}$")
    private String password;

    @Email
    private String email;

    public Member toEntity() {
        return Member.builder()
                .email(email)
                .username(username)
                .password(password)
                .build();
    }
}
