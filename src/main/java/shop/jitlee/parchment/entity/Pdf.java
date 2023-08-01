package shop.jitlee.parchment.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Pdf {

    @Id
    @GeneratedValue
    @Column(name = "PDF_ID")
    private Long id;

    @Column(nullable = false)
    private String originFileName;

    @Column(nullable = false)
    private String path;

    private int pageTotal;
}
