package study.server.domain.basket.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import study.server.domain.basket.entity.BasketDetail;

@Repository
public interface BasketDetailRepository extends JpaRepository<BasketDetail, Long> {
}
