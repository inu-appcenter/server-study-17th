package Appcenter.study.repository;

import Appcenter.study.entity.Game;
import Appcenter.study.entity.Member;
import Appcenter.study.entity.Purchase;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PurchaseRepository extends JpaRepository<Purchase, Long> {
    void deleteByMemberAndGame(Member member, Game game);
}
