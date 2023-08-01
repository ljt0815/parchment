package shop.jitlee.parchment.entity;

import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.sql.Timestamp;

@Entity
@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Image {

    @Id @GeneratedValue
    @Column(name = "IMAGE_ID")
    private Long id;

    @Column(nullable = false)
    private String imgPath;

    @CreationTimestamp
    private Timestamp postDate;
}
