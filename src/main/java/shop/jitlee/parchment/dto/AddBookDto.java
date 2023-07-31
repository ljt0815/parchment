package shop.jitlee.parchment.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import shop.jitlee.parchment.entity.Book;

@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AddBookDto {

    private String title;
    private Long id;
    private String uuid;

    public Book toEntity() {
        return Book.builder()
                .title(title)
                .uuid(uuid)
                .build();
    }
}
