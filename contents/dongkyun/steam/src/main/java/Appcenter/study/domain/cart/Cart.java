package Appcenter.study.domain.cart;

import Appcenter.study.domain.game.Game;
import Appcenter.study.domain.member.Member;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne
    @JoinColumn(name = "game_id")
    private Game game;

    @CreatedDate
    private LocalDateTime createAt;

    @Builder
    private Cart(Member member, Game game) {
        this.member = member;
        this.game = game;
        this.createAt = LocalDateTime.now();
    }

    public void setMember(Member member) {
        this.member = member;
    }
}
