package Appcenter.study.domain.purchase;

import Appcenter.study.domain.game.Game;
import Appcenter.study.domain.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PurchaseRepository extends JpaRepository<Purchase, Long> {
    Optional<Purchase> findByMemberAndGame(Member member, Game game);
}
