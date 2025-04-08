package study.server.domain.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import study.server.domain.user.entity.Seller;

@Repository
public interface SellerRepository extends JpaRepository<Seller, Long> {
}
