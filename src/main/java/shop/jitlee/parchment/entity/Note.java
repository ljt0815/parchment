package shop.jitlee.parchment.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.sql.Timestamp;

@Entity
@Getter @Setter
@NoArgsConstructor(access= AccessLevel.PROTECTED)
public class Note {

    @Id @GeneratedValue
    @Column(name = "NOTE_ID")
    private Long id;

    @Column(nullable = false)
    private String notePath;

    @CreationTimestamp
    private Timestamp postDate;
}
