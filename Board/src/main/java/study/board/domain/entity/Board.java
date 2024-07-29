package study.board.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@SQLDelete(sql = "UPDATE board SET isActive = false WHERE id = ?")
@SQLRestriction("is_active = true")
public class Board extends BaseEntity{

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String userName;

    private String password;

    private String content;

    @Column(columnDefinition = "BOOLEAN DEFAULT true")
    private Boolean isActive = true;


    @Builder
    public Board(String title, String userName, String password, String content) {
        this.title = title;
        this.userName = userName;
        this.password = password;
        this.content = content;
    }

}
