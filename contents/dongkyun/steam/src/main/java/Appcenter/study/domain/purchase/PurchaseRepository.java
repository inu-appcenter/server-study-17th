package Appcenter.study.domain.purchase;

import Appcenter.study.domain.game.Game;
import Appcenter.study.domain.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PurchaseRepository extends JpaRepository<Purchase, Long> {
    void deleteByMemberAndGame(Member member, Game game);
}
