package shop.jitlee.parchment.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Page {

    @Id @GeneratedValue
    @Column(name = "PAGE_ID")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "BOOK_ID", nullable = false)
    private Book book;

    @OneToOne
    @JoinColumn(name = "IMAGE_ID", nullable = false)
    private Image image;

    @OneToOne
    @JoinColumn(name = "NOTE_ID")
    private Note note;
}
