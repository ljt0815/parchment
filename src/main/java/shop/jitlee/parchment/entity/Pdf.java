package shop.jitlee.parchment.entity;

import lombok.*;
import org.hibernate.annotations.ColumnDefault;

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

    @ColumnDefault("false")
    @Column(columnDefinition = "TINYINT(1)")
    private boolean isConverted;

    private int pageTotal;
}
